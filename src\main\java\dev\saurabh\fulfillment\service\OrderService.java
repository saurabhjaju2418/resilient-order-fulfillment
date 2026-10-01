package dev.saurabh.fulfillment.service;
import dev.saurabh.fulfillment.api.*;
import dev.saurabh.fulfillment.domain.*;
import dev.saurabh.fulfillment.repo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class OrderService {
 private final OrderRepository orders; private final OrderLineRepository lines; private final OutboxRepository outbox;
 private final IdempotencyRepository idempotency; private final ObjectMapper mapper;
 public OrderService(OrderRepository o,OrderLineRepository l,OutboxRepository b,IdempotencyRepository i,ObjectMapper m){orders=o;lines=l;outbox=b;idempotency=i;mapper=m;}

 @Transactional
 public OrderView create(CreateOrderRequest req,String key){
  if(key==null||key.isBlank()||key.length()>128)throw new IllegalArgumentException("A valid Idempotency-Key header is required");
  String hash=hash(req);
  var prior=idempotency.findById(key);
  if(prior.isPresent()){
   if(!prior.get().requestHash.equals(hash))throw new IllegalStateException("Idempotency key was already used with a different request");
   var existing=orders.findById(prior.get().responseOrderId).orElseThrow();
   return OrderView.of(existing,lines.findByOrderId(existing.id));
  }
  Instant now=Instant.now(); var order=orders.save(new FulfillmentOrder(req.externalRef(),req.customerRef(),now));
  req.lines().forEach(line->lines.save(new OrderLine(order,line.sku(),line.quantity())));
  outbox.save(new OutboxEvent(order.id,"OrderReceived","{\"orderId\":\""+order.id+"\",\"status\":\"RECEIVED\"}",now));
  idempotency.save(new IdempotencyRecord(key,hash,order.id,now));
  return OrderView.of(order,lines.findByOrderId(order.id));
 }

 @Transactional
 public OrderView transition(UUID id,OrderStatus target){
  var order=orders.findById(id).orElseThrow(()->new EntityNotFoundException("Order not found"));
  boolean allowed=switch(order.status){
   case RECEIVED -> target==OrderStatus.RESERVED||target==OrderStatus.CANCELLED;
   case RESERVED -> target==OrderStatus.PICKING||target==OrderStatus.CANCELLED;
   case PICKING -> target==OrderStatus.SHIPPED||target==OrderStatus.CANCELLED;
   case SHIPPED -> target==OrderStatus.DELIVERED;
   case DELIVERED,CANCELLED -> false;
  };
  if(!allowed)throw new IllegalStateException("Transition not allowed: "+order.status+" -> "+target);
  order.status=target; order.updatedAt=Instant.now();
  outbox.save(new OutboxEvent(order.id,"Order"+target,"{\"orderId\":\""+order.id+"\",\"status\":\""+target+"\"}",order.updatedAt));
  return OrderView.of(order,lines.findByOrderId(order.id));
 }
 @Transactional(readOnly=true)
 public OrderView get(UUID id){var o=orders.findById(id).orElseThrow(()->new EntityNotFoundException("Order not found"));return OrderView.of(o,lines.findByOrderId(id));}
 private String hash(CreateOrderRequest request){
  try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(mapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)));}
  catch(Exception e){throw new IllegalStateException("Unable to fingerprint request",e);}
 }
}


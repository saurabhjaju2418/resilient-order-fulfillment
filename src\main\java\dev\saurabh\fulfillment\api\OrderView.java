package dev.saurabh.fulfillment.api;
import dev.saurabh.fulfillment.domain.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
public record OrderView(UUID id,String externalRef,String customerRef,OrderStatus status,long version,Instant createdAt,List<LineView> lines){
 public record LineView(String sku,int quantity,int reservedQuantity){}
 public static OrderView of(FulfillmentOrder o,List<OrderLine> lines){return new OrderView(o.id,o.externalRef,o.customerRef,o.status,o.version,o.createdAt,lines.stream().map(l->new LineView(l.sku,l.quantity,l.reservedQuantity)).toList());}
}


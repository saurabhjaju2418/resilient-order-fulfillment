package dev.saurabh.fulfillment.api;
import dev.saurabh.fulfillment.domain.OrderStatus;
import dev.saurabh.fulfillment.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequestMapping("/api/orders")
public class OrderController{
 private final OrderService service;
 public OrderController(OrderService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public OrderView create(@Valid @RequestBody CreateOrderRequest request,@RequestHeader(value="Idempotency-Key",required=false) String key){return service.create(request,key);}
 @GetMapping("/{id}") public OrderView get(@PathVariable UUID id){return service.get(id);}
 @PostMapping("/{id}/transitions") public OrderView transition(@PathVariable UUID id,@RequestBody TransitionRequest req){return service.transition(id,req.status());}
 public record TransitionRequest(OrderStatus status){}
}


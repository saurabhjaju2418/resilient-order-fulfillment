package dev.saurabh.fulfillment.api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
public record CreateOrderRequest(@NotBlank @Size(max=100) String externalRef,@NotBlank @Size(max=100) String customerRef,
 @NotEmpty @Size(max=100) List<@Valid Line> lines) {
 public record Line(@NotBlank @Size(max=80) String sku,@Min(1) @Max(10000) int quantity){}
}


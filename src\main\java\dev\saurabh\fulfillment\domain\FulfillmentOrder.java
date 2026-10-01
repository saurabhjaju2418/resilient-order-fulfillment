package dev.saurabh.fulfillment.domain;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="fulfillment_orders")
public class FulfillmentOrder {
 @Id public UUID id;
 @Column(name="external_ref",nullable=false,length=100) public String externalRef;
 @Column(name="customer_ref",nullable=false,length=100) public String customerRef;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=16) public OrderStatus status;
 @Version public long version;
 @Column(name="created_at",nullable=false) public Instant createdAt;
 @Column(name="updated_at",nullable=false) public Instant updatedAt;
 protected FulfillmentOrder(){}
 public FulfillmentOrder(String externalRef,String customerRef,Instant now){this.id=UUID.randomUUID();this.externalRef=externalRef;this.customerRef=customerRef;this.status=OrderStatus.RECEIVED;this.createdAt=now;this.updatedAt=now;}
}


package dev.saurabh.fulfillment.domain;
import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="order_lines")
public class OrderLine {
 @Id public UUID id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="order_id",nullable=false) public FulfillmentOrder order;
 @Column(name="sku",nullable=false,length=80) public String sku;
 @Column(nullable=false) public int quantity;
 @Column(name="reserved_quantity",nullable=false) public int reservedQuantity;
 protected OrderLine(){}
 public OrderLine(FulfillmentOrder order,String sku,int quantity){this.id=UUID.randomUUID();this.order=order;this.sku=sku;this.quantity=quantity;this.reservedQuantity=0;}
}


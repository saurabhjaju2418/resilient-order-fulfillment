package dev.saurabh.fulfillment.domain;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="idempotency_records")
public class IdempotencyRecord {
 @Id public String idempotencyKey;
 @Column(name="request_hash",nullable=false,length=64) public String requestHash;
 @Column(name="response_order_id",nullable=false) public UUID responseOrderId;
 @Column(name="created_at",nullable=false) public Instant createdAt;
 protected IdempotencyRecord(){}
 public IdempotencyRecord(String key,String hash,UUID orderId,Instant now){this.idempotencyKey=key;this.requestHash=hash;this.responseOrderId=orderId;this.createdAt=now;}
}


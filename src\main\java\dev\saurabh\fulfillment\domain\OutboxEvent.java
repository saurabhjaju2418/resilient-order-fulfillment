package dev.saurabh.fulfillment.domain;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="outbox_events",indexes=@Index(name="idx_outbox_pending",columnList="published_at,created_at"))
public class OutboxEvent {
 @Id public UUID id;
 @Column(name="aggregate_id",nullable=false) public UUID aggregateId;
 @Column(name="event_type",nullable=false,length=80) public String eventType;
 @Column(nullable=false,columnDefinition="jsonb") public String payload;
 @Column(name="created_at",nullable=false) public Instant createdAt;
 @Column(name="published_at") public Instant publishedAt;
 protected OutboxEvent(){}
 public OutboxEvent(UUID aggregateId,String eventType,String payload,Instant now){this.id=UUID.randomUUID();this.aggregateId=aggregateId;this.eventType=eventType;this.payload=payload;this.createdAt=now;}
}


<div align="center">

<img src="assets/project-banner.svg" alt="Animated resilient order fulfillment flow" width="900" />

# Resilient Order Fulfillment

**Make order transitions explicit, retryable, and auditable.**

Java 21 · Spring Boot · PostgreSQL · Flyway · Docker

</div>

A small order lifecycle API demonstrating guarded workflow transitions, idempotent order creation, optimistic concurrency metadata, and transactional outbox records.

## Implemented

- POST /api/orders creates an order and lines. Require an Idempotency-Key; replaying an identical request returns the original order, while reusing that key for another payload conflicts.
- GET /api/orders/{id} returns the order and its lines.
- POST /api/orders/{id}/transitions validates the lifecycle graph and records a corresponding outbox event in the same database transaction.
- PostgreSQL/Flyway schema, input validation, health/metrics endpoints, Docker Compose.

Allowed flow:

```text
RECEIVED -> RESERVED -> PICKING -> SHIPPED -> DELIVERED
    |           |          |
    +-----------+----------+----> CANCELLED
```

## Run

```bash
docker compose up --build
```

Example:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' -H 'Idempotency-Key: demo-order-001' \
  -d '{"externalRef":"WEB-1001","customerRef":"customer-demo","lines":[{"sku":"SKU-RED-01","quantity":2}]}'
```

## Reliability choices

The idempotency key, request hash, order, line items, and initial outbox record commit in one transaction. Status changes are checked against an explicit state graph and `@Version` tracks concurrent writes. Outbox events are durable records that a future publisher can deliver at least once; this starter does not yet include a dispatcher, broker, inventory reservation adapter, or dead-letter handling.

## Boundaries

This portfolio slice is a single service with demo-level customer references. It does not implement authentication, tenant isolation, payment, inventory accounting, shipping providers, webhook signing, retries, or reconciliation. Add those adapters behind the order workflow before treating it as production software.

## License

MIT. See LICENSE.


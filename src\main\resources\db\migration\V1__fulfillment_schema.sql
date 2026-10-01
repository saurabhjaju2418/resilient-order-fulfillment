create table fulfillment_orders (
 id uuid primary key, external_ref varchar(100) not null, customer_ref varchar(100) not null,
 status varchar(16) not null check(status in ('RECEIVED','RESERVED','PICKING','SHIPPED','DELIVERED','CANCELLED')),
 version bigint not null default 0, created_at timestamptz not null, updated_at timestamptz not null
);
create unique index uq_order_external_ref on fulfillment_orders(external_ref);
create table order_lines (
 id uuid primary key, order_id uuid not null references fulfillment_orders(id) on delete cascade,
 sku varchar(80) not null, quantity integer not null check(quantity > 0), reserved_quantity integer not null default 0 check(reserved_quantity >= 0)
);
create index idx_order_lines_order on order_lines(order_id);
create table idempotency_records (
 idempotency_key varchar(128) primary key, request_hash varchar(64) not null,
 response_order_id uuid not null references fulfillment_orders(id), created_at timestamptz not null
);
create table outbox_events (
 id uuid primary key, aggregate_id uuid not null, event_type varchar(80) not null,
 payload jsonb not null, created_at timestamptz not null, published_at timestamptz
);
create index idx_outbox_pending on outbox_events(published_at,created_at);


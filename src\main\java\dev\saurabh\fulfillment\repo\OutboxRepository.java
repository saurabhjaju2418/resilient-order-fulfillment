package dev.saurabh.fulfillment.repo;
import dev.saurabh.fulfillment.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface OutboxRepository extends JpaRepository<OutboxEvent,UUID>{}


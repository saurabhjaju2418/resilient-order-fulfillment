package dev.saurabh.fulfillment.repo;
import dev.saurabh.fulfillment.domain.FulfillmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface OrderRepository extends JpaRepository<FulfillmentOrder,UUID>{}


package dev.saurabh.fulfillment.repo;
import dev.saurabh.fulfillment.domain.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord,String>{}


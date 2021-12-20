package me.boukadi.eventsourcingcqrstraining.query.repo;

import me.boukadi.eventsourcingcqrstraining.query.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepo extends JpaRepository<Operation, Long> {
}

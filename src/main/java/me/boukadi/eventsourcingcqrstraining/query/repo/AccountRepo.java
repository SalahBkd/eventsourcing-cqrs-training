package me.boukadi.eventsourcingcqrstraining.query.repo;

import me.boukadi.eventsourcingcqrstraining.query.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, String> {
}

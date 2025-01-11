package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

package com.example.oauth2.repositories;

import com.example.oauth2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);
}

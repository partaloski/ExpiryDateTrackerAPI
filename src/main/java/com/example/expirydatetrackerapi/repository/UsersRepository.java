package com.example.expirydatetrackerapi.repository;

import com.example.expirydatetrackerapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsernameAndPassword(String username, String password);
}

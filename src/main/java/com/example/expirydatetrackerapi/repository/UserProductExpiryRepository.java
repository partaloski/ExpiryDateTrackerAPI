package com.example.expirydatetrackerapi.repository;

import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.relations.UserProductsExpiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductExpiryRepository extends JpaRepository<UserProductsExpiry, Integer> {
    List<UserProductsExpiry> findAllByUser(User user);
}

package com.example.expirydatetrackerapi.repository;

import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import com.example.expirydatetrackerapi.models.relations.UserProductsWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductWishlistRepository extends JpaRepository<UserProductsWishlist, UserProductsWishlistPK> {
    List<UserProductsWishlist> findAllByUser(User user);
}

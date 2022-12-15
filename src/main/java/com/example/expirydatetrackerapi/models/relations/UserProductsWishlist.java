package com.example.expirydatetrackerapi.models.relations;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import com.example.expirydatetrackerapi.models.primarykeys.UserProductsWishlistPK;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "users_products_wishlists")
public class UserProductsWishlist {
    @EmbeddedId
    private UserProductsWishlistPK id;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name="username")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name="productid")
    private Product product;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    public UserProductsWishlist(User user, Product product, Integer quantity) {
        this.id = new UserProductsWishlistPK(user.getUsername(), product.getProductId());
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public UserProductsWishlist() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProductsWishlist that = (UserProductsWishlist) o;
        return Objects.equals(id, that.id);
    }
}

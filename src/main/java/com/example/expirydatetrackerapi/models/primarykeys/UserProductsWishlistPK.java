package com.example.expirydatetrackerapi.models.primarykeys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Data
public class UserProductsWishlistPK implements Serializable {
    @Column(name="username")
    private String username;

    @Column(name="productid")
    private Integer productId;

    public UserProductsWishlistPK(String username, Integer productId) {
        this.username = username;
        this.productId = productId;
    }

    public UserProductsWishlistPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProductsWishlistPK that = (UserProductsWishlistPK) o;
        return Objects.equals(username, that.username) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, productId);
    }
}

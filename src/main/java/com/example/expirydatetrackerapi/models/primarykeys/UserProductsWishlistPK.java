package com.example.expirydatetrackerapi.models.primarykeys;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
public class UserProductsWishlistPK implements Serializable {
    @Column(name="username")
    private String username;

    @Column(name="productid")
    private String productId;

    public UserProductsWishlistPK(String username, String productId) {
        this.username = username;
        this.productId = productId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProductsWishlistPK that = (UserProductsWishlistPK) o;
        return Objects.equals(username, that.username) && Objects.equals(productId, that.productId);
    }
}

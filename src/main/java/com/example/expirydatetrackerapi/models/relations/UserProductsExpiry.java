package com.example.expirydatetrackerapi.models.relations;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.User;
import lombok.Data;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@Table(name = "users_products_expiry")
public class UserProductsExpiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer ID;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

    @ManyToOne
    @JoinColumn(name="productid")
    private Product product;

    @Basic
    @Column(name = "expirydate")
    private LocalDate expirydate;

    public UserProductsExpiry() {
    }

    public UserProductsExpiry(Integer ID, User user, Product product, LocalDate expirydate) {
        this.ID = ID;
        this.user = user;
        this.product = product;
        this.expirydate = expirydate;
    }

    public UserProductsExpiry(User user, Product product, LocalDate expirydate) {
        this.user = user;
        this.product = product;
        this.expirydate = expirydate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProductsExpiry that = (UserProductsExpiry) o;
        return Objects.equals(ID, that.ID) && Objects.equals(user, that.user) && Objects.equals(product, that.product) && Objects.equals(expirydate, that.expirydate);
    }
}

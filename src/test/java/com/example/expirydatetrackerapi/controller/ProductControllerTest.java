package com.example.expirydatetrackerapi.controller;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.dto.ProductsDTO;
import com.example.expirydatetrackerapi.service.ManufacturerService;
import com.example.expirydatetrackerapi.service.ProductService;
import com.example.expirydatetrackerapi.web.ManufacturerController;
import com.example.expirydatetrackerapi.web.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService service;
    private ProductController controller;

    @BeforeEach
    void setUp(){
        controller = new ProductController(service);
    }

    @Test
    void shouldFindAll(){
        //given
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        Product product1 = new Product("1", "Product 1", manufacturer);
        Product product2 = new Product("2", "Product 2", manufacturer);
        Product product3 = new Product("3", "Product 3", manufacturer);
        List<Product> productList = List.of(product1, product2, product3);
        given(service.findAll()).willReturn(productList);
        //when
        ProductsDTO response = controller.getAll();
        //then
        assertThat(response.getProducts().size()).isEqualTo(productList.size());
    }

    @Test
    void shouldFindOne(){
        //given
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productID = "1";
        String productName = "Product";
        Product product = new Product(productID, productName, manufacturer);
        given(service.getProduct(productID)).willReturn(product);
        //when
        ResponseEntity<Product> productResponse = controller.getProduct(productID);
        //then
        assertThat(productResponse.getBody().getProductId()).isEqualTo(productID);
        assertThat(productResponse.getBody().getName()).isEqualTo(productName);
    }

    @Test
    void shouldAddOne(){
        //given
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productID = "1";
        String productName = "Product";
        Product product = new Product(productID, productName, manufacturer);
        given(service.addProduct(productID, productName, manufacturerId)).willReturn(product);
        //when
        ResponseEntity<Product> productResponse = controller.saveProduct(productID, productName, manufacturerId);
        //then
        assertThat(productResponse.getBody().getProductId()).isEqualTo(productID);
        assertThat(productResponse.getBody().getName()).isEqualTo(productName);
    }

    @Test
    void shouldDeleteOne(){
        //given
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        String productID = "1";
        String productName = "Product";
        Product product = new Product(productID, productName, manufacturer);
        //when
        ResponseEntity<String> response = controller.deleteProduct("1");
        //then
        assertThat(response.getBody()).isEqualTo("Product deleted (or never existed)");
    }
}

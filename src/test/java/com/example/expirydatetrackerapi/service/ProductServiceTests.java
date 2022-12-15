package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.exceptions.ManufacturerDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.NotValidException;
import com.example.expirydatetrackerapi.models.exceptions.ProductDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.ProductWithIdAlreadyExistsException;
import com.example.expirydatetrackerapi.repository.ManufacturerRepository;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private ManufacturerRepository manufacturerRepository;

    @BeforeEach
    void setUp(){
        service = new ProductServiceImpl(repository, manufacturerRepository);
    }

    @Test
    void shouldFindAll(){
        //when
        service.findAll();
        //then
        verify(repository).findAll();
    }

    @Test
    void shouldAddProduct(){
        //given
        String id ="1234";
        String name ="Product";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Manufacturer");
        Product product = new Product(id, name, manufacturer);

        //when
        given(manufacturerRepository.findById(manufacturerId)).willReturn(Optional.of(manufacturer));
        given(repository.findById(id)).willReturn(Optional.empty());
        service.addProduct(id, name, manufacturerId);

        //then
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(productCaptor.capture());
        Product productAdded = productCaptor.getValue();
        assertThat(productAdded.getName()).isEqualTo(name);
        assertThat(productAdded.getProductId()).isEqualTo(id);
        assertThat(productAdded.getManufacturer().getId()).isEqualTo(manufacturer.getId());
        assertThat(productAdded.getManufacturer().getName()).isEqualTo(manufacturer.getName());
    }

    @Test
    void shouldNotAddProductEmptyId(){
        //given
        String id ="";
        String name ="Product";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Manufacturer");

        //then
        assertThatThrownBy(() -> service.addProduct(id, name, manufacturerId))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Barcode cannot be empty.\"");
    }

    @Test
    void shouldNotAddProductNullId(){
        //given
        String id =null;
        String name ="Product";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Manufacturer");

        //then
        assertThatThrownBy(() -> service.addProduct(id, name, manufacturerId))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Barcode cannot be empty.\"");
    }

    @Test
    void shouldNotAddProductEmptyName(){
        //given
        String id ="1234";
        String name ="";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Manufacturer");

        //then
        assertThatThrownBy(() -> service.addProduct(id, name, manufacturerId))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Name cannot be empty.\"");
    }

    @Test
    void shouldNotAddProductNullName(){
        //given
        String id = "1234";
        String name = null;
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Manufacturer");

        //then
        assertThatThrownBy(() -> service.addProduct(id, name, manufacturerId))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Name cannot be empty.\"");
    }

    @Test
    void shouldNotAddProductManufacturerNotExist(){
        //given
        String id = "1234";
        String name = "Product";
        Integer manufacturerId=1;

        //when
        given(manufacturerRepository.findById(manufacturerId)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.addProduct(id, name, manufacturerId))
                .isInstanceOf(ManufacturerDoesNotExistException.class)
                .hasMessage("Manufacturer with ID #"+manufacturerId+ " does not exist.");
    }

    @Test
    void shouldNotAddProductBarcodeAlreadyRegistered(){
        //given
        String id = "1234";
        String name = "Product";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, name);
        Product product = new Product(id, name, manufacturer);

        //when
        given(manufacturerRepository.findById(manufacturerId)).willReturn(Optional.of(manufacturer));
        given(repository.findById(id)).willReturn(Optional.of(product));

        //then
        assertThatThrownBy(() -> service.addProduct(id, name, manufacturerId))
                .isInstanceOf(ProductWithIdAlreadyExistsException.class)
                .hasMessage("The product with id #"+id+" already exists.");
    }

    @Test
    void shouldDeleteProduct(){
        //given
        String id = "1234";
        String name = "Product";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, name);
        Product product = new Product(id, name, manufacturer);
        //when
        given(repository.findById(id)).willReturn(Optional.of(product));
        //then
        assertDoesNotThrow(() -> service.deleteProduct(id));
    }

    @Test
    void shouldNotDeleteProductEmptyId(){
        //given
        String id = "";
        //then
        assertThatThrownBy(() -> service.deleteProduct(id))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Barcode cannot be empty.\"");
    }

    @Test
    void shouldNotDeleteProductNullId(){
        //given
        String id = null;
        //then
        assertThatThrownBy(() -> service.deleteProduct(id))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Barcode cannot be empty.\"");
    }

    @Test
    void shouldNotDeleteProductNotFound(){
        //given
        String id = "123";
        //when
        given(repository.findById(id)).willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.deleteProduct(id))
                .isInstanceOf(ProductDoesNotExistException.class)
                .hasMessage("The product with barcode (id) [" + id + "] does not exist.");
    }

    @Test
    void shouldFindProduct(){
        //given
        String id = "1234";
        String name = "Product";
        Integer manufacturerId=1;
        Manufacturer manufacturer = new Manufacturer(manufacturerId, name);
        Product product = new Product(id, name, manufacturer);
        //when
        given(repository.findById(id)).willReturn(Optional.of(product));
        //then
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        Product productFound = service.getProduct(id);
        assertThat(productFound.getProductId()).isEqualTo(id);
        assertThat(productFound.getName()).isEqualTo(name);
        assertThat(productFound.getManufacturer().getId()).isEqualTo(manufacturer.getId());
        assertThat(productFound.getManufacturer().getName()).isEqualTo(manufacturer.getName());
    }

    @Test
    void shouldNotFindProductEmptyId(){
        //given
        String id = "";
        //then
        assertThatThrownBy(() -> service.getProduct(id))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Barcode cannot be empty.\"");
    }

    @Test
    void shouldNotFindProductNullId(){
        //given
        String id = null;
        //then
        assertThatThrownBy(() -> service.getProduct(id))
                .isInstanceOf(NotValidException.class)
                .hasMessage("400 BAD_REQUEST \"Product's Barcode cannot be empty.\"");
    }

    @Test
    void shouldNotFindProductDoesNotExist(){
        //given
        String id = "1234";
        //when
        given(repository.findById(id)).willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.getProduct(id))
                .isInstanceOf(ProductDoesNotExistException.class)
                .hasMessage("The product with barcode (id) [" + id + "] does not exist.");
    }
}

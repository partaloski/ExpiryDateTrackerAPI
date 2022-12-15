package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.models.exceptions.ManufacturerDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.NotValidException;
import com.example.expirydatetrackerapi.models.exceptions.ProductDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.ProductWithIdAlreadyExistsException;
import com.example.expirydatetrackerapi.repository.ManufacturerRepository;
import com.example.expirydatetrackerapi.repository.ProductRepository;
import com.example.expirydatetrackerapi.service.ProductService;
import com.example.expirydatetrackerapi.utils.RedisUtility;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final RedisUtility redisUtility;
    private final Gson gson;
    private final String REDIS_KEY = "products";

    @Override
    public List<Product> findAll() {
        String productsJSON = redisUtility.getValue(REDIS_KEY);

        List<Product> products;

        if(isNull(productsJSON)){
            products = productRepository.findAll();
            redisUtility.setValue(REDIS_KEY, products);
        }
        else{
            products = gson.fromJson(productsJSON, List.class);
        }

        return products;
    }

    @Override
    public Product addProduct(String id, String name, Integer manufacturer_id) {
        if(isNull(id) || id.isEmpty()){
            throw new NotValidException("Product's Barcode cannot be empty.");
        }

        if(isNull(name) || name.isEmpty()){
            throw new NotValidException("Product's Name cannot be empty.");
        }

        Manufacturer manufacturer = manufacturerRepository.findById(manufacturer_id).orElseThrow(() -> new ManufacturerDoesNotExistException(manufacturer_id));
        Product product = new Product(id, name, manufacturer);

        if(productRepository.findById(id).isPresent()){
            throw new ProductWithIdAlreadyExistsException(id);
        }

        product = productRepository.save(product);

        invalidateCache();

        return product;
    }
    @Override
    public void deleteProduct(String id) {
        if(isNull(id) || id.isEmpty()){
            throw new NotValidException("Product's Barcode cannot be empty.");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistException(id));

        productRepository.delete(product);

        invalidateCache();
    }

    @Override
    public Product getProduct(String id) {
        if(isNull(id) || id.isEmpty()){
            throw new NotValidException("Product's Barcode cannot be empty.");
        }

        return productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistException(id));
    }

    private void invalidateCache(){
        redisUtility.clearValue(REDIS_KEY);
    }
}

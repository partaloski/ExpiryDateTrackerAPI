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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;

    public ProductServiceImpl(ProductRepository productRepository, ManufacturerRepository manufacturerRepository) {
        this.productRepository = productRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(String id, String name, Integer manufacturer_id) {
        if(isNull(id) || id.isEmpty())
            throw new NotValidException("Product's Barcode cannot be empty.");

        if(isNull(name) || name.isEmpty())
            throw new NotValidException("Product's Name cannot be empty.");

        Manufacturer manufacturer = manufacturerRepository.findById(manufacturer_id).orElseThrow(() -> new ManufacturerDoesNotExistException(manufacturer_id));
        Product product = new Product(id, name, manufacturer);
        if(productRepository.findById(id).isPresent())
            throw new ProductWithIdAlreadyExistsException(id);
        return productRepository.save(product);
    }
    @Override
    public void deleteProduct(String id) {
        if(isNull(id) || id.isEmpty())
            throw new NotValidException("Product's Barcode cannot be empty.");

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistException(id));
        productRepository.delete(product);
    }

    @Override
    public Product getProduct(String id) {
        if(isNull(id) || id.isEmpty())
            throw new NotValidException("Product's Barcode cannot be empty.");

        return productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistException(id));
    }
}

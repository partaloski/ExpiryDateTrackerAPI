package com.example.expirydatetrackerapi.web.controller.rest;

import com.example.expirydatetrackerapi.models.Product;
import com.example.expirydatetrackerapi.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<Product> getAll(){
        return productService.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id){
        Product p = productService.getProduct(id);
        if (p != null){
            return ResponseEntity.ok().body(p);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Product> saveProduct(@RequestParam Integer id, @RequestParam String name, @RequestParam Integer manufacturer_id){
        return ResponseEntity.ok().body(productService.addProduct(id, name, manufacturer_id));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().body("Product deleted (or never existed)");
    }
}

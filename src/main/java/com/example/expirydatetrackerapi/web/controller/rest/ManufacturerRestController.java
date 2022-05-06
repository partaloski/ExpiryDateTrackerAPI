package com.example.expirydatetrackerapi.web.controller.rest;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.exceptions.ManufacturerDoesNotExistException;
import com.example.expirydatetrackerapi.service.ManufacturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerRestController {

    private final ManufacturerService manufacturerService;

    public ManufacturerRestController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping()
    public List<Manufacturer> findAll(){
        return manufacturerService.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Manufacturer> addManufacturer(@RequestParam String name){
        Manufacturer manufacturer = manufacturerService.save(name);
        return ResponseEntity.ok().body(manufacturer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteManufacturer(@PathVariable Integer id){
        manufacturerService.delete(id);
        return ResponseEntity.ok().body("Manufacturer deleted (or never existed)");
    }
}

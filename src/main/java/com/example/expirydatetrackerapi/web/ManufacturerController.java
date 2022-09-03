package com.example.expirydatetrackerapi.web;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.dto.ManufacturersDTO;
import com.example.expirydatetrackerapi.service.ManufacturerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping()
    public ManufacturersDTO findAll(){
        return ManufacturersDTO.createOf(manufacturerService.findAll());
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

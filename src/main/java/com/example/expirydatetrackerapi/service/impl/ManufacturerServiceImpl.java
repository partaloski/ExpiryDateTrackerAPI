package com.example.expirydatetrackerapi.service.impl;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.exceptions.ManufacturerDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.NotValidException;
import com.example.expirydatetrackerapi.repository.ManufacturerRepository;
import com.example.expirydatetrackerapi.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository repository;

    public ManufacturerServiceImpl(ManufacturerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Manufacturer> findAll() {
        return repository.findAll();
    }

    @Override
    public Manufacturer save(String name) {
        if(isNull(name) || name.isEmpty())
            throw new NotValidException("Manufacturer's name cannot be empty.");
        Manufacturer manufacturer = new Manufacturer(name);
        return repository.save(manufacturer);
    }

    @Override
    public void delete(Integer id) {
        if(isNull(id) || id < 0)
            throw new NotValidException("ID cannot be null or smaller than 0.");
        Manufacturer manufacturer = repository.findById(id).orElseThrow(() -> new ManufacturerDoesNotExistException(id));
        repository.delete(manufacturer);
    }
}

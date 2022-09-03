package com.example.expirydatetrackerapi.controller;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.dto.ManufacturersDTO;
import com.example.expirydatetrackerapi.repository.ManufacturerRepository;
import com.example.expirydatetrackerapi.service.ManufacturerService;
import com.example.expirydatetrackerapi.service.impl.ManufacturerServiceImpl;
import com.example.expirydatetrackerapi.web.ManufacturerController;
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
public class ManufacturerControllerTest {
    @Mock
    private ManufacturerService service;
    private ManufacturerController controller;

    @BeforeEach
    public void setUp(){
        controller = new ManufacturerController(service);
    }

    @Test
    void shouldFindAll(){
        //given
        Integer manufacturerID1 = 1;
        String manufacturerName1 = "Manufacturer 1";
        Integer manufacturerID2 = 2;
        String manufacturerName2 = "Manufacturer 2";
        Integer manufacturerID3 = 3;
        String manufacturerName3 = "Manufacturer 3";
        Manufacturer manufacturer1 = new Manufacturer(manufacturerID1, manufacturerName1);
        Manufacturer manufacturer2 = new Manufacturer(manufacturerID2, manufacturerName2);
        Manufacturer manufacturer3 = new Manufacturer(manufacturerID3, manufacturerName3);
        List<Manufacturer> manufacturers = List.of(manufacturer1, manufacturer2, manufacturer3);
        given(service.findAll()).willReturn(manufacturers);
        //when
        ManufacturersDTO dto = controller.findAll();
        //then
        assertThat(dto.getManufacturers().size()).isEqualTo(manufacturers.size());
    }

    @Test
    void shouldAdd(){
        //given
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        given(service.save(manufacturerName)).willReturn(manufacturer);
        //when
        ResponseEntity<Manufacturer> response = controller.addManufacturer(manufacturerName);
        //then
        assertThat(response.getBody().getId()).isEqualTo(manufacturerId);
        assertThat(response.getBody().getName()).isEqualTo(manufacturerName);
    }

    @Test
    void shouldDelete(){
        //given
        Integer manufacturerId = 1;
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerId, manufacturerName);
        //when
        ResponseEntity<String> response = controller.deleteManufacturer(1);
        //then
        assertThat(response.getBody()).isEqualTo("Manufacturer deleted (or never existed)");
    }
}

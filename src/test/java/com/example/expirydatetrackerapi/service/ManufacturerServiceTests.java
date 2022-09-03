package com.example.expirydatetrackerapi.service;

import com.example.expirydatetrackerapi.models.Manufacturer;
import com.example.expirydatetrackerapi.models.exceptions.ManufacturerDoesNotExistException;
import com.example.expirydatetrackerapi.models.exceptions.NotValidException;
import com.example.expirydatetrackerapi.repository.ManufacturerRepository;
import com.example.expirydatetrackerapi.service.impl.ManufacturerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceTests {
    @Mock
    private ManufacturerRepository repository;
    private ManufacturerService service;

    @BeforeEach
    public void setUp(){
        service = new ManufacturerServiceImpl(repository);
    }

    @Test
    void shouldFindAll(){
        //when
        service.findAll();
        //then
        verify(repository).findAll();
    }

    @Test
    void shouldAddManufacturer(){
        //given
        String manufacturerName = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(manufacturerName);
        //when
        when(repository.save(Mockito.any())).thenReturn(manufacturer);
        //then
        assertThat(service.save(manufacturerName).getName()).isEqualTo(manufacturerName);
    }

    @Test
    void shouldNotAddManufacturerEmpty(){
        //given
        String manufacturerName = "";
        //then
        assertThatThrownBy(() -> service.save(manufacturerName)).isInstanceOf(NotValidException.class);
    }

    @Test
    void shouldNotAddManufacturerNull(){
        //given
        String manufacturerName = null;
        //then
        assertThatThrownBy(() -> service.save(manufacturerName)).isInstanceOf(NotValidException.class);
    }

    @Test
    void shouldDeleteManufacturer(){
        //given
        Integer id = 1;
        String name = "Manufacturer";
        Manufacturer manufacturer = new Manufacturer(id, name);
        //when
        when(repository.findById(id)).thenReturn(Optional.of(manufacturer));
        //then
        assertDoesNotThrow(() -> service.delete(id));
    }

    @Test
    void shouldThrowExceptionAndNotDeleteManufacturerNotFound(){
        //given
        Integer id = 1;
        //when
        when(repository.findById(id)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(ManufacturerDoesNotExistException.class);
    }

    @Test
    void shouldThrowExceptionAndNotDeleteManufacturerLessThanZero(){
        //given
        Integer id = -1;
        //then
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(NotValidException.class);
    }

    @Test
    void shouldThrowExceptionAndNotDeleteManufacturerNull(){
        //given
        Integer id = -1;
        //then
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(NotValidException.class);
    }


}

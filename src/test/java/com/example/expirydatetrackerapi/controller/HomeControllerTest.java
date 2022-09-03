package com.example.expirydatetrackerapi.controller;

import com.example.expirydatetrackerapi.web.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {
    private HomeController controller;

    @BeforeEach
    void setUp(){
        controller = new HomeController();
    }

    @Test
    void shouldGetEN(){
        //given
        String lang = "EN";
        //when
        String response = controller.getHomePage(lang);
        //then
        assertThat(response).isEqualTo("home-page-en");
    }

    @Test
    void shouldGetENFromNull(){
        //given
        String lang = null;
        //when
        String response = controller.getHomePage(lang);
        //then
        assertThat(response).isEqualTo("home-page-en");
    }

    @Test
    void shouldGetMK(){
        //given
        String lang = "MK";
        //when
        String response = controller.getHomePage(lang);
        //then
        assertThat(response).isEqualTo("home-page-mk");
    }
}

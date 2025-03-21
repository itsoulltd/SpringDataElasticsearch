package com.infoworks.lab.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoworks.lab.webapp.WebApplicationTest;
import com.infoworks.lab.webapp.config.BeanConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {WebApplicationTest.class, BeanConfig.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application.properties"
        , "classpath:application-test.properties"
        , "classpath:application-h2db.properties"})
public class UserControllerUnitTest {

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @InjectMocks
    UserController controller;

    @Test
    public void loadTest() {
        System.out.println("Object Loaded: " + (controller != null ? "Yes" : "No"));
    }

    @Test
    public void rowCountGetTest() throws Exception {
        //Call controller to make the save:
        MvcResult result = mockMvc.perform(get("/user/rowCount")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andDo(print())
                //.andExpect(status().isOk())
                .andReturn();
        String str = "Status: " + result.getResponse().getStatus();
        System.out.println(str);
        //
    }

}

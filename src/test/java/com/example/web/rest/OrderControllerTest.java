package com.example.web.rest;

import com.example.Warsztaty4Application;
import com.example.domain.Order;
import com.example.repository.OrderRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by marek.papis on 2016-06-08.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Warsztaty4Application.class)
@WebIntegrationTest // WebApplicationContext should be loaded for the test., embedded tomcat container should be started.
//@WebAppConfiguration // for mocking mvc without tomcat container

public class OrderControllerTest {

    private static final String ORDER_DESCRIPTION = "Order description";

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        //simulates the whole web application context
        //we could also use MockMvcBuilders.standaloneSetup to narrow down the controllers to be setup
        //to the ones only that we need to test
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //new order created
        order = new Order(ORDER_DESCRIPTION);
    }

    @Test
    public void getOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void addOrder() throws Exception {
        //post an object
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(order)))
                .andDo(print())
                .andExpect(status().isCreated());

        //Test in the DB if record exists
        List<Order> orders = orderRepository.findAll();

        // we use java 8 stream api
        assertThat(orders
                .stream()
                .peek(System.out::println)
                .filter(o -> o.getOrderName().equals(ORDER_DESCRIPTION)))
                .hasSize(1);
    }

}
package com.ecommerce.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.order.adapter.in.observability.CorrelationIdFilter;
import com.ecommerce.order.application.OrderServiceApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = OrderServiceApplication.class)
@AutoConfigureMockMvc
class SecurityIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn401WhenTokenIsMissing() throws Exception {
        mockMvc.perform(get("/api/orders/1")
            .header(CorrelationIdFilter.CORRELATION_ID_HEADER, "sec-corr-401"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.correlationId").value("sec-corr-401"));
    }

    @Test
    void shouldReturn401WhenTokenIsInvalid() throws Exception {
        mockMvc.perform(get("/api/orders/1")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnTokenWhenLoginCredentialsAreValid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void shouldReturn401WhenLoginCredentialsAreInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenRoleIsNotAllowedForEndpoint() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"customer\",\"password\":\"customer123\"}"))
                .andReturn()
            ;

        String responseJson = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseJson).get("token").asText();

        mockMvc.perform(post("/api/orders/1/ship")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"trackingNumber\":\"TRK-123\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void actuatorInfoShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isUnauthorized());
    }
}

package com.ecommerce.order.adapter.in.security;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Map<String, DemoUser> USERS = Map.of(
            "admin", new DemoUser("admin", "admin123", List.of("ADMIN")),
            "sales", new DemoUser("sales", "sales123", List.of("SALES")),
            "logistics", new DemoUser("logistics", "logistics123", List.of("LOGISTICS")),
            "customer", new DemoUser("customer", "customer123", List.of("CUSTOMER")));

    public Optional<DemoUser> authenticate(String username, String password) {
        DemoUser user = USERS.get(username);
        if (user == null) {
            return Optional.empty();
        }
        if (!user.password().equals(password)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public record DemoUser(String username, String password, List<String> roles) {
    }
}

package playground.security_demo.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import playground.security_demo.domain.Order;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final List<Order> orders;

    public OrderService() {
        // A mutable list of orders
        this.orders = new ArrayList<>(List.of(
                new Order("1", "Kibbles", "Luna"),
                new Order("2", "Chicken", "Skipper"),
                new Order("3", "Rice", "Luna"),
                new Order("4", "Lamb", "Skipper"),
                new Order("5", "Bone", "Luna"),
                new Order("6", "Toys", "Luna"),
                new Order("7", "Toys", "Skipper")
        ));
    }

    public Mono<List<Order>> getOrdersForCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Principal principal = securityContext.getAuthentication();
                    return orders.stream()
                            .filter(order -> order.owner().equals(principal.getName()))
                            .toList();
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Boolean> deleteOrder(String orderId) {
        return Mono.just(orders.removeIf(order -> order.id().equals(orderId)));
    }
}

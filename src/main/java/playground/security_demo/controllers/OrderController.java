package playground.security_demo.controllers;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import playground.security_demo.domain.Order;
import playground.security_demo.service.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
public record OrderController(OrderService orderService) {

    @QueryMapping
    List<Order> myOrders(Principal principal) {
        return orderService.getOrdersByOwner(principal.getName());
    }
}

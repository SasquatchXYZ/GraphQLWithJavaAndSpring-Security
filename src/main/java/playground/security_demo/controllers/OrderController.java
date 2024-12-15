package playground.security_demo.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import playground.security_demo.domain.DeleteOrderInput;
import playground.security_demo.domain.DeleteOrderPayload;
import playground.security_demo.domain.Order;
import playground.security_demo.service.OrderService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Controller
public record OrderController(OrderService orderService) {

    @QueryMapping
    List<Order> myOrders(Principal principal) {
        return orderService.getOrdersByOwner(principal.getName());
    }

    @MutationMapping
    DeleteOrderPayload deleteOrder(@Argument DeleteOrderInput input, Principal principal) throws AccessDeniedException {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        if (!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only Admins can delete orders");
        }
        return new DeleteOrderPayload(orderService.deleteOrder(input.orderId()));
    }
}

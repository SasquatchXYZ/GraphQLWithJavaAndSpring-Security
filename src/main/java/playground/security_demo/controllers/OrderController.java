package playground.security_demo.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import playground.security_demo.domain.DeleteOrderInput;
import playground.security_demo.domain.DeleteOrderPayload;
import playground.security_demo.domain.Order;
import playground.security_demo.service.OrderService;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public record OrderController(OrderService orderService) {

    @QueryMapping
    Mono<List<Order>> myOrders() {
        return orderService.getOrdersForCurrentUser();
    }

    @MutationMapping
    Mono<DeleteOrderPayload> deleteOrder(
            @Argument DeleteOrderInput input
    ) {
        Mono<Boolean> success = orderService.deleteOrder(input.orderId());
        return success.map(DeleteOrderPayload::new);
    }
}

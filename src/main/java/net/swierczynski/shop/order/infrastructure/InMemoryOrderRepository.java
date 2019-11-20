package net.swierczynski.shop.order.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swierczynski.shop.order.domain.Order;
import net.swierczynski.shop.order.domain.OrderId;
import net.swierczynski.shop.order.domain.OrderRepository;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> orderMap = new HashMap<>();
    private final ObjectMapper objectMapper;

    public InMemoryOrderRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Order place(Order order) {
        orderMap.put((order.id()), order);
        return order;
    }

    @Override
    public List<OrderDTO> listInRange(Clock from, Clock to) throws JsonProcessingException {
        List<OrderDTO> orders = new ArrayList<>();
        for (Order order : orderMap.values()) {
            if (order.wasPlacedBetween(from, to)) {
                OrderDTO orderDTO = OrderDTO.fromSnapshot(order.toSnapshot(objectMapper), objectMapper);
                orders.add(orderDTO);
            }
        }
        return orders;
    }

}

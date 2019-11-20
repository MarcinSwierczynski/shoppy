package net.swierczynski.shop.order.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.swierczynski.shop.order.infrastructure.OrderDTO;
import net.swierczynski.shop.order.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.List;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class ListOrderService {

    private final Logger logger = LoggerFactory.getLogger(ListOrderService.class);

    private final OrderRepository orderRepository;

    ListOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    List<OrderDTO> listInRange(Clock from, Clock to) {
        try {
            return orderRepository.listInRange(from, to);
        } catch (JsonProcessingException e) {
            // TODO: 19/11/2019 we probably should return Result here to transfer error messages
            logger.error("Cannot parse order items", e);
            return List.of();
        }
    }

}

package net.swierczynski.shop.order.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.swierczynski.shop.order.infrastructure.OrderDTO;

import java.time.Clock;
import java.util.List;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public interface OrderRepository {

    Order place(Order order) throws JsonProcessingException;
    List<OrderDTO> listInRange(Clock from, Clock to) throws JsonProcessingException;

}

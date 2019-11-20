package net.swierczynski.shop.order.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.order.domain.Order;
import net.swierczynski.shop.order.domain.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import static net.swierczynski.shop.common.Result.failure;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class PlaceOrderService {

    private final OrderRepository orderRepository;

    PlaceOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    Result<Order> placeOrder(PlaceOrderDTO dto) {
        try {
            final Order order = Order.create(dto.getBuyerEmail());
            dto.getProductsQuantities().forEach(order::addItem);

            Result<Order> result = order.place();
            orderRepository.place(order);
            return result;
        } catch (JsonProcessingException e) {
            return failure("Cannot de/serialize order items");
        } catch (IllegalArgumentException e) {
            return failure(e.getMessage());
        }
    }

}

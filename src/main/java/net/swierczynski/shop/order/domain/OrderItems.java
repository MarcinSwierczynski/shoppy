package net.swierczynski.shop.order.domain;

import net.swierczynski.shop.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class OrderItems {

    private final List<OrderItem> orderItems;

    private OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    static OrderItems create() {
        return new OrderItems(new ArrayList<>());
    }

    OrderItems add(Product product, int quantity) {
        final List<OrderItem> newOrderItems = new ArrayList<>(this.orderItems);
        newOrderItems.add(OrderItem.of(product, quantity));
        return new OrderItems(newOrderItems);
    }

    boolean isNotEmpty() {
        return !this.orderItems.isEmpty();
    }

    BigDecimal calculateTotalPrice() {
        return this.orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}

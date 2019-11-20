package net.swierczynski.shop.order.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.common.Version;
import net.swierczynski.shop.product.domain.Product;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static net.swierczynski.shop.common.Result.failure;
import static net.swierczynski.shop.common.Result.success;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class Order {

    private final OrderId orderId;
    private final String buyerEmail; // TODO: 19/11/2019 implement as value object with format validation
    private final Version version;
    private OrderItems orderItems;
    private Clock placeDate;

    Order(OrderId orderId, OrderItems orderItems, String buyerEmail, Version version) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.buyerEmail = buyerEmail;
        this.version = version;
    }

    Order(OrderId orderId, OrderItems orderItems, String buyerEmail, Clock placeDate, Version version) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.buyerEmail = buyerEmail;
        this.placeDate = placeDate;
        this.version = version;
    }

    public static Order create(String buyerEmail) {
        return new Order(OrderId.newOne(), OrderItems.create(), buyerEmail, Version.zero());
    }

    public Result<Order> addItem(Product product, int quantity) {
        this.orderItems = this.orderItems.add(product, quantity);
        return success(this);
    }

    public Result<Order> place() {
        if (!containsAnyItem()) {
            return failure("The order does not contain any items");
        }
        this.placeDate = Clock.systemDefaultZone();
        return success(this);
    }

    private boolean containsAnyItem() {
        return orderItems.isNotEmpty();
    }

    BigDecimal calculateTotalPrice() {
        return orderItems.calculateTotalPrice();
    }

    public OrderSnapshot toSnapshot(ObjectMapper objectMapper) throws JsonProcessingException {
        return new OrderSnapshot(orderId.id(), buyerEmail, objectMapper.writeValueAsString(orderItems),
                placeDate.instant(), calculateTotalPrice(), version.value());
    }

    public OrderId id() {
        return orderId;
    }

    public boolean wasPlacedBetween(Clock from, Clock to) {
        final Instant placeDateInstant = this.placeDate.instant();

        boolean afterFrom = true;
        if (Objects.nonNull(from)) {
            afterFrom = placeDateInstant.isAfter(from.instant());
        }

        boolean beforeTo = true;
        if (Objects.nonNull(to)) {
            beforeTo = placeDateInstant.isBefore(to.instant());
        }

        return afterFrom && beforeTo;
    }

}

package net.swierczynski.shop.order.infrastructure;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.order.application.OrderFacade;
import net.swierczynski.shop.order.application.PlaceOrderCommand;
import net.swierczynski.shop.order.domain.Order;
import net.swierczynski.shop.product.domain.ProductId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
@RestController
@RequestMapping(path = "/orders")
class OrderController {

    private final OrderFacade orderFacade;

    OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping
    List<OrderDTO> list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return orderFacade.listInRange(dateToClock(from), dateToClock(to));
    }

    private Clock dateToClock(@RequestParam LocalDateTime from) {
        if (isNull(from)) {
            return null;
        }
        return Clock.fixed(from.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    OrdersView placeOrder(@RequestBody PlaceOrderDTO command) {
        final Result<Order> result = orderFacade.place(command.toPlaceOrderCommand());
        return OrdersView.create(result);
    }

}

class PlaceOrderDTO {

    private Map<String, Integer> productsIdsQuantities;
    private String buyerEmail;

    public PlaceOrderDTO() {
    }

    public void setProductsIdsQuantities(Map<String, Integer> productsIdsQuantities) {
        this.productsIdsQuantities = productsIdsQuantities;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    PlaceOrderCommand toPlaceOrderCommand() {
        final Map<ProductId, Integer> productIdToQuantity = productsIdsQuantities.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> new ProductId(UUID.fromString(entry.getKey())),
                        Map.Entry::getValue
                ));

        return new PlaceOrderCommand(
                productIdToQuantity,
                buyerEmail
        );
    }

}

class OrdersView {

    private final UUID orderId;
    private final String failureReason;

    private OrdersView(Order order) {
        this.orderId = order.id().id();
        this.failureReason = null;
    }

    private OrdersView(String failureReason) {
        this.orderId = null;
        this.failureReason = failureReason;
    }

    static OrdersView create(Result<Order> result) {
        if (result.isSuccessful()) {
            return new OrdersView(result.result().orElseThrow());
        } else {
            return new OrdersView(result.reason());
        }
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getFailureReason() {
        return failureReason;
    }

}
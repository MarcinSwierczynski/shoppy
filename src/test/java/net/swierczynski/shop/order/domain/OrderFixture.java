package net.swierczynski.shop.order.domain;

import net.swierczynski.shop.common.Version;
import net.swierczynski.shop.product.domain.Product;

import java.time.Clock;
import java.util.Map;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
public class OrderFixture {

    public static Order newOrder(Clock placeDate) {
        return new Order(OrderId.newOne(), OrderItems.create(), "marcin@swierczynski.net", placeDate, Version.zero());
    }

    public static Order newOrder(Clock placeDate, Map<Product, Integer> productToQuantity) {
        final OrderItems orderItems = productToQuantity.entrySet()
                .stream()
                .reduce(
                        OrderItems.create(),
                        (orderItemsAcc, entry) -> orderItemsAcc.add(entry.getKey(), entry.getValue()),
                        (orderItems1, orderItems2) -> orderItems1
                );
        return new Order(OrderId.newOne(), orderItems, "marcin@swierczynski.net", placeDate, Version.zero());
    }

}

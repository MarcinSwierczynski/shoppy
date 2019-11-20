package net.swierczynski.shop.order.domain;

import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductSnapshot;

import java.math.BigDecimal;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class OrderItem {

    private final String productName;
    private final BigDecimal price;
    private final int quantity;

    static OrderItem of(Product product, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(String.format("The minimum number of %s to order is 1", product.id().id()));
        }
        final ProductSnapshot snapshot = product.toSnapshot();
        return new OrderItem(snapshot.getName(), snapshot.getPrice(), quantity);
    }

    private OrderItem(String productName, BigDecimal price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

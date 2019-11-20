package net.swierczynski.shop.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
class Price {

    static Price of(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        return new Price(price);
    }

    private final BigDecimal price;

    Price(BigDecimal price) {
        this.price = price;
    }

    BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price1 = (Price) o;
        return price.equals(price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                '}';
    }
}

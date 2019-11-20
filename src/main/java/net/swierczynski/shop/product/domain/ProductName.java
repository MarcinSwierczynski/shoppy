package net.swierczynski.shop.product.domain;

import java.util.Objects;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
class ProductName {

    static ProductName of(String name) {
        if (name.trim().length() < 1) {
            throw new IllegalArgumentException("Product name must have at least one character");
        }
        return new ProductName(name);
    }

    private final String name;

    ProductName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductName)) return false;
        ProductName that = (ProductName) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ProductName{" +
                "name='" + name + '\'' +
                '}';
    }
}

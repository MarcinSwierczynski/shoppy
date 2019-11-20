package net.swierczynski.shop.product.application;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
public class CreateProductCommand {
    private final String name;
    private final BigDecimal price;

    public CreateProductCommand(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateProductCommand)) return false;
        CreateProductCommand that = (CreateProductCommand) o;
        return name.equals(that.name) &&
                price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "CreateProductCommand{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

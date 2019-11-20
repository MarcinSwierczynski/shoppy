package net.swierczynski.shop.product.application;

import net.swierczynski.shop.product.domain.ProductId;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
public class UpdateProductCommand {

    private final ProductId productId;
    private final String name;
    private final BigDecimal price;

    public UpdateProductCommand(ProductId productId, String name, BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    ProductId getProductId() {
        return productId;
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
        if (!(o instanceof UpdateProductCommand)) return false;
        UpdateProductCommand that = (UpdateProductCommand) o;
        return productId.equals(that.productId) &&
                name.equals(that.name) &&
                price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, price);
    }

    @Override
    public String toString() {
        return "UpdateProductCommand{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

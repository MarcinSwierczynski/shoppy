package net.swierczynski.shop.product.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public class ProductId {

    private final UUID id;

    public ProductId(UUID id) {
        this.id = id;
    }

    public static ProductId newOne() {
        return new ProductId(UUID.randomUUID());
    }

    public UUID id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId that = (ProductId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "ProductId{id=" + id + '}';
    }

}

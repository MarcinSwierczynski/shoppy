package net.swierczynski.shop.product.application;

import net.swierczynski.shop.product.domain.ProductId;

import java.util.Objects;
import java.util.Set;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class ProductsByIdsQuery {

    private final Set<ProductId> productIds;

    public ProductsByIdsQuery(Set<ProductId> productIds) {
        this.productIds = productIds;
    }

    Set<ProductId> getProductIds() {
        return productIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductsByIdsQuery)) return false;
        ProductsByIdsQuery that = (ProductsByIdsQuery) o;
        return productIds.equals(that.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productIds);
    }

}

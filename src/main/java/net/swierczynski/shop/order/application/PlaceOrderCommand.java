package net.swierczynski.shop.order.application;

import net.swierczynski.shop.product.domain.ProductId;

import java.util.Map;
import java.util.Set;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class PlaceOrderCommand {

    private final Map<ProductId, Integer> productsIdsQuantities;
    private final String buyerEmail;

    public PlaceOrderCommand(Map<ProductId, Integer> productsIdsQuantities, String buyerEmail) {
        this.productsIdsQuantities = productsIdsQuantities;
        this.buyerEmail = buyerEmail;
    }

    Set<ProductId> getProductIds() {
        return productsIdsQuantities.keySet();
    }

    Map<ProductId, Integer> getProductsQuantities() {
        return productsIdsQuantities;
    }

    String getBuyerEmail() {
        return buyerEmail;
    }

}

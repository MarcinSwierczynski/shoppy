package net.swierczynski.shop.order.application;

import net.swierczynski.shop.product.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class PlaceOrderDTO {

    private final Map<Product, Integer> productsQuantities;
    private final String buyerEmail;

    PlaceOrderDTO(PlaceOrderCommand command,
                  List<Product> products) {
        final Map<UUID, Product> productIdProductMap = mapProductsById(products);
        this.productsQuantities = mapProductsToQuantities(command, productIdProductMap);
        this.buyerEmail = command.getBuyerEmail();
    }

    private Map<Product, Integer> mapProductsToQuantities(PlaceOrderCommand command, Map<UUID, Product> productIdProductMap) {
        return productIdProductMap.size() > 0 ? mapProductToQuantity(command, productIdProductMap) : Map.of();
    }

    private Map<Product, Integer> mapProductToQuantity(PlaceOrderCommand command, Map<UUID, Product> productIdProductMap) {
        return command.getProductsQuantities().entrySet()
                .stream()
                .collect(toMap(entry -> productIdProductMap.get(entry.getKey().id()), Map.Entry::getValue));
    }

    private Map<UUID, Product> mapProductsById(List<Product> products) {
        return products.stream()
                .collect(toMap(product -> product.id().id(), Function.identity()));
    }

    Map<Product, Integer> getProductsQuantities() {
        return productsQuantities;
    }

    String getBuyerEmail() {
        return buyerEmail;
    }

}

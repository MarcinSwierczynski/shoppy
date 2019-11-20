package net.swierczynski.shop.product.infrastructure;

import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductId;
import net.swierczynski.shop.product.domain.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> productMap = new HashMap<>();

    @Override
    public Product createNew(Product product) {
        return update(product);
    }

    @Override
    public Product update(Product product) {
        productMap.put(product.id(), product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public Optional<Product> findBy(ProductId id) {
        return productMap.values()
                .stream()
                .filter(product -> product.id().equals(id))
                .findAny();
    }

    @Override
    public List<Product> findProductsByIds(Set<ProductId> productIds) {
        return productMap.values()
                .stream()
                .filter(product -> productIds.contains(product.id()))
                .collect(Collectors.toList());
    }

}

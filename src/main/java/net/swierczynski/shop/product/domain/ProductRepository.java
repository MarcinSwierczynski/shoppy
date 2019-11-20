package net.swierczynski.shop.product.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public interface ProductRepository {

    Product createNew(Product product);

    Product update(Product product);

    List<Product> findAll();

    Optional<Product> findBy(ProductId id);

    List<Product> findProductsByIds(Set<ProductId> productIds);

}

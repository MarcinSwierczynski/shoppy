package net.swierczynski.shop.product.application;

import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductRepository;

import java.util.List;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
class ListProductService {

    private final ProductRepository productRepository;

    ListProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    List<Product> listAll() {
        return productRepository.findAll();
    }

    List<Product> findProductsByIds(ProductsByIdsQuery query) {
        return productRepository.findProductsByIds(query.getProductIds());
    }

}

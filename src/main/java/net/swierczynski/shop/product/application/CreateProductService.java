package net.swierczynski.shop.product.application;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import static net.swierczynski.shop.common.Result.failure;
import static net.swierczynski.shop.common.Result.success;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
class CreateProductService {

    private final ProductRepository productRepository;

    CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    Result<Product> create(CreateProductCommand command) {
        try {
            final Product product = productRepository.createNew(Product.product(command.getName(), command.getPrice()));
            return success(product);
        } catch (IllegalArgumentException e) {
            return failure(e.getMessage());
        }
    }

}

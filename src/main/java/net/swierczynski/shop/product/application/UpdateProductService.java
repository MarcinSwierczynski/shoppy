package net.swierczynski.shop.product.application;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.format;
import static net.swierczynski.shop.common.Result.failure;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
class UpdateProductService {

    private final ProductRepository productRepository;

    UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    Result<Product> update(UpdateProductCommand command) {
        final Product product = productRepository.findBy(command.getProductId()).orElse(null);
        if (Objects.isNull(product)) {
            return failure(format("Product %s cannot be found", command.getProductId()));
        }

        try {
            Result<Product> result = product.update(command.getName(), command.getPrice());
            productRepository.update(product);
            return result;
        } catch (IllegalArgumentException e) {
            return failure(e.getMessage());
        }
    }

}

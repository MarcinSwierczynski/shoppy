package net.swierczynski.shop.product.application;

import net.swierczynski.shop.product.domain.ProductRepository;
import net.swierczynski.shop.product.infrastructure.InMemoryProductRepository;
import net.swierczynski.shop.product.infrastructure.JdbcProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
@Configuration
class ProductConfiguration {

    public ProductFacade productFacade() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();

   		return productFacade(
   		        createProductService(productRepository),
                updateProductService(productRepository),
                listProductService(productRepository)
        );
   	}

    @Bean
    ProductFacade productFacade(CreateProductService createProductService,
                                UpdateProductService updateProductService,
                                ListProductService listProductService) {
        return new ProductFacade(
                createProductService,
                updateProductService,
                listProductService
        );
    }

    @Bean
    CreateProductService createProductService(ProductRepository productRepository) {
        return new CreateProductService(productRepository);
    }

    @Bean
    UpdateProductService updateProductService(ProductRepository productRepository) {
        return new UpdateProductService(productRepository);
    }

    @Bean
    ListProductService listProductService(ProductRepository productRepository) {
        return new ListProductService(productRepository);
    }

    @Bean
    ProductRepository productRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcProductRepository(jdbcTemplate);
    }

}

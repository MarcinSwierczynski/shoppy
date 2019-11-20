package net.swierczynski.shop.product.infrastructure

import net.swierczynski.shop.config.TestDatabaseConfig
import net.swierczynski.shop.product.domain.Product
import net.swierczynski.shop.product.domain.ProductFixture
import net.swierczynski.shop.product.domain.ProductId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Specification

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
@SpringBootTest(classes = TestDatabaseConfig.class)
class JdbcProductRepositoryIntegrationSpec extends Specification {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate
    JdbcProductRepository repository

    def setup() {
        repository = new JdbcProductRepository(jdbcTemplate)
    }

    def "can save a product"() {
        given:
            Product product = ProductFixture.newProduct()
        when:
            repository.createNew(product)
        and:
            Optional<Product> productMaybe = repository.findBy(product.id())
        then:
            productMaybe.isPresent()
        and:
            productMaybe.get().id() == product.id()
    }

    def 'optimistic locking should work'() {
        given:
            Product product = ProductFixture.newProduct()
        and:
            repository.createNew(product)
        and:
            Optional<Product> productMaybe = repository.findBy(product.id())
        and:
            someoneModifiedProductInTheMeantime(product.id())
        when:
            repository.update(productMaybe.get())
        then:
            thrown(OptimisticLockingFailureException)
    }

    void someoneModifiedProductInTheMeantime(ProductId productId) {
        Product product = repository.findBy(productId).get()
        product.update("new name", BigDecimal.TEN)
        repository.update(product)
    }

    def 'can find products by ids'() {
        given:
            Product product1 = ProductFixture.newProduct()
            Product product2 = ProductFixture.newProduct()
        and:
            repository.createNew(product1)
            repository.createNew(product2)
        when:
            List<Product> products = repository.findProductsByIds([product1.id(), product2.id()].toSet())
        then:
            products.size() == 2
        and:
            products.collect {product -> product.id()}.toSet() == [product1.id(), product2.id()].toSet()
    }

}

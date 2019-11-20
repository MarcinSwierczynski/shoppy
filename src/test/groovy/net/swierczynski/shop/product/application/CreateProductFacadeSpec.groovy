package net.swierczynski.shop.product.application

import net.swierczynski.shop.common.Result
import net.swierczynski.shop.product.domain.ProductSnapshot
import spock.lang.Specification

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
class CreateProductFacadeSpec extends Specification {

    ProductFacade facade = new ProductConfiguration().productFacade()

    def "should create a new product"() {
        when:
            Result result = facade.create(new CreateProductCommand("product", BigDecimal.TEN))
        then:
            result.isSuccessful()
        and:
            facade.listAll().size() == 1
        and:
            ProductSnapshot snapshot = facade.listAll().first().toSnapshot()
            snapshot.name == "product"
            snapshot.price == BigDecimal.TEN
    }

    def "should not create a new product with empty name"() {
        when:
            Result result = facade.create(new CreateProductCommand(" ", BigDecimal.TEN))
        then:
            result.isFailure()
            result.reason() == 'Product name must have at least one character'
    }

    def "should not create a new product with a price below zero"() {
        when:
            Result result = facade.create(new CreateProductCommand("product", BigDecimal.ZERO - 1))
        then:
            result.isFailure()
            result.reason() == 'Price must be greater than 0'
    }

}

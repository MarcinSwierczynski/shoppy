package net.swierczynski.shop.product.application

import net.swierczynski.shop.common.Result
import net.swierczynski.shop.product.domain.Product
import net.swierczynski.shop.product.domain.ProductId
import spock.lang.Specification

import static java.lang.String.format

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
class UpdateProductFacadeSpec extends Specification {

    ProductFacade facade = new ProductConfiguration().productFacade()

    def "should update existing product"() {
        given:
            facade.create(new CreateProductCommand("different name", BigDecimal.TEN))
        and:
            Product product = facade.listAll().first()
        and:
            UpdateProductCommand command = new UpdateProductCommand(product.id(), "name", BigDecimal.ONE)
        when:
            Result result = facade.update(command)
        then:
            result.isSuccessful()
        and:
            product.toSnapshot().name == "name"
            product.toSnapshot().price == BigDecimal.ONE
    }

    def "should not update non-existing product"() {
        given:
            UpdateProductCommand command = new UpdateProductCommand(ProductId.newOne(), "name", BigDecimal.ONE)
        when:
            Result result = facade.update(command)
        then:
            result.isFailure()
            result.reason() == format("Product %s cannot be found", command.getProductId())
    }

    def "should not set empty name to a product"() {
        given:
            facade.create(new CreateProductCommand("different name", BigDecimal.TEN))
        and:
            Product product = facade.listAll().first()
        when:
            Result result = facade.update(new UpdateProductCommand(product.id(), " ", BigDecimal.TEN))
        then:
            result.isFailure()
            result.reason() == 'Product name must have at least one character'
    }

    def "should not create a new product with a price below zero"() {
        given:
            facade.create(new CreateProductCommand("different name", BigDecimal.TEN))
        and:
            Product product = facade.listAll().first()
        when:
            Result result = facade.update(new UpdateProductCommand(product.id(), "product", BigDecimal.ZERO - 1))
        then:
            result.isFailure()
            result.reason() == 'Price must be greater than 0'
    }

}

package net.swierczynski.shop.product.application

import net.swierczynski.shop.product.domain.Product
import spock.lang.Specification

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
class ListAllProductFacadeSpec extends Specification {

    ProductFacade facade = new ProductConfiguration().productFacade()

    def "should return empty list if no products available"() {
        expect:
            facade.listAll().isEmpty()
    }

    def "should return all products available"() {
        given:
            facade.create(new CreateProductCommand("p1 name", BigDecimal.ONE))
        and:
            facade.create(new CreateProductCommand("p2 name", BigDecimal.TEN))
        when:
            List<Product> products = facade.listAll()
        then:
            products.size() == 2
            products.sort { p1, p2 -> p1.toSnapshot().name <=> p2.toSnapshot().name }
            products.first().toSnapshot().name == "p1 name"
            products.first().toSnapshot().price == BigDecimal.ONE
            products.last().toSnapshot().name == "p2 name"
            products.last().toSnapshot().price == BigDecimal.TEN
    }

}

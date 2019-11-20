package net.swierczynski.shop.product.application

import net.swierczynski.shop.product.domain.Product
import spock.lang.Specification

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class FindProductsByIdsFacadeSpec extends Specification {

    ProductFacade facade = new ProductConfiguration().productFacade()

    def "should find products by ids"() {
        given:
            facade.create(new CreateProductCommand("name 1", BigDecimal.TEN))
            facade.create(new CreateProductCommand("name 2", BigDecimal.ONE))
        and:
            Product product = facade.listAll()
                    .sort { p1, p2 -> p1.toSnapshot().name <=> p2.toSnapshot().name}
                    .first()
        and:
            ProductsByIdsQuery query = new ProductsByIdsQuery(Set.of(product.id()))
        when:
            List<Product> products = facade.findProductsByIds(query)
        then:
            facade.listAll().size() == 2
        and:
            products.size() == 1
            product.toSnapshot().name == "name 1"
            product.toSnapshot().price == BigDecimal.TEN
    }

}

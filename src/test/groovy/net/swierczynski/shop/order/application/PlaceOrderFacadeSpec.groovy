package net.swierczynski.shop.order.application

import net.swierczynski.shop.common.Result
import net.swierczynski.shop.product.application.ProductFacade
import net.swierczynski.shop.product.application.ProductsByIdsQuery
import net.swierczynski.shop.product.domain.Product
import net.swierczynski.shop.product.domain.ProductId
import net.swierczynski.shop.product.domain.ProductSnapshot
import spock.lang.Specification

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class PlaceOrderFacadeSpec extends Specification {

    ProductFacade productFacade = Mock()
    OrderFacade facade = new OrderConfiguration().orderFacade(productFacade)

    def "should place a new order"() {
        given:
            ProductId productId1 = ProductId.newOne()
            ProductId productId2 = ProductId.newOne()
        and:
            Map<ProductId, Integer> productsQuantities = [
                    (productId1): 5,
                    (productId2): 10
            ]
        and:
            ProductsByIdsQuery query = new ProductsByIdsQuery(productsQuantities.keySet())
            productFacade.findProductsByIds(query) >> [
                    Product.fromSnapshot(new ProductSnapshot(productId1.id(), "one", BigDecimal.ZERO, 0)),
                    Product.fromSnapshot(new ProductSnapshot(productId2.id(), "two", BigDecimal.ONE, 0))
            ]
        when:
            Result result = facade.place(new PlaceOrderCommand(productsQuantities, 'marcin@swierczynski.net'))
        then:
            result.isSuccessful()
    }

    def "should not create a new order if product quantity is below 1"() {
        given:
            ProductId productId1 = ProductId.newOne()
            ProductId productId2 = ProductId.newOne()
        and:
            Map<ProductId, Integer> productsQuantities = [
                    (productId1): 0,
                    (productId2): 10
            ]
        and:
            ProductsByIdsQuery query = new ProductsByIdsQuery(productsQuantities.keySet())
            productFacade.findProductsByIds(query) >> [
                    Product.fromSnapshot(new ProductSnapshot(productId1.id(), "one", BigDecimal.ZERO, 0)),
                    Product.fromSnapshot(new ProductSnapshot(productId2.id(), "two", BigDecimal.ONE, 0))
            ]
        when:
            Result result = facade.place(new PlaceOrderCommand(productsQuantities, 'marcin@swierczynski.net'))
        then:
            result.isFailure()
            result.reason() == String.format("The minimum number of %s to order is 1", productId1.id())
    }

    def "should not create a new order if no items were added"() {
        given:
            ProductId productId1 = ProductId.newOne()
            ProductId productId2 = ProductId.newOne()
        and:
            Map<ProductId, Integer> productsQuantities = [
                    (productId1): 5,
                    (productId2): 10
            ]
        and:
            ProductsByIdsQuery query = new ProductsByIdsQuery(productsQuantities.keySet())
            productFacade.findProductsByIds(query) >> []
        when:
            Result result = facade.place(new PlaceOrderCommand(productsQuantities, 'marcin@swierczynski.net'))
        then:
            result.isFailure()
            result.reason() == "The order does not contain any items"
    }

}

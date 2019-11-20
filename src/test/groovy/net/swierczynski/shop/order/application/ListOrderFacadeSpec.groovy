package net.swierczynski.shop.order.application

import net.swierczynski.shop.common.Result
import net.swierczynski.shop.order.infrastructure.OrderDTO
import net.swierczynski.shop.product.application.ProductFacade
import net.swierczynski.shop.product.application.ProductsByIdsQuery
import net.swierczynski.shop.product.domain.Product
import net.swierczynski.shop.product.domain.ProductId
import net.swierczynski.shop.product.domain.ProductSnapshot
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDate

import static java.time.ZoneId.systemDefault

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class ListOrderFacadeSpec extends Specification {

    ProductFacade productFacade = Mock()
    OrderFacade facade = new OrderConfiguration().orderFacade(productFacade)

    private static Instant now = LocalDate.of(2000, 1, 2).atStartOfDay(systemDefault()).toInstant()
    private static Clock clock = Clock.fixed(now, systemDefault())

    def "should list orders in a time range"() {
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
        and:
            List<OrderDTO> pastRange = facade.listInRange(null, clock)
            pastRange.size() == 0
        and:
            List<OrderDTO> sameMoment = facade.listInRange(clock, clock)
            sameMoment.size() == 0
        and:
            List<OrderDTO> futureRange = facade.listInRange(clock, null)
            futureRange.size() == 1
    }

}

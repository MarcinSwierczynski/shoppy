package net.swierczynski.shop.order.infrastructure

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import net.swierczynski.shop.config.TestDatabaseConfig
import net.swierczynski.shop.order.domain.Order
import net.swierczynski.shop.order.domain.OrderFixture
import net.swierczynski.shop.product.domain.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Specification

import java.time.Clock

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
@SpringBootTest(classes = TestDatabaseConfig.class)
class JdbcOrderRepositoryIntegrationSpec extends Specification {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate
    JdbcOrderRepository repository


    def setup() {
        final ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

        repository = new JdbcOrderRepository(jdbcTemplate, objectMapper)
    }

    def "can save an order"() {
        given:
            Clock now = Clock.systemDefaultZone()
            Map<Product, Integer> productToQuantity = [
                    (Product.product("name 1", BigDecimal.ONE)) : 1,
                    (Product.product("name 2", BigDecimal.TEN)) : 2,
            ]
        and:
            Order order = OrderFixture.newOrder(now, productToQuantity)
        when:
            repository.place(order)
        and:
            List<OrderDTO> orders = repository.listInRange(null, null)
        then:
            orders.size() == 1
        and:
            OrderDTO orderDTO = orders.first()
            orderDTO.id == order.id().id()
            orderDTO.buyerEmail == 'marcin@swierczynski.net'
            orderDTO.placeDate != null
            orderDTO.totalPrice == new BigDecimal(21)
        and:
            orderDTO.items.orderItems.sort {i1, i2 -> i1.productName.toString() <=> i2.productName.toString()}
            orderDTO.items.orderItems.size() == 2
            orderDTO.items.orderItems[0].productName.asText() == "name 1"
            orderDTO.items.orderItems[0].price.asDouble() == BigDecimal.ONE
            orderDTO.items.orderItems[0].quantity.intValue() == 1
            orderDTO.items.orderItems[1].productName.asText() == "name 2"
            orderDTO.items.orderItems[1].price.asDouble() == BigDecimal.TEN
            orderDTO.items.orderItems[1].quantity.intValue() == 2
    }

}

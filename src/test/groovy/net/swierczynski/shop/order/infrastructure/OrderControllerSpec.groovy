package net.swierczynski.shop.order.infrastructure

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import net.swierczynski.shop.common.Result
import net.swierczynski.shop.order.application.OrderFacade
import net.swierczynski.shop.order.application.PlaceOrderCommand
import net.swierczynski.shop.order.domain.Order
import net.swierczynski.shop.product.domain.ProductId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.bind.annotation.RequestParam
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

import static java.util.Objects.isNull
import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
@WebMvcTest(OrderController.class)
class OrderControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    OrderFacade orderFacade

    def "should place a new order"() {
        given:
            Result<Order> success = Result.success(Order.create('marcin@swierczynski.net'))
        and:
            Map<String, Integer> productIdStringQuantity = [
                    (UUID.randomUUID().toString()): 1,
                    (UUID.randomUUID().toString()): 2
            ]
            Map<String, Integer> payload = [
                    productsIdsQuantities: productIdStringQuantity,
                    buyerEmail: 'marcin@swierczynski.net'
            ]
        and:
            Map<ProductId, Integer> productIdQuantity = productIdStringQuantity.collectEntries { [ (new ProductId(UUID.fromString(it.key))) : it.value] }
            orderFacade.place(new PlaceOrderCommand(productIdQuantity, 'marcin@swierczynski.net')) >> success
        when:
            def perform = mockMvc.perform(
                    post('/orders')
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(payload))
            )
        then:
            perform.andExpect(status().isCreated())
        and:
            perform.andExpect(jsonPath('$.failureReason', is(emptyString())))
            perform.andExpect(jsonPath('$.orderId', is(not(emptyString()))))
    }

    def "should filter orders by date"() {
        given:
            OrderDTO order = new OrderDTO(UUID.randomUUID(), 'marcin@swierczynski.net', null, Instant.now(), BigDecimal.TEN)
        and:
            LocalDateTime past = LocalDateTime.of(2019, 11, 11, 20, 00)
            orderFacade.listInRange(dateToClock(past), null) >> [order]
        when:
            def perform = mockMvc.perform(
                    get('/orders')
                            .param('from', past.atZone(ZoneId.of('UTC')).format(DateTimeFormatter.ISO_INSTANT))
            )
        then:
            perform.andExpect(status().isOk())
        and:
            perform.andExpect(jsonPath('$', hasSize(1)))
            perform.andExpect(jsonPath('$[0].id', is(order.id.toString())))
            perform.andExpect(jsonPath('$[0].buyerEmail', is(order.buyerEmail)))
            perform.andExpect(jsonPath('$[0].totalPrice', is(order.totalPrice as Integer)))
            perform.andExpect(jsonPath('$[0].placeDate', is(order.placeDate)))
    }

    private static String toJson(Object o) {
        final ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        return objectMapper.writeValueAsString(o)
    }

    private static Clock dateToClock(@RequestParam LocalDateTime from) {
        if (isNull(from)) {
            return null
        }
        return Clock.fixed(from.toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        OrderFacade orderFacade() {
            return detachedMockFactory.Stub(OrderFacade)
        }
    }

}
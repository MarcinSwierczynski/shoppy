package net.swierczynski.shop.order.application;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swierczynski.shop.order.domain.OrderRepository;
import net.swierczynski.shop.order.infrastructure.InMemoryOrderRepository;
import net.swierczynski.shop.order.infrastructure.JdbcOrderRepository;
import net.swierczynski.shop.product.application.ProductFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
@Configuration
class OrderConfiguration {

    public OrderFacade orderFacade(ProductFacade productFacade) {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository(objectMapper());

        return orderFacade(
                productFacade,
                placeOrderService(orderRepository),
                listOrderService(orderRepository)
        );
    }

    @Bean
    OrderFacade orderFacade(ProductFacade productFacade,
                            PlaceOrderService placeOrderService,
                            ListOrderService listOrderService) {
        return new OrderFacade(
                productFacade,
                placeOrderService,
                listOrderService
        );
    }

    @Bean
    PlaceOrderService placeOrderService(OrderRepository orderRepository) {
        return new PlaceOrderService(orderRepository);
    }

    @Bean
    ListOrderService listOrderService(OrderRepository orderRepository) {
        return new ListOrderService(orderRepository);
    }

    @Bean
    OrderRepository orderRepository(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        return new JdbcOrderRepository(jdbcTemplate, objectMapper);
    }

    @Bean
    ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

}

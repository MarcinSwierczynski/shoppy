package net.swierczynski.shop.order.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swierczynski.shop.order.domain.Order;
import net.swierczynski.shop.order.domain.OrderRepository;
import net.swierczynski.shop.order.domain.OrderSnapshot;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class JdbcOrderRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Order place(Order order) throws JsonProcessingException {
        final OrderSnapshot snapshot = order.toSnapshot(objectMapper);
        jdbcTemplate.update("INSERT INTO orders " +
                        "(id, order_id, buyer_email, items, place_date, total_price, version) " +
                        "VALUES " +
                        "(orders_seq.nextval, :order_id, :buyer_email, :items, :place_date, :total_price, :version)",
                Map.of(
                        "order_id", snapshot.getOrderId(),
                        "buyer_email", snapshot.getBuyerEmail(),
                        "items", snapshot.getItems(),
                        "place_date", snapshot.getPlaceDate(),
                        "total_price", snapshot.getTotalPrice(),
                        "version", 0
                ));
        return order;
    }

    @Override
    public List<OrderDTO> listInRange(Clock from, Clock to) throws JsonProcessingException {
        // TODO: 19/11/2019 refactor nicely

        List<OrderSnapshot> orderSnapshots = List.of();

        if (nonNull(from) && nonNull(to)) {
            orderSnapshots = jdbcTemplate.query(
                    "SELECT * FROM orders o where o.place_date < :to AND o.place_date > :from",
                    Map.of(
                            "from", from.instant(),
                            "to", to.instant()
                    ),
                    new BeanPropertyRowMapper<>(OrderSnapshot.class));
        }

        if (nonNull(from) && isNull(to)) {
            orderSnapshots = jdbcTemplate.query(
                    "SELECT * FROM orders o where o.place_date > :from",
                    Map.of(
                            "from", from.instant()
                    ),
                    new BeanPropertyRowMapper<>(OrderSnapshot.class));
        }

        if (isNull(from) && nonNull(to)) {
            orderSnapshots = jdbcTemplate.query(
                    "SELECT * FROM orders o where o.place_date < :to",
                    Map.of(
                            "to", to.instant()
                    ),
                    new BeanPropertyRowMapper<>(OrderSnapshot.class));
        }

        if (isNull(from) && isNull(to)) {
            orderSnapshots = jdbcTemplate.query(
                    "SELECT * FROM orders",
                    new BeanPropertyRowMapper<>(OrderSnapshot.class));
        }

        return mapToOrders(orderSnapshots);
    }

    private List<OrderDTO> mapToOrders(List<OrderSnapshot> orderSnapshots) throws JsonProcessingException {
        List<OrderDTO> orders = new ArrayList<>();
        for (OrderSnapshot orderSnapshot : orderSnapshots) {
            OrderDTO order = OrderDTO.fromSnapshot(orderSnapshot, objectMapper);
            orders.add(order);
        }
        return orders;
    }

}

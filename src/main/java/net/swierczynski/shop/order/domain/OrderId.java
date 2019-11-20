package net.swierczynski.shop.order.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class OrderId {

    private final UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }

    public static OrderId newOne() {
        return new OrderId(UUID.randomUUID());
    }

    public UUID id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId that = (OrderId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "OrderId{id=" + id + '}';
    }

}

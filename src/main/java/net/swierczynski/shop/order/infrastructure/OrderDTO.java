package net.swierczynski.shop.order.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swierczynski.shop.order.domain.OrderSnapshot;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class OrderDTO {

    private UUID id;
    private String buyerEmail;
    private JsonNode items;
    private String placeDate;
    private BigDecimal totalPrice;

    OrderDTO(UUID id, String buyerEmail, JsonNode items, Instant placeDate, BigDecimal totalPrice) {
        this.id = id;
        this.buyerEmail = buyerEmail;
        this.items = items;
        this.placeDate = placeDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT);
        this.totalPrice = totalPrice;
    }

    static OrderDTO fromSnapshot(OrderSnapshot snapshot, ObjectMapper objectMapper) throws JsonProcessingException {
        UUID id = snapshot.getOrderId();
        String buyerEmail = snapshot.getBuyerEmail();
        JsonNode orderItems = objectMapper.readTree(snapshot.getItems());
        final Instant placeDate = snapshot.getPlaceDate();
        final BigDecimal totalPrice = snapshot.getTotalPrice();
        return new OrderDTO(id, buyerEmail, orderItems, placeDate, totalPrice);
    }

    public UUID getId() {
        return id;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public JsonNode getItems() {
        return items;
    }

    public String getPlaceDate() {
        return placeDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

}

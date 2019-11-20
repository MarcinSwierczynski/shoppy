package net.swierczynski.shop.order.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class OrderSnapshot {

    private UUID orderId;
    private String buyerEmail;
    private String items;
    private Instant placeDate;
    private BigDecimal totalPrice;
    private int version;

    OrderSnapshot() {
    }

    OrderSnapshot(UUID orderId, String buyerEmail, String items, Instant placeDate, BigDecimal totalPrice, int version) {
        this.orderId = orderId;
        this.buyerEmail = buyerEmail;
        this.items = items;
        this.placeDate = placeDate;
        this.totalPrice = totalPrice;
        this.version = version;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Instant getPlaceDate() {
        return placeDate;
    }

    public void setPlaceDate(Instant placeDate) {
        this.placeDate = placeDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}

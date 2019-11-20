package net.swierczynski.shop.product.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public class ProductSnapshot {

    private UUID productId;
    private String name;
    private BigDecimal price;
    private int version;

    ProductSnapshot() {
    }

    public ProductSnapshot(UUID productId, String name, BigDecimal price, int version) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.version = version;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getVersion() {
        return version;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

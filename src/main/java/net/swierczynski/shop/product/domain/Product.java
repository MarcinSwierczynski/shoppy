package net.swierczynski.shop.product.domain;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.common.Version;

import java.math.BigDecimal;

import static net.swierczynski.shop.common.Result.success;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public class Product {

    private final ProductId productId;
    private ProductName name;
    private Price price;
    private final Version version;

    public static Product product(String name, BigDecimal price) {
        return new Product(ProductId.newOne(), ProductName.of(name), Price.of(price), Version.zero());
    }

    public static Product fromSnapshot(ProductSnapshot snapshot) {
        ProductId id = new ProductId(snapshot.getProductId());
        ProductName productName = new ProductName(snapshot.getName());
        Price price = new Price(snapshot.getPrice());
        Version version = new Version(snapshot.getVersion());
        return new Product(id, productName, price, version);
    }

    private Product(ProductId productId, ProductName name, Price price, Version version) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.version = version;
    }

    public ProductSnapshot toSnapshot() {
        return new ProductSnapshot(productId.id(), name.getName(), price.getPrice(), version.value());
    }

    public ProductId id() {
        return productId;
    }

    public Result<Product> update(String name, BigDecimal price) {
        this.name = ProductName.of(name);
        this.price = Price.of(price);
        return success(this);
    }
}

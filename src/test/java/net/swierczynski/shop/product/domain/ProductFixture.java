package net.swierczynski.shop.product.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
public class ProductFixture {

    public static Product newProduct() {
        return Product.product(randomName(), randomPrice());
    }

    private static BigDecimal randomPrice() {
        return new BigDecimal(Math.random());
    }

    private static String randomName() {
        return UUID.randomUUID().toString();
    }

}

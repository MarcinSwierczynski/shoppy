package net.swierczynski.shop.product.infrastructure;

import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductId;
import net.swierczynski.shop.product.domain.ProductRepository;
import net.swierczynski.shop.product.domain.ProductSnapshot;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static net.swierczynski.shop.product.domain.Product.fromSnapshot;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public class JdbcProductRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product createNew(Product product) {
        ProductSnapshot productSnapshot = product.toSnapshot();
        jdbcTemplate.update("INSERT INTO product " +
                        "(id, product_id, name, price, version) " +
                        " VALUES " +
                        "(products_seq.nextval, :product_id, :name, :price, :version)",
                Map.of(
                        "product_id", productSnapshot.getProductId(),
                        "name", productSnapshot.getName(),
                        "price", productSnapshot.getPrice(),
                        "version", 0));
        return product;

    }

    @Override
    public Product update(Product product) {
        ProductSnapshot productSnapshot = product.toSnapshot();
        int update = jdbcTemplate.update("UPDATE product SET " +
                        "name = :name, price = :price, version = :new_version" +
                        " WHERE " +
                        "product_id = :product_id and version = :version",
                Map.of(
                        "product_id", productSnapshot.getProductId(),
                        "name", productSnapshot.getName(),
                        "price", productSnapshot.getPrice(),
                        "version", productSnapshot.getVersion(),
                        "new_version", productSnapshot.getVersion() + 1));
        if (update == 0) {
            throw new OptimisticLockingFailureException(String.format("Product %s must have been updated in the meantime", product.id()));
        }
        return product;
    }

    @Override
    public List<Product> findAll() {
        List<ProductSnapshot> query = jdbcTemplate.query(
                "SELECT * FROM product",
                new BeanPropertyRowMapper<>(ProductSnapshot.class)
        );
        return query.stream()
                .map(Product::fromSnapshot)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findBy(ProductId id) {
        try {
            return Optional.ofNullable(fromSnapshot(
                    jdbcTemplate.queryForObject("SELECT * FROM product p where p.product_id = :product_id",
                            Map.of("product_id", id.id()),
                            new BeanPropertyRowMapper<>(ProductSnapshot.class))));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findProductsByIds(Set<ProductId> productIds) {
        final Set<UUID> ids = productIds.stream()
                .map(ProductId::id)
                .collect(Collectors.toSet());
        List<ProductSnapshot> query = jdbcTemplate.query(
                "SELECT * FROM product p WHERE p.product_id IN (SELECT * FROM TABLE(x UUID = (:product_ids)))",
                Map.of("product_ids", ids),
                new BeanPropertyRowMapper<>(ProductSnapshot.class)
        );
        return query.stream()
                .map(Product::fromSnapshot)
                .collect(Collectors.toList());
    }

}

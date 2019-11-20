package net.swierczynski.shop.product.infrastructure;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.product.application.CreateProductCommand;
import net.swierczynski.shop.product.application.ProductFacade;
import net.swierczynski.shop.product.application.UpdateProductCommand;
import net.swierczynski.shop.product.domain.Product;
import net.swierczynski.shop.product.domain.ProductId;
import net.swierczynski.shop.product.domain.ProductSnapshot;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
@RestController
@RequestMapping(path = "/products")
class ProductController {

    private final ProductFacade productFacade;

    ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @GetMapping
    ProductsView list() {
        final List<Product> products = productFacade.listAll();
        return ProductsView.create(products);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    ProductsView newProduct(@RequestParam String name,
                            @RequestParam BigDecimal price) {
        final CreateProductCommand command = new CreateProductCommand(name, price);
        final Result<Product> result = productFacade.create(command);
        return ProductsView.create(result);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    ProductsView updateProduct(@PathVariable UUID id,
                               @RequestParam String name,
                               @RequestParam BigDecimal price) {
        final UpdateProductCommand command = new UpdateProductCommand(new ProductId(id), name, price);
        final Result<Product> result = productFacade.update(command);
        return ProductsView.create(result);
    }

}

class ProductsView {

    private final List<ProductDTO> products;
    private final String failureReason;

    private ProductsView(ProductDTO product) {
        this.products = List.of(product);
        this.failureReason = null;
    }

    private ProductsView(String failureReason) {
        this.products = List.of();
        this.failureReason = failureReason;
    }

    private ProductsView(List<ProductDTO> products) {
        this.products = products;
        this.failureReason = null;
    }

    static ProductsView create(Result<Product> result) {
        if (result.isSuccessful()) {
            final ProductDTO product = result
                    .result()
                    .map(ProductDTO::of)
                    .orElseThrow();
            return new ProductsView(product);
        } else {
            return new ProductsView(result.reason());
        }
    }

    static ProductsView create(List<Product> products) {
        final List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::of)
                .collect(Collectors.toList());
        return new ProductsView(productDTOs);
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public String getFailureReason() {
        return failureReason;
    }
}

class ProductDTO {

    private final String id;
    private final String name;
    private final BigDecimal price;

    private ProductDTO(String id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    static ProductDTO of(Product product) {
        final ProductSnapshot snapshot = product.toSnapshot();
        return new ProductDTO(
                snapshot.getProductId().toString(),
                snapshot.getName(),
                snapshot.getPrice()
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
package net.swierczynski.shop.product.application;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.product.domain.Product;

import java.util.List;

/**
 * @author Marcin Świerczyński
 * @since 16/11/2019
 */
public class ProductFacade {

    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final ListProductService listProductService;

    ProductFacade(CreateProductService createProductService,
                  UpdateProductService updateProductService,
                  ListProductService listProductService) {
        this.createProductService = createProductService;
        this.updateProductService = updateProductService;
        this.listProductService = listProductService;
    }

    public Result<Product> create(CreateProductCommand command) {
        return createProductService.create(command);
    }

    public Result<Product> update(UpdateProductCommand command) {
        return updateProductService.update(command);
    }

    public List<Product> listAll() {
        return listProductService.listAll();
    }

    public List<Product> findProductsByIds(ProductsByIdsQuery query) {
        return listProductService.findProductsByIds(query);
    }

}

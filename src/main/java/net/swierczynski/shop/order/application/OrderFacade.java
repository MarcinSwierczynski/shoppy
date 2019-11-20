package net.swierczynski.shop.order.application;

import net.swierczynski.shop.common.Result;
import net.swierczynski.shop.order.domain.Order;
import net.swierczynski.shop.order.infrastructure.OrderDTO;
import net.swierczynski.shop.product.application.ProductFacade;
import net.swierczynski.shop.product.application.ProductsByIdsQuery;
import net.swierczynski.shop.product.domain.Product;

import java.time.Clock;
import java.util.List;

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
public class OrderFacade {

    private final ProductFacade productFacade;
    private final PlaceOrderService placeOrderService;
    private final ListOrderService listOrderService;

    public OrderFacade(ProductFacade productFacade, PlaceOrderService placeOrderService, ListOrderService listOrderService) {
        this.productFacade = productFacade;
        this.placeOrderService = placeOrderService;
        this.listOrderService = listOrderService;
    }

    public Result<Order> place(PlaceOrderCommand command) {
        final ProductsByIdsQuery productsByIdsQuery = new ProductsByIdsQuery(command.getProductIds());
        final List<Product> products = productFacade.findProductsByIds(productsByIdsQuery);
        return placeOrderService.placeOrder(new PlaceOrderDTO(command, products));
    }

    public List<OrderDTO> listInRange(Clock from, Clock to) {
        return listOrderService.listInRange(from, to);
    }

}

package net.swierczynski.shop.product.infrastructure


import net.swierczynski.shop.common.Result
import net.swierczynski.shop.product.application.CreateProductCommand
import net.swierczynski.shop.product.application.ProductFacade
import net.swierczynski.shop.product.application.UpdateProductCommand
import net.swierczynski.shop.product.domain.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
@WebMvcTest(ProductController.class)
class ProductControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ProductFacade productFacade

    def "should add new product"() {
        given:
            Result<Product> success = Result.success(Product.product('product name', new BigDecimal(122.23)))
            productFacade.create(new CreateProductCommand('product name', 122.23)) >> success
        when:
            def perform = mockMvc.perform(
                    post('/products')
                            .param('name', 'product name')
                            .param('price', '122.23')
            )
        then:
            perform.andExpect(status().isCreated())
        and:
            perform.andExpect(jsonPath('$.failureReason', is(nullValue())))
        and:
            perform.andExpect(jsonPath('$.products', hasSize(1)))
            perform.andExpect(jsonPath('$.products[0].id', not(nullValue())))
            perform.andExpect(jsonPath('$.products[0].name', is('product name')))
            perform.andExpect(jsonPath('$.products[0].price', is(new BigDecimal(122.23))))
    }

    def "should not add new product with a price below 0"() {
        given:
            Result<Product> failure = Result.failure('Price must be greater than 0')
            productFacade.create(new CreateProductCommand('product name', BigDecimal.ZERO - 1)) >> failure
        when:
            def perform = mockMvc.perform(
                    post('/products')
                            .param('name', 'product name')
                            .param('price', '-1')
            )
        then:
            perform.andExpect(status().isCreated())
        and:
            perform.andExpect(jsonPath('$.failureReason', is('Price must be greater than 0')))
        and:
            perform.andExpect(jsonPath('$.products', hasSize(0)))
    }

    def "should update product"() {
        given:
            Product product = Product.product('new name', new BigDecimal(22.23))
            Result<Product> success = Result.success(product)
            productFacade.update(new UpdateProductCommand(product.id(), 'new name', 22.23)) >> success
        when:
            def perform = mockMvc.perform(
                    put('/products/' + product.id().id().toString())
                            .param('name', 'new name')
                            .param('price', '22.23')
            )
        then:
            perform.andExpect(status().isAccepted())
        and:
            perform.andExpect(jsonPath('$.failureReason', is(nullValue())))
        and:
            perform.andExpect(jsonPath('$.products', hasSize(1)))
            perform.andExpect(jsonPath('$.products[0].id', not(nullValue())))
            perform.andExpect(jsonPath('$.products[0].name', is('new name')))
            perform.andExpect(jsonPath('$.products[0].price', is(new BigDecimal(22.23))))
    }

    def "should not update product if it's name is empty"() {
        given:
            Product product = Product.product('new name', new BigDecimal(22.23))
            Result<Product> failure = Result.failure('Product name must have at least one character')
            productFacade.update(new UpdateProductCommand(product.id(), ' ', 22.23)) >> failure
        when:
            def perform = mockMvc.perform(
                    put('/products/' + product.id().id().toString())
                            .param('name', ' ')
                            .param('price', '22.23')
            )
        then:
            perform.andExpect(status().isAccepted())
        and:
            perform.andExpect(jsonPath('$.failureReason', is('Product name must have at least one character')))
        and:
            perform.andExpect(jsonPath('$.products', hasSize(0)))
    }

    def "should list all products"() {
        given:
            productFacade.listAll() >> [
                    Product.product('product name', new BigDecimal(122.23)),
                    Product.product('different product name', new BigDecimal(222.23))
            ]
        when:
            def perform = mockMvc.perform(
                    get('/products')
            )
        then:
            perform.andExpect(status().isOk())
        and:
            perform.andExpect(jsonPath('$.failureReason', is(nullValue())))
        and:
            perform.andExpect(jsonPath('$.products', hasSize(2)))
        and:
            perform.andExpect(jsonPath('$.products[0].id', not(nullValue())))
            perform.andExpect(jsonPath('$.products[0].name', is('product name')))
            perform.andExpect(jsonPath('$.products[0].price', is(new BigDecimal(122.23))))
        and:
            perform.andExpect(jsonPath('$.products[1].id', not(nullValue())))
            perform.andExpect(jsonPath('$.products[1].name', is('different product name')))
            perform.andExpect(jsonPath('$.products[1].price', is(new BigDecimal(222.23))))
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        ProductFacade productFacade() {
            return detachedMockFactory.Stub(ProductFacade)
        }
    }

}
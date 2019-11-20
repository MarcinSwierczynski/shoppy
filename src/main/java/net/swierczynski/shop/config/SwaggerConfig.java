package net.swierczynski.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.ant;

/**
 * @author Marcin Świerczyński
 * @since 18/11/2019
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Products")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(ant("/products/**"))
                .build();
    }

    @Bean
    public Docket ordersApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Orders")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(ant("/orders/**"))
                .build();
    }

}
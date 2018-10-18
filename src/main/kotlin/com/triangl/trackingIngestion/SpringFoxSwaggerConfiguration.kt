package com.triangl.trackingIngestion

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.triangl.trackingIngestion"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
    }

    fun getApiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Triangl Tracking Ingestion Service Documentation")
                .description("GitHub: https://github.com/codeuniversity/triangl-tracking-ingestion-service \n Triangl: https://triangl.io")
                .build()
    }
}
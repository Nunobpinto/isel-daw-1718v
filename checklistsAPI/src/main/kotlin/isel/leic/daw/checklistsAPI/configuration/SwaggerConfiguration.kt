package isel.leic.daw.checklistsAPI.configuration

import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api() = Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(Predicates.not(PathSelectors.regex("/error")))
            .build()

    private fun apiInfo() = ApiInfoBuilder()
            .title("Cheklist API Documentation")
            .description("Spring Boot WEB API for management of Checklists")
            .contact(Contact("Bruno Filipe, Nuno Pinto, João Gameiro", "https://github.com/isel-leic-daw/1718-2-LI61D-G16", ""))
            .version("1.0")
            .build()

}

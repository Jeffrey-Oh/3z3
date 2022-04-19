package com.threedotthree.infrastructure.configuration;

import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        List<Response> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseBuilder().code("200").description(ResponseMessage.SUCCESS_MSG).build());
        responseMessages.add(new ResponseBuilder().code("400").description(ResponseMessage.BAD_REQUEST_MSG).build());
        responseMessages.add(new ResponseBuilder().code("401").description(ResponseMessage.UNAUTHORIZED_MSG).build());
        responseMessages.add(new ResponseBuilder().code("403").description(ResponseMessage.FORBIDDEN_MSG).build());
        responseMessages.add(new ResponseBuilder().code("405").description(ResponseMessage.METHOD_NOT_ALLOWED_MSG).build());
        responseMessages.add(new ResponseBuilder().code("409").description(ResponseMessage.ALREADY_DATA_MSG).build());
        responseMessages.add(new ResponseBuilder().code("417").description(ResponseMessage.EXPECTATION_FAILED_MSG).build());
        responseMessages.add(new ResponseBuilder().code("422").description(ResponseMessage.NOT_FOUND_DATA_MSG).build());
        responseMessages.add(new ResponseBuilder().code("500").description(ResponseMessage.INTERNAL_SERVER_ERROR_MSG).build());

        return new Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.threedotthree"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .globalResponses(HttpMethod.GET, responseMessages)
            .globalResponses(HttpMethod.POST, responseMessages)
            .globalResponses(HttpMethod.PUT, responseMessages)
            .globalResponses(HttpMethod.DELETE, responseMessages);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("SZS Swagger")
            .description("SZS Swagger Configuration")
            .version("1.0")
            .build();
    }
}

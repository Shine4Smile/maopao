package com.simple.maopao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义swagger文档配置
 */
@Configuration
@EnableSwagger2WebMvc
// 该注解实现只在部分环境下开启接口文档
//@Profile({"prod","test"})
public class SwaggerConfig {


    /**
     * 设置多个：
     *
     * @return
     * @Bean public Docket appApi() {
     * <p>
     * List<Parameter> pars = new ArrayList<>();
     * ParameterBuilder token = new ParameterBuilder();
     * token.name("token").description("用户令牌").modelRef(new ModelRef("string")).parameterType("header").required(false)
     * .build();
     * pars.add(token.build());
     * <p>
     * return new Docket(DocumentationType.SWAGGER_2).select().paths(regex("/app/.*")).build()
     * .globalOperationParameters(pars).apiInfo(pdaApiInfo()).useDefaultResponseMessages(false)
     * .enable(enableSwagger)
     * .groupName("appApi");
     * <p>
     * }
     * @Bean public Docket adminApi() {
     * <p>
     * List<Parameter> pars = new ArrayList<>();
     * ParameterBuilder token = new ParameterBuilder();
     * token.name("token").description("用户令牌").modelRef(new ModelRef("string")).parameterType("header").required(false)
     * .build();
     * pars.add(token.build());
     * return new Docket(DocumentationType.SWAGGER_2).select().paths(regex("/admin/.*")).build()
     * .globalOperationParameters(pars).apiInfo(pdaApiInfo()).useDefaultResponseMessages(false)
     * .enable(enableSwagger)
     * .groupName("adminApi");
     * <p>
     * }
     */

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 控制器目录
                .apis(RequestHandlerSelectors.basePackage("com.simple.maopao.controller"))
                .paths(PathSelectors.any())
                .build();
//                .globalOperationParameters(setHeaderToken());

    }

    /**
     * api信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("冒泡-伙伴匹配")
                .description("冒泡-伙伴匹配项目接口文档")
                .termsOfServiceUrl("")
                .version("1.0").build();
    }

    /**
     * @param
     * @Description: 设置swagger文档中全局参数
     * @Date: 2020/9/11 10:15
     * @return: java.util.List<springfox.documentation.service.Parameter>
     */

//    private List<Parameter> setHeaderToken() {
//        List<Parameter> pars = new ArrayList<>();
//        ParameterBuilder userId = new ParameterBuilder();
//        userId.name("token").description("用户TOKEN").modelRef(new ModelRef("string")).parameterType("header")
//                .required(true).build();
//        pars.add(userId.build());
//        return pars;
//    }
}

package com.example.mediasoftjavaeecityguide.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        description = "${operations.default.description.${locale}}"
)
@RequestMapping
public @interface SpringSwaggerEndpoint {

    /**
     * See: {@link RequestMapping#method()}
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "method")
    RequestMethod[] method() default {};

    /**
     * See: {@link RequestMapping#path()}
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] path() default {};

    /**
     * See: {@link RequestMapping#consumes()}
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "consumes")
    String[] consumes() default {};

    /**
     * See: {@link Operation#description()}
     */
    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "${operations.default.description.${locale}}";

}

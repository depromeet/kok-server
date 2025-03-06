package com.kok.kokapi.config.annotion;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestController
@RequestMapping("/v1/api")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface V1Controller {
}

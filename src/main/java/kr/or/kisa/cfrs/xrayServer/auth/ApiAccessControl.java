package kr.or.kisa.cfrs.xrayServer.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAccessControl {
    ApiType value();
}

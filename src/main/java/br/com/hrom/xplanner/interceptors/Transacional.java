package br.com.hrom.xplanner.interceptors;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * Esta anota��o Indica um Interceptador que abre e fecha uma transa��o JPA
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@InterceptorBinding
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Transacional {

}

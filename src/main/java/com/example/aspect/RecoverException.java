package com.example.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})

public @interface RecoverException {
    Class<? extends RuntimeException>[] noRecoverFor() default {
            //for checkInList()
//        IllegalArgumentException.class, ArithmeticException.class

            // for checkAssign()
            RuntimeException.class
    };
}

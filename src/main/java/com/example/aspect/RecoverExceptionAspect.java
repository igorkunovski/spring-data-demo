package com.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class RecoverExceptionAspect {

    @Around("@annotation(com.example.aspect.RecoverException)")
    public Object handleRecoverException(ProceedingJoinPoint joinPoint){

        log.error("enter RecoverExceptionAspect");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RecoverException recoverException = method.getAnnotation(RecoverException.class);

        try {
            return joinPoint.proceed();

        }catch (Exception rte) {
//            return checkInList(rte, recoverException);
            return checkAssign(rte, recoverException);
        } catch (Throwable e) {
            log.error("Was ERROR");
            throw new RuntimeException();
        }
    }

    /**
     *
     * @param rte - caught exception
     * @param recoverException - Annotation with exceptions list
     * @return - Object value depending on caught exception
     */
    private Object checkInList(Exception rte, RecoverException recoverException) {

        if (Arrays.asList(recoverException.noRecoverFor()).contains(rte.getClass())){
            log.info("Caught exception is in the list. Re-Throwing ->> " + rte.getClass().getName());
            return rte;

        }else {
            log.warn("Caught Exception is not in the list ->> " + rte.getClass().getName());
            return getObjectToReturn(rte);
        }
    }

    /**
     *
     * @param rte - caught exception
     * @param recoverException - Annotation with exceptions in the list
     * @return - Object value depending on caught exception
     */
    private Object checkAssign(Exception rte, RecoverException recoverException) {

        for (Class<? extends Exception> exc : (recoverException.noRecoverFor())) {
            if (exc.isAssignableFrom(rte.getClass())) {
                log.info("Caught exception IS assignable with the Exceptions in the list. Re-Throwing ->> " + rte.getClass().getName());
                return rte;

            }
        }
        log.warn("Caught Exception is NOT assignable with the Exceptions in the list ->> " + rte.getClass().getName());
        return getObjectToReturn(rte);
    }

    /**
     *
     * @param rte caught exception
     * @return - required Object
     */
    private Object getObjectToReturn(Exception rte) {
        log.error("returning null");
        return null;
    }
}

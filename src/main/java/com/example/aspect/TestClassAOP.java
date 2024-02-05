package com.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

import static java.lang.Thread.sleep;

@Slf4j
@Component
public class TestClassAOP {

    @Timer
    public String method1(String str) throws InterruptedException {
        sleep(100);
        return (str+str).toLowerCase();
    }

    /**
     *
     * @return checks ERROR
     * @throws OutOfMemoryError
     */
    @RecoverException
    public String method2(){
        log.error("enter method 2");
        throw new OutOfMemoryError("RTE method 2");
    }

//    /**
//     *
////     * @return checks assigned
////     * @throws IllegalArgumentException
////     */
//    @RecoverException
//    public String method3(){
//        log.error("enter method 3");
//        throw new IllegalArgumentException("RTE method 3");
//    }

    /**
     *
     * @return Checks not assigned
     * @throws FileNotFoundException
     */
    @RecoverException
    public String method4() throws FileNotFoundException {
        log.error("enter method 4");
        throw new FileNotFoundException("RTE method 4");
    }
}

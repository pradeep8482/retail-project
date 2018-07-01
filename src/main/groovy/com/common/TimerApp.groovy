package com.common

import groovy.util.logging.Slf4j

@Slf4j
class TimerApp {

    static <T> T execute(Map logValues,Closure closure){
        long start = System.nanoTime()
        Exception thrown = null
        T result = null
        try{
            result =   closure.call()
        }catch(Exception e){
            thrown = e
            throw e
        }finally{
            long duration = System.nanoTime() - start
            Long millis = Math.round(duration/1000000l)
            StringBuilder sb = new StringBuilder("elapsedMillis ${millis} ")
            logValues.each { logKey, logValue ->
                sb.append("${logKey}=${logValue};")
            }
            log.info(sb.toString())
        }


    }
}

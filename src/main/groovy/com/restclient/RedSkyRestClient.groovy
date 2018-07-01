package com.restclient

import com.exception.ErrorDetail
import com.exception.RedSkyException
import groovy.util.logging.Slf4j
import org.apache.http.impl.NoConnectionReuseStrategy
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
@Slf4j
class RedSkyRestClient {

    @Autowired
    RestTemplate redskyClient

    public static void main(String[] strings){
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
                .setMaxConnPerRoute(5)
                .setMaxConnTotal(5)
                .disableAutomaticRetries()
                .useSystemProperties()
                .setDefaultCookieStore(new BasicCookieStore())
                .setSSLContext(null)
                .build()

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient: httpClient,connectTimeout: 10000,readTimeout: 5000)
        RestTemplate redskyClient = new RestTemplate(requestFactory: httpComponentsClientHttpRequestFactory)
        ResponseEntity<Map> responseEntity = null
        String xRequest = 'retail-request-id'
        String url = "http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
        try {
            responseEntity = redskyClient.exchange(url, HttpMethod.GET, new HttpEntity(getHeader(xRequest)),Map)
        } catch (HttpClientErrorException hcep) {
            log.error("Redsky response with ${hcep.statusCode} url=${url}")
            hcep.printStackTrace()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    ResponseEntity<Map> getProductDetail(String productId) {
        ResponseEntity<Map> responseEntity = null
        String xRequest = 'retail-request-id'
        String url = "http://redsky.target.com/v2/pdp/tcin/${productId}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
        try {
            responseEntity = redskyClient.exchange(url, HttpMethod.GET, new HttpEntity(getHeader(xRequest)),Map)
        } catch (HttpClientErrorException hcep) {
            log.error("Redsky response with ${hcep.statusCode} url=${url}")
            throw new RedSkyException(new ErrorDetail(errorCode: 'NO_CONTENT', userMessage: 'Invalid RedSky Response'))
        } catch (Exception e) {
            log.error('Invalid Redsky Response: '+e.getMessage())
            throw new RedSkyException(new ErrorDetail(errorCode: 'NO_CONTENT', userMessage: 'Invalid RedSky Response'))
        }

        responseEntity
    }

    protected static HttpHeaders getHeader(String value) {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add('X-REQUEST-ID', value)
        httpHeaders
    }
}

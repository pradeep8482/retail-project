package com.service

import com.exception.ErrorDetail
import com.exception.RedSkyException
import com.restclient.RedSkyRestClient
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
@Slf4j
class RedskyProductService {

    @Autowired
    RedSkyRestClient redSkyRestClient

    /**
     * This method takes product id and makes rest call to red sky and get Response Entity
     * @param productId
     * @return ResponseEntity
     */
     ResponseEntity getProductDetail(String productId){
         ResponseEntity responseEntity = null

          responseEntity = redSkyRestClient.getProductDetail(productId)
             if((!responseEntity) || (responseEntity?.statusCode != HttpStatus.OK) ){
                 log.error("Invalid RedSky Response")
                 throw new RedSkyException(new ErrorDetail(errorCode: HttpStatus.NO_CONTENT, userMessage: 'Invalid RedSky Response'))
             }

         responseEntity
     }

     String getProductName(String id){
         getProductDetail(id)?.body?.product?.item?.product_description?.title
     }
}

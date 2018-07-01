package com.config

import com.controller.ProductController
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.stereotype.Component

@Component
class JerseyConfig extends ResourceConfig{

    public JerseyConfig(){
        register(ProductController.class)
    }
}

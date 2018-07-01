package com.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ProductData {

    @JsonProperty(value = 'id')
    String productId

    @JsonProperty(value = 'name')
    String productName

    ProductPrice productPrice
}

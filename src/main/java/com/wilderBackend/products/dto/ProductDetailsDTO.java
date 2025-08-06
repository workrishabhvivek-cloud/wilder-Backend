package com.wilderBackend.products.dto;

import lombok.Data;

@Data
public class ProductDetailsDTO {

    private long productId;
    private String productName;
    private String productTitle;
    private String productDescription;
    private String productCategory;
    private double price;
    private int stockQuantity;
    private String supplierName;
    private String supplierContact;
    private String productImageUrl;

    // Additional fields can be added as needed

}

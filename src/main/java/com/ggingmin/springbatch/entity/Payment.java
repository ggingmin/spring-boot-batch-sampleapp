package com.ggingmin.springbatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Payment {

    private Integer paymentId;
    private Integer customerId;
    private String customerName;
    private String prodName;
    private Double prodPrice;

    private Double paymentDisc;
}

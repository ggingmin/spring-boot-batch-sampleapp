package com.ggingmin.springbatch.processor;

import com.ggingmin.springbatch.entity.Payment;
import org.springframework.batch.item.ItemProcessor;

public class PaymentProcessor implements ItemProcessor<Payment, Payment> {

    @Override
    public Payment process(Payment item) throws Exception {
        double price = item.getProdPrice();
        if (item.getCustomerName().equals("SANGJUN LIM")) {
            item.setPaymentDisc(price * 80/100.0);
        } else {
            item.setPaymentDisc(price);
        }
        return item;
    }
}

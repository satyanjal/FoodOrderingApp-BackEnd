package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    /**
     *
     * @return list of all payment methods
     */

    public List<PaymentEntity> getAllPaymentMethods() {
        return paymentDao.getAllPaymentMethods();

    }

    /**
     *
     * @param paymentUUID - UUID of the payment method
     * @return payment
     * @throws PaymentMethodNotFoundException - Handle undefined payment method
     */
    public PaymentEntity getPaymentByUUID(String paymentUUID)
            throws PaymentMethodNotFoundException {
        PaymentEntity payment = paymentDao.getMethodbyId(paymentUUID);
        if (payment != null) {
            return payment;
        }
        throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
    }
}

package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public CouponEntity getCouponByName(String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        if(couponName==null)
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");

        CouponEntity couponEntity = orderDao.getCouponByName(couponName);

        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        return couponEntity;
    }
}

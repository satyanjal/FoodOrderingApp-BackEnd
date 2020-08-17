package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    @Autowired
    private CustomerService customerService;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByName(String couponName, final String authorizationToken) throws AuthorizationFailedException, CouponNotFoundException {
        CustomerEntity customerEntity = customerService.getCustomer(authorizationToken);

        if(couponName==null)
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");

        CouponEntity couponEntity = orderDao.getCouponByName(couponName);

        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        return couponEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderItemEntity> getOrderItemsByOrderId(Long orderId) throws AuthorizationFailedException {
        return orderDao.getOrderItemsByOrderId(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrdersEntity> getOrdersByCustomerId(String authorizationToken) throws AuthorizationFailedException {
        CustomerEntity customerEntity = customerService.getCustomer(authorizationToken);
        return orderDao.getOrdersByCustomerId(customerEntity.getUuid());
    }
}

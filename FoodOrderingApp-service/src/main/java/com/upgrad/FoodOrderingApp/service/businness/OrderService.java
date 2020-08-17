package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    public CouponEntity getCouponByName(String couponName, final String authorizationToken) throws AuthorizationFailedException, CouponNotFoundException {

        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt()!=null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }


        if(couponName==null)
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");

        CouponEntity couponEntity = orderDao.getCouponByName(couponName);

        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        return couponEntity;
    }

    public List<OrderItemEntity> getOrderItemsByOrderId(Long orderId) throws AuthorizationFailedException {

        List<OrderItemEntity> orderItemEntities = orderDao.getOrderItemsByOrderId(orderId);
        return orderItemEntities;

    }

    public List<OrdersEntity> getOrdersByCustomerId(String authorizationToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt()!=null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }



        List<OrdersEntity> ordersEntities = orderDao.getOrdersByCustomerId(customerAuthEntity.getCustomer().getUuid());
        return ordersEntities;

    }
}

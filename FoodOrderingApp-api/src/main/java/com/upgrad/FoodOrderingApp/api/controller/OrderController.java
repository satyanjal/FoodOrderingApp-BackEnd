package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}")
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable(value = "coupon_name", required = false) String couponName)
    throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity couponEntity = orderService.getCouponByName(couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());
        couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
        couponDetailsResponse.setPercent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);
    }

}

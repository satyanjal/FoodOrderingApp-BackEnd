package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}")
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable(value = "coupon_name", required = false) String couponName, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity couponEntity = orderService.getCouponByName(couponName, authorization);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());
        couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
        couponDetailsResponse.setPercent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/order")
    public  ResponseEntity<List<OrderList>> getOrderItemsByCustomerId(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        //@PathVariable("customer_id") String customerId

        List<OrdersEntity> ordersEntities = orderService.getOrdersByCustomerId(authorization);

        /*if(orderItemEntities.isEmpty())
            return;*/

        List<OrderList> customerOrders = new ArrayList<>();

        for (OrdersEntity ordersEntity:ordersEntities) {

            OrderList currentOrder = new OrderList();

            //Setting Address
            OrderListAddress responseAddress = new OrderListAddress();
            AddressEntity serviceAddress = new AddressEntity();
            serviceAddress = ordersEntity.getAddress();
            responseAddress.setCity(serviceAddress.getCity());
            responseAddress.setFlatBuildingName(serviceAddress.getFlatBuilNumber());
            responseAddress.setId(UUID.fromString(serviceAddress.getUuid()));
            responseAddress.setLocality(serviceAddress.getLocality());
            responseAddress.setPincode(serviceAddress.getPincode());
            OrderListAddressState responseState = new OrderListAddressState();
            responseState.setId(UUID.fromString(serviceAddress.getState().getUuid()));
            responseState.setStateName(serviceAddress.getState().getStateName());
            responseAddress.setState(responseState);
            currentOrder.setAddress(responseAddress);
            //Address field of current order finally set!

            //Setting Bill
            currentOrder.setBill(java.math.BigDecimal.valueOf(ordersEntity.getBill()));

            //Setting Coupon
            OrderListCoupon responseCoupon = new OrderListCoupon();
            CouponEntity serviceCoupon = ordersEntity.getCoupon();

            if(serviceCoupon != null) {
                responseCoupon.setCouponName(serviceCoupon.getCouponName());
                responseCoupon.setId(UUID.fromString(serviceCoupon.getUuid()));
                responseCoupon.setPercent(serviceCoupon.getPercent());
            }
            currentOrder.setCoupon(responseCoupon);
            //Coupon of current order finally set!

            //Setting Customer
            OrderListCustomer responseCustomer = new OrderListCustomer();
            CustomerEntity serviceCustomer = ordersEntity.getCustomer();
            responseCustomer.setContactNumber(serviceCustomer.getContactNumber());
            responseCustomer.setEmailAddress(serviceCustomer.getEmail());
            responseCustomer.setFirstName(serviceCustomer.getFirstName());
            responseCustomer.setId(UUID.fromString(serviceCustomer.getUuid()));
            responseCustomer.setLastName(serviceCustomer.getLastName());
            currentOrder.setCustomer(responseCustomer);
            //Customer of current order finally set!

            //Setting Date
            currentOrder.setDate(ordersEntity.getDate().toString());

            //Setting Discount
            currentOrder.setDiscount(java.math.BigDecimal.valueOf(ordersEntity.getDiscount()));

            //Setting UUID
            currentOrder.setId(UUID.fromString(ordersEntity.getUuid()));


            //Setting Item Quantity Response
            Long orderId = ordersEntity.getId();
            List<OrderItemEntity> orderItemLists = orderService.getOrderItemsByOrderId(orderId);
            List<ItemQuantityResponse> respQtys = new ArrayList<>();



            for (OrderItemEntity orderItem: orderItemLists) {

                ItemQuantityResponse iQR = new ItemQuantityResponse();
                ItemQuantityResponseItem iQRI = new ItemQuantityResponseItem();

                iQRI.setId(UUID.fromString(orderItem.getItem().getUuid()));
                iQRI.setItemName(orderItem.getItem().getItemName());
                iQRI.setItemPrice(orderItem.getItem().getPrice());

                //Converting 0/1 coming as string into integer 0/1 first and then into VEG/NON_VEG for setting response ENUM
                String vegOrNon = Integer.parseInt(orderItem.getItem().getType())==0?"VEG":"NON_VEG";
                iQRI.setType(ItemQuantityResponseItem.TypeEnum.fromValue(vegOrNon));

                iQR.setItem(iQRI);
                iQR.setPrice(orderItem.getPrice());
                iQR.setQuantity(orderItem.getQuantity());

                respQtys.add(iQR);

            }

            currentOrder.setItemQuantities(respQtys);



            //Setting Payment
            OrderListPayment responsePayment = new OrderListPayment();
            PaymentEntity servicePayment = ordersEntity.getPayment();
            responsePayment.setId(UUID.fromString(servicePayment.getUuid()));
            responsePayment.setPaymentName(servicePayment.getPaymentName());
            currentOrder.setPayment(responsePayment);

            /* *** */
            /* *** */
            //Finally adding current order details to list of customer orders, after having set all fields!
            customerOrders.add(currentOrder);
        }

        return new ResponseEntity<>(customerOrders, HttpStatus.OK);

    }

}

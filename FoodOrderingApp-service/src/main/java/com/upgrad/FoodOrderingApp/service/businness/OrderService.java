package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    ItemDao itemDao;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderItemEntity> getOrderItemsByOrderId(Long orderId) {
        return orderDao.getOrderItemsByOrderId(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrdersEntity> getOrdersByCustomerId(String authorizationToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt()!=null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        return orderDao.getOrdersByCustomerId(customerAuthEntity.getCustomer().getUuid());
    }



    public OrdersEntity saveOrder(final OrdersEntity requestOrdersEntity, final String addressId, final
    String restaurantId, final String paymentId, final String couponId, final List<OrderItemEntity> orderItemEntities, final String authorizationToken)
            throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt()!=null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        CouponEntity couponEntity = orderDao.getCouponById(couponId);
        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }

        AddressEntity addressEntity = addressDao.getAddressByUuid(addressId);
        if(addressEntity == null){
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }

        int flag =0;
        List<CustomerAddressEntity> actualAddresses = orderDao.getAddressesByCustomer(customerAuthEntity.getCustomer().getUuid());
        for (CustomerAddressEntity currentAddress: actualAddresses) {
            if(currentAddress.equals(addressEntity)){
                flag=1;
                break;
            }
        }
        if(flag == 0){
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");

        }



        PaymentEntity paymentEntity = paymentDao.getMethodbyId(paymentId);
        if(paymentEntity == null){
            throw new AddressNotFoundException("PNF-002", "No payment method found by this id");
        }

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantId);
        if(restaurantEntity == null)
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");

        //Checking whether each item requested is actually present in DB or not
        for (OrderItemEntity orderItemEntity:orderItemEntities) {

            ItemEntity testingItem = itemDao.getItemByUuid(orderItemEntity.getItem().getUuid());
            if(testingItem == null){
                throw new ItemNotFoundException("INF-003", "No item by this id exist");
            }
        }

        //Make one entry in OrdersEntity
        OrdersEntity orderToSave = new OrdersEntity();
        orderToSave.setDate(new Date());
        orderToSave.setDiscount(requestOrdersEntity.getDiscount());
        orderToSave.setBill(requestOrdersEntity.getBill());
        orderToSave.setAddress(addressEntity);
        orderToSave.setCoupon(couponEntity);
        orderToSave.setCustomer(customerAuthEntity.getCustomer());
        orderToSave.setPayment(paymentEntity);
        orderToSave.setRestaurant(restaurantEntity);
        orderToSave.setUuid(UUID.randomUUID().toString());

        OrdersEntity savedOrder = orderDao.saveOrder(orderToSave);


        //Make multiple entries ( 1 entry per item ) in OrderItemsEntity
        String orderUuid = savedOrder.getUuid();
        for (OrderItemEntity orderItemEntity:orderItemEntities) {

            OrderItemEntity toSave = new OrderItemEntity();
            ItemEntity currentItem = itemDao.getItemByUuid(orderItemEntity.getItem().getUuid());
            toSave.setPrice(orderItemEntity.getPrice());
            toSave.setQuantity(orderItemEntity.getQuantity());
            toSave.setItem(currentItem);
            toSave.setOrder(savedOrder);

            orderDao.saveOrderItem(toSave);
        }

        return savedOrder;

    }
}

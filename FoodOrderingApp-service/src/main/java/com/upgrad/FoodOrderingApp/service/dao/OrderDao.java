package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;

    public CouponEntity getCouponByName(String couponName) {
        try {
            final CouponEntity couponEntity = entityManager.createNamedQuery("getCouponByName", CouponEntity.class).
                    setParameter("couponNameNq", couponName).getSingleResult();
            return couponEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CouponEntity getCouponById(String couponId) {
        try {
            final CouponEntity couponEntity = entityManager.createNamedQuery("getCouponById", CouponEntity.class).
                    setParameter("couponIdNq", couponId).getSingleResult();
            return couponEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }



    public List<OrderItemEntity> getOrderItemsByOrderId(Long orderId){
        try {
            final List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("orderItemsByOrderId", OrderItemEntity.class).
                    setParameter("orderIdNq", orderId).getResultList();
            return orderItemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<OrdersEntity> getOrdersByCustomerId(String customerUuid){
        try {
            final List<OrdersEntity> ordersEntities = entityManager.createNamedQuery("ordersByCustomerId", OrdersEntity.class).
                    setParameter("customerUuidNq", customerUuid).getResultList();
            return ordersEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<OrdersEntity> getOrdersByRestaurantId(Long restaurantid){
        try {
            return entityManager.createNamedQuery("ordersByRestaurantId", OrdersEntity.class).
                    setParameter("restaurant_id", restaurantid).getResultList();
        } catch (NoResultException nre) {return null;}
    }

    public List<CustomerAddressEntity> getAddressesByCustomer(String customerUuid){
        try {
            return entityManager.createNamedQuery("addressesByCustomer", CustomerAddressEntity.class).
                    setParameter("customerUuidNq", customerUuid).getResultList();
        } catch (NoResultException nre) {return null;}
    }

    public OrdersEntity saveOrder(OrdersEntity ordersEntity) {
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }



}

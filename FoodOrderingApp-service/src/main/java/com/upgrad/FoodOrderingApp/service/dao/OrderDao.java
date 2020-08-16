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
}

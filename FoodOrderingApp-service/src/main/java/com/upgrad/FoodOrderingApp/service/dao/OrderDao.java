package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
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
}

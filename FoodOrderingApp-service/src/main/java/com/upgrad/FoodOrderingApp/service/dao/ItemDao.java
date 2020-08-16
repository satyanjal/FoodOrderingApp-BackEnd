package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemById(Integer id) {
        try {
            return entityManager.createNamedQuery("itemById",ItemEntity.class).setParameter("id",id).getSingleResult();
        }catch (NoResultException nre){return null;}
    }
}

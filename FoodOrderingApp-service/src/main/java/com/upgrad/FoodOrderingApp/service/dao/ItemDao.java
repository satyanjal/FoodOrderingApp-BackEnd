package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemById(Integer id) {
        try {
            return entityManager.createNamedQuery("itemById",ItemEntity.class).setParameter("id",id).getSingleResult();
        }catch (NoResultException nre){return null;}
    }

    public ItemEntity getItemByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("itemByUuid",ItemEntity.class).setParameter("uuidNq",uuid).getSingleResult();
        }catch (NoResultException nre){return null;}
    }
}

package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategoryEntity> getCategoryByRestaurantId(RestaurantEntity restaurant) {
        try {
            return entityManager.createNamedQuery("categoryByRestaurantId",RestaurantCategoryEntity.class).
                    setParameter("restaurant",restaurant).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    public List<RestaurantCategoryEntity> getRestaurantByCategory(CategoryEntity category) {
        try {
            return entityManager.createNamedQuery("restaurantByCategoryId",RestaurantCategoryEntity.class).
                    setParameter("category",category).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }


}

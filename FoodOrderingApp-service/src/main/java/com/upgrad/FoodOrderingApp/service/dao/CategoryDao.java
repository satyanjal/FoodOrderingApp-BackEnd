package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<CategoryEntity> getAllCategories() {
        TypedQuery<CategoryEntity> query = entityManager.createQuery("SELECT c from CategoryEntity c", CategoryEntity.class);
        return query.getResultList();
    }
}

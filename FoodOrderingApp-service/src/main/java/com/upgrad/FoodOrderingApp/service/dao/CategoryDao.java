package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class CategoryDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<CategoryEntity> getAllCategories() {
        TypedQuery<CategoryEntity> query = entityManager.createQuery("SELECT c from CategoryEntity c order by c.categoryName", CategoryEntity.class);
        return query.getResultList();
    }

    public List<CategoryItemEntity> getCategoryItemsById(final String categoryUuid){


        try {
            final List<CategoryItemEntity> categoryItemEntities = entityManager.createNamedQuery("categoryItemByCategoryId", CategoryItemEntity.class).
                    setParameter("givenUuid", categoryUuid).getResultList();
            return categoryItemEntities;
        } catch (NoResultException nre) {return null;}

    }




}

package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Repository
public class CategoryDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<CategoryEntity> getAllCategories() {
        TypedQuery<CategoryEntity> query = entityManager.createQuery("SELECT c from CategoryEntity c order by c.categoryName", CategoryEntity.class);
        return query.getResultList();
    }

    public CategoryEntity getCategoryById(final UUID categoryUuid){


        try {
            return entityManager.createNamedQuery("categoryByUuid", CategoryEntity.class).
                    setParameter("uuid", categoryUuid).getSingleResult();
        } catch (NoResultException nre) {return null;}

    }




}

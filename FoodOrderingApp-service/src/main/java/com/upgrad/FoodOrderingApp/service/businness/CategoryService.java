package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories(){
        return categoryDao.getAllCategories();
    }

    public List<CategoryItemEntity> getCategoryItemsById(String categoryUuid) throws CategoryNotFoundException {

        if(categoryUuid == null){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }

        List<CategoryItemEntity> categoryItemEntities = categoryDao.getCategoryItemsById(categoryUuid);

        if(categoryItemEntities.isEmpty()){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        return categoryItemEntities;
    }
}

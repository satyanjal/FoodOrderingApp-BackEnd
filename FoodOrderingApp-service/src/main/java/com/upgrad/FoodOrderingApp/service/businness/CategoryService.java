package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories(){
        return categoryDao.getAllCategories();
    }

   public CategoryEntity getCategoryById(UUID categoryUuid) throws CategoryNotFoundException {

       if(categoryUuid.equals(null)){
           throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
       }

        return categoryDao.getCategoryById(categoryUuid);

    }
}

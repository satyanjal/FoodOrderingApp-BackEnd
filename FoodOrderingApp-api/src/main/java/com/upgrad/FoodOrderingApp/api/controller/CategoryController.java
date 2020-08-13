package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    //This controller class will implement the following 2 APIs.

    /*
    1. Get All Categories - “/category”
        It should be a GET request and will not require any parameters from the user.
        It should retrieve all the categories present in the database,
         ordered by their name and display the response in a JSON format with the corresponding HTTP status.
     */

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category")
    public ResponseEntity<List<CategoryListResponse>> getAllCategories(){
        final List<CategoryEntity> categories = categoryService.getAllCategories();

        List<CategoryListResponse> categoryListResponses = new ArrayList<>();

        for (CategoryEntity category:categories) {
            CategoryListResponse response = new CategoryListResponse();
            response.setId(category.getUuid());
            response.setCategoryName(category.getCategoryName());
            categoryListResponses.add(response);
        }
        return new ResponseEntity<List<CategoryListResponse>>(categoryListResponses, HttpStatus.OK);
    }


    /*
    2. Get Category by Id - “/category/{category_id}”
        It should be a GET request.
        This endpoint must request the following value from the customer as a path variable: Category UUID - String

        If the category id field is empty,
            throw “CategoryNotFoundException” with
                the message code (CNF-001) and
                message (Category id field should not be empty)
            and their corresponding HTTP status.

        If there are no categories available by the id provided,
            throw “CategoryNotFoundException” with
                the message code (CNF-002) and
                message (No category by this id)
                and their corresponding HTTP status.

        If the category id entered by the customer matches any category in the database,
            it should retrieve that category with all items within that category and
            then display the response in a JSON format with the corresponding HTTP status.
    */
}


package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ItemService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getFivePopularItems(final String restaurantUuid,final String authorizationToken) throws AuthorizationFailedException, RestaurantNotFoundException {
        CustomerEntity customerEntity = customerService.getCustomer(authorizationToken);

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        List<OrdersEntity> ordersEntities = orderDao.getOrdersByRestaurantId(restaurantEntity.getId());

        List<Long> itemIds = new ArrayList<>();
        for (OrdersEntity ordersEntity : ordersEntities) {
            List<OrderItemEntity> orderItemEntities = orderDao.getOrderItemsByOrderId(ordersEntity.getId());
            for (OrderItemEntity orderItemEntity : orderItemEntities) {
                itemIds.add(orderItemEntity.getItem().getId());
            }
        }

        List<ItemEntity> itemEntities = new ArrayList<>();
        List<Long> singleItemsIds = getItems(itemIds, itemIds.size());

        for (int i=0;i<5;i++) {
            itemEntities.add(itemDao.getItemById(singleItemsIds.get(i).intValue()));
        }
        return itemEntities;
    }

    private static List<Long> getItems(List<Long> arr, int n){
        List<Long> temp = new ArrayList<>();
        int j = 0;
        for (int i=0; i<n-1; i++){
            if (!arr.get(i).equals(arr.get(i+1))){
                temp.set(j++, arr.get(i));
            }
        }
        temp.set(j++, arr.get(n-1));
        for (int i=0; i<j; i++){
            arr.set(i, temp.get(i));
        }
        return arr;
    }

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String uuid) {
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);
        List<RestaurantCategoryEntity> restaurantCategoryEntity = restaurantCategoryDao.getCategoryByRestaurantId(restaurantEntity);

        List<ItemEntity> itemEntities = new ArrayList<>();
        for (RestaurantCategoryEntity restaurantCategoryEntity1: restaurantCategoryEntity) {
            List<CategoryItemEntity> categoryItemEntities = categoryDao.getCategoryItemsById(restaurantCategoryEntity1.getCategory().getUuid());
            for (CategoryItemEntity categoryItemEntity : categoryItemEntities) {
                itemEntities.add(categoryItemEntity.getItem());
            }
        }
        return itemEntities;
    }
}

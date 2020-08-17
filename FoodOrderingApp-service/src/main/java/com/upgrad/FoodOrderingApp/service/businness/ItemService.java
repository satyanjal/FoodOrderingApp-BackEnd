package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

public class ItemService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getFivePopularItems(final String restaurantUuid,final String authorizationToken) throws AuthorizationFailedException, RestaurantNotFoundException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt()!=null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

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

}

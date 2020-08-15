package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class ItemService {



    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getFivePopularItems(final String restaurantUuid,final String authorizationToken) throws AuthorizationFailedException {
        //        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(authorizationToken);
//        if (userAuthEntity == null) {
//            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
//        } else if (userAuthEntity.getLogoutAt()!=null) {
//            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
//        } else if (userAuthEntity.getLogoutAt()!=null) {
//            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
//        }

//        Steps:
//        get restaurant entity
//        get orders of that restaurant from OrderEntity
//        then from the order id, get the item_id from OrderItemEntity
//        then check which item_id comes more number of times and then sort it, and then get the first 5 rows
//        get the items details from the items id from ItemEntity
//

        List<ItemEntity> itemEntities = new ArrayList<>();
        return itemEntities;
    }

}

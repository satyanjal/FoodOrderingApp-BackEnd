package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class StateService {

    @Autowired
    private StateDao stateDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates(final String authorizationToken) throws AuthorizationFailedException {
        //        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(authorizationToken);
//        if (userAuthEntity == null) {
//            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
//        } else if (userAuthEntity.getLogoutAt()!=null) {
//            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
//        } else if (userAuthEntity.getLogoutAt()!=null) {
//            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
//        }

        return stateDao.getAllStates();
    }
}

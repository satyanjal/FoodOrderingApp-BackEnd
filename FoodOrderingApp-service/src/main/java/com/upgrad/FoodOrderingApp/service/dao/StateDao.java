package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class).
                    setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {return null;}
    }

    public List<StateEntity> getAllStates() {
        TypedQuery<StateEntity> query = entityManager.createQuery("SELECT s from StateEntity s", StateEntity.class);
        return query.getResultList();
    }
}

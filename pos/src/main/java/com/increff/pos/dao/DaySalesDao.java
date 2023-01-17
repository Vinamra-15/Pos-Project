package com.increff.pos.dao;

import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class DaySalesDao extends AbstractDao{
    private static String select_all = "select p from DaySalesPojo p";
    @PersistenceContext
    private EntityManager em;
    public void insert(DaySalesPojo daySalesPojo){
        em.persist(daySalesPojo);
    }

    public List<DaySalesPojo> selectDaySales() {
        TypedQuery<DaySalesPojo> query = getQuery(select_all, DaySalesPojo.class);
        return query.getResultList();
    }
}

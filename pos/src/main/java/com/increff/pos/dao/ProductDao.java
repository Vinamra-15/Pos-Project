package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
@Transactional
public class ProductDao extends AbstractDao {
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_all = "select p from ProductPojo p";
    private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
    @PersistenceContext
    private EntityManager em;
    public void insert(ProductPojo p) {
        em.persist(p);
    }

    public ProductPojo select(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }


    public void update(ProductPojo p) {
        em.merge(p);
    }

    public ProductPojo selectByBarcode(String barcode){
        TypedQuery<ProductPojo> query = getQuery(select_barcode,ProductPojo.class);
        query.setParameter("barcode",barcode);
        return getSingle(query);
    }

}

package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.BrandCategoryPojo;

@Repository
@Transactional
public class BrandCategoryDao extends AbstractDao {

	private static String select_id = "select p from BrandCategoryPojo p where id=:id";
	private static String select_all = "select p from BrandCategoryPojo p";
	private static String select_brand_category = "select p from BrandCategoryPojo p where brand=:brand and category=:category";
	private static String select_brand = "select p from BrandCategoryPojo p where brand=:brand";
	private static String select_category = "select p from BrandCategoryPojo p where category=:category";

	@PersistenceContext
	private EntityManager em;

	public void insert(BrandCategoryPojo p) {
		em.persist(p);
	}

	public BrandCategoryPojo select(Integer id) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_id, BrandCategoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandCategoryPojo> selectAll() {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_all, BrandCategoryPojo.class);
		return query.getResultList();
	}

	public List<BrandCategoryPojo> selectByBrand(String brand) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brand, BrandCategoryPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}

	public List<BrandCategoryPojo> selectByCategory(String category) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_category, BrandCategoryPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}
	public void update(BrandCategoryPojo p) {
	}

	public BrandCategoryPojo select(String brand, String category) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brand_category, BrandCategoryPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}







}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.dao;

import br.com.jpb.util.JpaUtil;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Named
@Singleton
public class GenericDao {

	public static final int MAX_LIMIT_IN_CLAUSE = 500;

	@PersistenceContext
	private EntityManager em;

	public <T> T merge(T entity) {
		return em.merge(entity);
	}

	public void persist(Object entity) {
		em.persist(entity);
	}

	public <T> T refresh(T entity) {
		em.refresh(entity);
		return entity;
	}

	public <T> T findById(Class<T> who, Object id) {
		return em.find(who, id);
	}

	public <T> T findByUniqueIndex(Class<T> who, String colName, Object colValue, boolean cacheable) {
		String name = who.getSimpleName();
		StringBuilder query = new StringBuilder();
		query.append("SELECT c ");
		query.append("FROM ");
		query.append(name);
		query.append(" c ");
		query.append(" WHERE ");
		query.append(colName);
		query.append(" = :value");
		TypedQuery<T> q = em.createQuery(query.toString(), who);
		q.setParameter("value", colValue);
		q.setHint("org.hibernate.cacheable", cacheable);
		return JpaUtil.uniqueResultOrNull(q);
	}

	public <T> List<T> findAll(Class<T> who, boolean cacheable) {
		String name = who.getName();
		StringBuilder query = new StringBuilder();
		query.append("FROM ");
		query.append(name);
		query.append(" WHERE 1 = 1");
		TypedQuery<T> q = em.createQuery(query.toString(), who);
		q.setHint("org.hibernate.cacheable", cacheable);
		return q.getResultList();
	}

	public <T> List<T> findAllOrdered(Class<T> who, String order, boolean cacheable) {
		String name = who.getName();
		StringBuilder query = new StringBuilder();
		query.append("FROM ");
		query.append(name);
		query.append(" WHERE 1 = 1");
		query.append(" ORDER BY ");
		query.append(order);
		TypedQuery<T> q = em.createQuery(query.toString(), who);
		q.setHint("org.hibernate.cacheable", cacheable);
		return q.getResultList();
	}

	public void remove(Object entity) {
		em.remove(entity);
	}

	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return em.getReference(entityClass, primaryKey);
	}

	public void truncateTable(String entityName) {
		em.createQuery("DELETE FROM " + entityName).executeUpdate();
	}

	public EntityManager getEm() {
		return em;
	}

	/**
	 * Used for tests
	 */
	public void flush() {
		em.flush();
	}
}
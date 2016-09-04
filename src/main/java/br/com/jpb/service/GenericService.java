package br.com.jpb.service;

import br.com.jpb.model.BaseEntity;
import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class GenericService<T extends BaseEntity> implements Serializable {

	private static final int IN_PARTS_SIZE = 500;

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	private final Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
			.getActualTypeArguments()[0];

	protected Class<T> getType() {
		return this.type;
	}

	protected EntityManager getEntityManager() {
		return this.entityManager;
	}

	public T findById(long id) {
		return entityManager.find(this.type, id);
	}

	public T refresh(T obj) {
		entityManager.refresh(obj);
		return obj;
	}

	@Transactional
	public void persist(T obj) {
		entityManager.persist(obj);
	}

	@Transactional
	public T merge(T obj) {
		return entityManager.merge(obj);
	}

	@Transactional
	public T save(T obj) {
		if (obj.getId() == null) {
			this.persist(obj);
		} else {
			obj = this.merge(obj);
		}

		return obj;
	}

	public T getReference(long id) {
		return entityManager.getReference(this.type, id);
	}

	@Transactional
	public void remove(long id) {
		entityManager.remove(getReference(id));
	}

	@Transactional
	public void remove(T obj) {
		this.remove(obj.getId());
	}

	protected JPAQuery<T> createJPAQuery() {
		return new JPAQuery<>(entityManager);
	}

	protected JPAQuery<T> createJPAQuery(String cacheRegion) {
		JPAQuery<T> query = createJPAQuery();
		if (cacheRegion != null) {
			query.setHint(QueryHints.CACHEABLE, true);
			query.setHint(QueryHints.CACHE_REGION, cacheRegion);
		}
		return query;
	}

	protected <U> JPAQuery<U> createJPAQuery(Class<U> u) {
		return new JPAQuery<>(entityManager);
	}

	protected <U> JPAQuery<U> createJPAQuery(Class<U> u, String cacheRegion) {
		JPAQuery<U> query = createJPAQuery(u);
		if (cacheRegion != null) {
			query.setHint(QueryHints.CACHEABLE, true);
			query.setHint(QueryHints.CACHE_REGION, cacheRegion);
		}
		return query;
	}

	//	protected JPAQuery<T> createJPAQuery() {
	//		return this.createJPAQuery((String) null);
	//	}
	//
	//	protected JPAQuery<T> createJPAQuery(String cacheRegion) {
	//		String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, this.type.getSimpleName());
	//		EntityPathBase<T> path = new EntityPathBase<>(this.type, name);
	//		return this.createJPAQuery(path, cacheRegion);
	//	}
	//
	//	protected <R> JPAQuery<R> createJPAQuery(EntityPath<R> path) {
	//		return this.createJPAQuery(path, (String) null);
	//	}
	//
	//	protected <R> JPAQuery<R> createJPAQuery(EntityPath<R> path, String cacheRegion) {
	//		new JPAQuery<>()
	//		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(this.getEntityManager());
	//		JPAQuery query = jpaQueryFactory.selectFrom(path);
	//		if (cacheRegion != null) {
	//			query.setHint(QueryHints.CACHEABLE, true);
	//			query.setHint(QueryHints.CACHE_REGION, cacheRegion);
	//		}
	//
	//		return query;
	//	}

	protected void flush() {
		entityManager.flush();
	}

	protected void clear() {
		entityManager.clear();
	}

	public Session getSession() {
		return (Session) getEntityManager().getDelegate();
	}

	public List<T> findAll() {
		return this.createJPAQuery().fetch();
	}

	public long countAll() {
		return this.createJPAQuery().fetchCount();
	}

	@Transactional
	public List<T> saveAll(Iterable<T> objs) {
		final List<T> saved = new ArrayList<>();
		for (T t : objs) {
			saved.add(save(t));
		}
		return saved;
	}

	@Transactional
	public void removeAll(Iterable<T> objs) {
		for (final T obj : objs) {
			remove(obj);
		}
	}

	/**
	 * Split a Query with a IN clause. Avoid a high number of parameters in a single IN.
	 *
	 * @param query           Typed
	 * @param inParams        all Params
	 * @param inParameterName name of the in Param of the query
	 * @param <T>             the return Type
	 * @param <P>             the param Type
	 * @return List of Type
	 */
	public <T, P> List<T> splitQuery(TypedQuery<T> query, Collection<P> inParams, String inParameterName) {
		if (inParams.isEmpty()) {
			return Collections.emptyList();
		}
		List<P> asLists = new ArrayList<>(inParams);
		List<List<P>> parts = Lists.partition(asLists, IN_PARTS_SIZE);
		List<T> ts = new ArrayList<>();
		for (List<P> part : parts) {
			ts.addAll(query.setParameter(inParameterName, part).getResultList());
		}
		return ts;
	}
}

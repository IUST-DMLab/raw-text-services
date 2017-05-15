package ir.ac.iust.dml.kg.raw.services.access.repositories;

import ir.ac.iust.dml.kg.raw.services.access.entities.Occurrence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class OccurrenceRepositoryImpl implements OccurrenceRepositoryCustom {

  @Autowired
  private MongoTemplate op;

  @Override
  public Page<Occurrence> search(int page, int pageSize, String predicate, Integer minOccurrence, Boolean approved) {
    Query query = new Query();
    if (predicate != null) query.addCriteria(Criteria.where("predicate").regex(predicate));
    if (minOccurrence != null) query.addCriteria(Criteria.where("occurrence").gte(minOccurrence));
    if (approved != null) query.addCriteria(Criteria.where("approved").is(approved));
    return page(op, query, page, pageSize, Occurrence.class);
  }

  public static <T> Page<T> page(MongoTemplate op, Query query, int page, int pageSize, Class<T> entityClass) {
    final long total = op.count(query, entityClass);
    final PageRequest p = new PageRequest(page, pageSize);
    query.with(p);
    final List<T> list = op.find(query, entityClass);
    return new PageImpl<>(list, p, total);
  }
}

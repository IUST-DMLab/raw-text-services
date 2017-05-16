package ir.ac.iust.dml.kg.raw.services.access.repositories;

import ir.ac.iust.dml.kg.raw.services.access.entities.Rule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository extends MongoRepository<Rule, ObjectId> {

}

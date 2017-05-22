package ir.ac.iust.dml.kg.raw.services.logic;

import ir.ac.iust.dml.kg.raw.services.access.entities.Occurrence;
import ir.ac.iust.dml.kg.raw.services.access.entities.User;
import ir.ac.iust.dml.kg.raw.services.access.repositories.OccurrenceRepository;
import ir.ac.iust.dml.kg.raw.services.access.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserLogic {

  @Autowired
  private UserRepository db;
  @Autowired
  private OccurrenceRepository occurrenceDao;

  @PostConstruct
  public void createUsers() {
    final List<User> users = db.findAll();
    if (users.isEmpty()) {
      db.save(new User("asgari"));
      db.save(new User("hemmatan"));
      db.save(new User("alizadeh"));
      db.save(new User("abdous"));
      db.save(new User("sajadi"));
      db.save(new User("hadian"));
      db.save(new User("khaledi"));
      db.save(new User("damirchi"));
      db.save(new User("oskooee"));
      db.save(new User("mahdavi"));
      db.save(new User("mahdizadeh"));
      db.save(new User("shahshahani"));
      db.save(new User("rahimi"));
    }
  }

  public List<User> findAll() {
    return db.findAll();
  }

  public int assign(String username, String predicate, int count) {
    final User user = db.findByUsername(username);
    if (user == null) return 0;
    final Page<Occurrence> occurrences = occurrenceDao.search(0, count, predicate,
        null, null, false, null);
    occurrences.forEach(it -> {
          it.setAssignee(user);
          occurrenceDao.save(occurrences);
        }
    );
    return occurrences.getNumberOfElements();
  }

  public User getUser(String username) {
    return db.findByUsername(username);
  }
}

package ir.ac.iust.dml.kg.raw.services.web.rest;

import edu.stanford.nlp.pipeline.Annotation;
import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.raw.SentenceTokenizer;
import ir.ac.iust.dml.kg.raw.TextProcess;
import ir.ac.iust.dml.kg.raw.rulebased.ExtractTriple;
import ir.ac.iust.dml.kg.raw.rulebased.Triple;
import ir.ac.iust.dml.kg.raw.services.access.entities.DependencyPattern;
import ir.ac.iust.dml.kg.raw.services.access.entities.Occurrence;
import ir.ac.iust.dml.kg.raw.services.access.entities.Rule;
import ir.ac.iust.dml.kg.raw.services.access.entities.User;
import ir.ac.iust.dml.kg.raw.services.access.repositories.RuleRepository;
import ir.ac.iust.dml.kg.raw.services.logic.OccurrenceLogic;
import ir.ac.iust.dml.kg.raw.services.logic.UserLogic;
import ir.ac.iust.dml.kg.raw.services.logic.data.AssigneeData;
import ir.ac.iust.dml.kg.raw.services.logic.data.OccurrenceSearchResult;
import ir.ac.iust.dml.kg.raw.services.logic.data.PredicateData;
import ir.ac.iust.dml.kg.raw.services.tree.ParsedWord;
import ir.ac.iust.dml.kg.raw.services.tree.ParsingLogic;
import ir.ac.iust.dml.kg.raw.services.web.rest.data.RuleTestData;
import ir.ac.iust.dml.kg.raw.services.web.rest.data.TextBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/raw/")
@Api(tags = "raw", description = "سرویس‌های متن خام")
public class RawTextRestServices {

  @Autowired
  private OccurrenceLogic occurrenceLogic;
  @Autowired
  private UserLogic userLogic;
  @Autowired
  private ParsingLogic parsingLogic;
  @Autowired
  private RuleRepository ruleDao;

  private final TextProcess tp = new TextProcess();

  @RequestMapping(value = "/searchPattern", method = RequestMethod.GET)
  @ResponseBody
  public Page<DependencyPattern> searchPattern(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize) throws Exception {
    return parsingLogic.searchPattern(page, pageSize);
  }

  @RequestMapping(value = "/savePattern", method = RequestMethod.POST)
  @ResponseBody
  public DependencyPattern savePattern(@RequestBody DependencyPattern data) throws Exception {
    if (data == null) return null;
    return parsingLogic.save(data);
  }

  @RequestMapping(value = "/dependencyParsePost", method = RequestMethod.POST)
  @ResponseBody
  public List<ParsedWord> dependencyParsePost(@RequestBody TextBucket textBucket) throws Exception {
    if (textBucket.getText() == null) return null;
    return parsingLogic.dependencySentence(textBucket.getText());
  }

  @RequestMapping(value = "/dependencyParseGet", method = RequestMethod.POST)
  @ResponseBody
  public List<ParsedWord> dependencyParseGet(@RequestParam String text) throws Exception {
    return parsingLogic.dependencySentence(text);
  }

  @RequestMapping(value = "/approve", method = RequestMethod.GET)
  @ResponseBody
  public Occurrence edit(@RequestParam String id,
                         @RequestParam(required = false) Boolean approved) throws Exception {
    final Occurrence e = occurrenceLogic.findOne(id);
    if (e == null) return null;
    e.setApproved(approved);
    occurrenceLogic.save(e);
    return e;
  }

  @RequestMapping(value = "/export", method = RequestMethod.GET)
  @ResponseBody
  public List<Occurrence> export() throws Exception {
    return occurrenceLogic.export().getContent();
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @ResponseBody
  public OccurrenceSearchResult search(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String predicate,
      @RequestParam(required = false, defaultValue = "true") boolean like,
      @RequestParam(required = false) Integer minOccurrence,
      @RequestParam(required = false) Boolean approved,
      @RequestParam(required = false) String assigneeUsername
  ) throws Exception {
    return occurrenceLogic.search(page, pageSize, predicate, like, minOccurrence, approved, assigneeUsername);
  }

  @RequestMapping(value = "/predicates", method = RequestMethod.GET)
  @ResponseBody
  public Page<PredicateData> predicates(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String predicate,
      @RequestParam(required = false, defaultValue = "false") boolean fillAssignees
  ) throws Exception {
    return occurrenceLogic.predicates(page, pageSize, predicate, fillAssignees);
  }

  @RequestMapping(value = "/assigneeCount", method = RequestMethod.GET)
  @ResponseBody
  public List<AssigneeData> assigneeCount(
      @RequestParam(required = false) String predicate
  ) throws Exception {
    return occurrenceLogic.assigneeCount(predicate);
  }

  @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
  @ResponseBody
  public List<User> listUsers() throws Exception {
    return userLogic.findAll();
  }

  @RequestMapping(value = "/assign", method = RequestMethod.GET)
  @ResponseBody
  public int assign(
      @RequestParam String username,
      @RequestParam(required = false) String predicate,
      @RequestParam int count
  ) throws Exception {
    return userLogic.assign(username, predicate, count);
  }

  @RequestMapping(value = "/rules", method = RequestMethod.GET)
  @ResponseBody
  public Page<Rule> rules(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize) throws Exception {
    return ruleDao.findAll(new PageRequest(page, pageSize));
  }

  @RequestMapping(value = "/editRule", method = RequestMethod.GET)
  @ResponseBody
  public Rule editRule(
      @RequestParam(required = false) String id,
      @RequestParam String rule,
      @RequestParam String predicate,
      @RequestParam boolean approved) throws Exception {
    Rule e = (id == null) ? null : ruleDao.findOne(new ObjectId(id));
    if (e == null) e = new Rule();
    e.setRule(rule);
    e.setPredicate(predicate);
    e.setApproved(approved);
    ruleDao.save(e);
    return e;
  }

  @RequestMapping(value = "/removeRule", method = RequestMethod.GET)
  @ResponseBody
  public Rule removeRule(@RequestParam String id) throws Exception {
    final Rule e = ruleDao.findOne(new ObjectId(id));
    if (e == null) return null;
    ruleDao.delete(e);
    return e;
  }

  @RequestMapping(value = "/ruleTest", method = RequestMethod.POST)
  @ResponseBody
  public List<Triple> ruleTest(@RequestBody RuleTestData data) throws Exception {
    List<Triple> result = new ArrayList<>();
    ExtractTriple extractTriple = new ExtractTriple(data.getRules());
    final List<String> lines = SentenceTokenizer.SentenceSplitterRaw(data.getText());
    for (String line : lines) {
      Annotation annotation = new Annotation(line);
      tp.preProcess(annotation);
      result.addAll(extractTriple.extractTripleFromAnnotation(annotation));
    }
    return result;
  }
}

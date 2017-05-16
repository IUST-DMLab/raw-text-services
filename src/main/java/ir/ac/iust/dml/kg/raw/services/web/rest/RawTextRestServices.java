package ir.ac.iust.dml.kg.raw.services.web.rest;

import edu.stanford.nlp.pipeline.Annotation;
import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.raw.SentenceTokenizer;
import ir.ac.iust.dml.kg.raw.TextProcess;
import ir.ac.iust.dml.kg.raw.rulebased.ExtractTriple;
import ir.ac.iust.dml.kg.raw.rulebased.Triple;
import ir.ac.iust.dml.kg.raw.services.access.entities.Occurrence;
import ir.ac.iust.dml.kg.raw.services.access.entities.Rule;
import ir.ac.iust.dml.kg.raw.services.access.repositories.OccurrenceRepository;
import ir.ac.iust.dml.kg.raw.services.access.repositories.RuleRepository;
import ir.ac.iust.dml.kg.raw.services.web.rest.data.RuleTestData;
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
  private OccurrenceRepository occurrenceDao;
  @Autowired
  private RuleRepository ruleDao;
  private final TextProcess tp = new TextProcess();

  @RequestMapping(value = "/approve", method = RequestMethod.GET)
  @ResponseBody
  public Occurrence edit(@RequestParam String id,
                         @RequestParam(required = false) Boolean approved) throws Exception {
    final Occurrence e = occurrenceDao.findOne(new ObjectId(id));
    if (e == null) return null;
    e.setApproved(approved);
    occurrenceDao.save(e);
    return e;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @ResponseBody
  public Page<Occurrence> search(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String predicate,
      @RequestParam(required = false) Integer minOccurrence,
      @RequestParam(required = false) Boolean approved
  ) throws Exception {
    return occurrenceDao.search(page, pageSize, predicate, minOccurrence, approved);
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
      @RequestParam String id,
      @RequestParam String rule,
      @RequestParam boolean approved) throws Exception {
    Rule e = (id == null) ? null : ruleDao.findOne(new ObjectId(id));
    if (e == null) e = new Rule();
    e.setRule(rule);
    e.setApproved(approved);
    ruleDao.save(e);
    return e;
  }

  @RequestMapping(value = "/ruleTest", method = RequestMethod.POST)
  @ResponseBody
  public List<Triple> ruleTest(@RequestBody RuleTestData data) throws Exception {
    List<Triple> result = new ArrayList<>();
    ExtractTriple extractTriple = new ExtractTriple(data.getRules(), data.getPredicates());
    final List<String> lines = SentenceTokenizer.SentenceSplitterRaw(data.getText());
    for (String line : lines) {
      Annotation annotation = new Annotation(line);
      tp.preProcess(annotation);
      result.addAll(extractTriple.extractTripleFromAnnotation(annotation));
    }
    return result;
  }
}

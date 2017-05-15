package ir.ac.iust.dml.kg.raw.services.web.rest;

import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.raw.services.access.entities.Occurrence;
import ir.ac.iust.dml.kg.raw.services.access.repositories.OccurrenceRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/raw/")
@Api(tags = "raw", description = "سرویس‌های متن خام")
public class EntityEvaluationRestServices {

  @Autowired
  private OccurrenceRepository dao;

  @RequestMapping(value = "/approve", method = RequestMethod.GET)
  @ResponseBody
  public Occurrence edit(@RequestParam String id,
                         @RequestParam(required = false) Boolean approved) throws Exception {
    final Occurrence e = dao.findOne(new ObjectId(id));
    if (e == null) return null;
    e.setApproved(approved);
    dao.save(e);
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
    return dao.search(page, pageSize, predicate, minOccurrence, approved);
  }
}

package ir.ac.iust.dml.kg.raw.services.logic;

import ir.ac.iust.dml.kg.raw.extractor.EnhancedEntityExtractor;
import ir.ac.iust.dml.kg.raw.extractor.ResolvedEntityToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FKGfyLogic {
  private EnhancedEntityExtractor extractor;

  public List<List<ResolvedEntityToken>> fkgFy(String text) {
    if (extractor == null) extractor = new EnhancedEntityExtractor();
    final List<List<ResolvedEntityToken>> resolved = extractor.extract(text);
    extractor.disambiguateByContext(resolved, 0);
    extractor.resolveByName(resolved);
    extractor.resolvePronouns(resolved);
    return resolved;
  }
}

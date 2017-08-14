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
    return extractor.extract(text);
  }
}

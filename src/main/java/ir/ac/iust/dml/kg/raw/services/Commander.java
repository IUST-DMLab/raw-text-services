package ir.ac.iust.dml.kg.raw.services;

import ir.ac.iust.dml.kg.raw.extractor.EnhancedEntityExtractor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
class Commander {
  void processArgs(String[] args) {
    switch (args[0]) {
      case "export":
        final EnhancedEntityExtractor extractor = new EnhancedEntityExtractor();
        final Integer maxAmbiguities = args.length > 2 ? Integer.parseInt(args[2]) : 3;
        final Float contextDisambiguationThreshold = args.length > 3 ? Float.parseFloat(args[3]) : 0.0011f;
        final String pattern = args.length > 4 ? args[4] : ".*\\.txt";
        extractor.exportFolder(Paths.get(args[1]), pattern, maxAmbiguities, true,
            contextDisambiguationThreshold, true, true);
        break;
    }
  }
}

/*
 * Farsi Knowledge Graph Project
 *  Iran University of Science and Technology (Year 2017)
 *  Developed by Majid Asgari.
 */

package ir.ac.iust.dml.kg.raw.services;

import ir.ac.iust.dml.kg.raw.extractor.EnhancedEntityExtractor;
import ir.ac.iust.dml.kg.raw.services.split.SplitLogic;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
class Commander {
  void processArgs(String[] args) throws IOException {
    final EnhancedEntityExtractor extractor = new EnhancedEntityExtractor();
    final Path path = Paths.get(args[1]);
    final String encoding = args[2];
    final Integer maxAmbiguities = args.length > 3 ? Integer.parseInt(args[3]) : 4;
    final Float contextDisambiguationThreshold = args.length > 4 ? Float.parseFloat(args[4]) : 0.0011f;
    switch (args[0]) {
      case "export":
        final String pattern = args.length > 5 ? args[5] : ".*\\.txt";
        extractor.exportFolder(path, encoding, pattern, maxAmbiguities, true, true,
            contextDisambiguationThreshold, true, true, true);
        break;
      case "exportWiki":
        extractor.exportWiki(path, maxAmbiguities, true, true,
            contextDisambiguationThreshold, true, true, true);
        break;
      case "conjCount":
        SplitLogic.INSTANCE.findConjunctions(path);
        break;
      case "conjAndDepCount":
        SplitLogic.INSTANCE.findConjunctionsAndDep(path);
        break;
    }
    System.exit(0);
  }
}

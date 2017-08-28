package ir.ac.iust.dml.kg.raw.services.logic;

import ir.ac.iust.dml.kg.raw.extractor.EnhancedEntityExtractor;
import ir.ac.iust.dml.kg.raw.extractor.ResolvedEntityToken;
import ir.ac.iust.dml.kg.raw.services.access.entities.Article;
import ir.ac.iust.dml.kg.raw.services.access.entities.ArticleSentence;
import ir.ac.iust.dml.kg.raw.services.access.repositories.ArticleRepository;
import ir.ac.iust.dml.kg.raw.services.logic.data.TextRepositoryFile;
import ir.ac.iust.dml.kg.raw.utils.ConfigReader;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextRepositoryLogic {

  private final Path mainPath = ConfigReader.INSTANCE.getPath("raw.repository", "~/raw/repository");
  @Autowired
  ArticleRepository articleRepository;

  private Path getPath(String path) {
    if (path == null || path.isEmpty() || path.contains("..")) return mainPath;
    return mainPath.resolve(path);
  }

  public List<TextRepositoryFile> ls(String path) throws IOException {
    return Files.list(getPath(path))
        .filter(file -> Files.isDirectory(file) || file.getFileName().toString().endsWith(".json"))
        .map(file -> new TextRepositoryFile(Files.isDirectory(file), file.getFileName().toString()))
        .collect(Collectors.toList());
  }

  public List<List<ResolvedEntityToken>> get(String path) {
    final Path p = getPath(path);
    if (Files.isDirectory(p)) return null;
    return EnhancedEntityExtractor.importFromFile(p);
  }

  public boolean mark(String path) {
    final Path p = getPath(path);
    if (Files.isDirectory(p)) return false;
    Article article = articleRepository.findByPath(path);
    if (article != null) return false;
    final List<List<ResolvedEntityToken>> content = EnhancedEntityExtractor.importFromFile(p);
    if (content == null) return false;
    article = new Article();
    article.setPath(path);
    article.setTitle(path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path);
    article.setNumberOfSentences(content.size());
    article.setNumberOfRelations(0);
    article.setPercentOfRelations(0f);
    article.setApproved(false);
    for (List<ResolvedEntityToken> sentence : content) {
      final ArticleSentence articleSentence = new ArticleSentence();
      articleSentence.setNumberOfRelations(0);
      articleSentence.setSentence(String.join(" ",
          sentence.stream().map(ResolvedEntityToken::getWord).collect(Collectors.toList())));
      articleSentence.setTokens(sentence);
      article.getSentences().add(articleSentence);
    }
    try {
      articleRepository.save(article);
    } catch (Throwable e) {
      return false;
    }
    return true;
  }

  public Page<Article> searchArticles(int page, int pageSize, Integer minPercentOfRelations, Boolean approved) {
    return articleRepository.search(page, pageSize, minPercentOfRelations, approved);
  }

  public Article saveArticle(Article article) {
    if (article.getIdentifier() == null) return null;
    final Article articleInDB = articleRepository.findOne(new ObjectId(article.getIdentifier()));
    if (articleInDB == null) return null;
    articleInDB.setTitle(article.getTitle());
    articleInDB.setApproved(article.isApproved());
    int numberOfRelations = 0;
    List<ArticleSentence> sentences = article.getSentences();
    for (int i = 0; i < sentences.size(); i++) {
      final ArticleSentence sentence = sentences.get(i);
      if (sentence.getNumberOfRelations() < 0) continue;
      final ArticleSentence sentenceInDb = articleInDB.getSentences().get(i);
      sentenceInDb.setNumberOfRelations(sentence.getNumberOfRelations());
      numberOfRelations += sentence.getNumberOfRelations();
//      articleInDB.getSentences().get()
    }
    articleInDB.setNumberOfRelations(numberOfRelations);
    articleInDB.setPercentOfRelations((float) numberOfRelations / article.getNumberOfSentences());

    articleRepository.save(articleInDB);
    return articleInDB;
  }

}

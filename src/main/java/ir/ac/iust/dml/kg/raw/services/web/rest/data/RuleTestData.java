package ir.ac.iust.dml.kg.raw.services.web.rest.data;

import java.util.List;

public class RuleTestData {
  private List<String> rules;
  private List<String> predicates;
  private String text;

  public List<String> getRules() {
    return rules;
  }

  public void setRules(List<String> rules) {
    this.rules = rules;
  }

  public List<String> getPredicates() {
    return predicates;
  }

  public void setPredicates(List<String> predicates) {
    this.predicates = predicates;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}

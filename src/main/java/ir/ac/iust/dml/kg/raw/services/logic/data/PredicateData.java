package ir.ac.iust.dml.kg.raw.services.logic.data;

import java.util.ArrayList;
import java.util.List;

public class PredicateData {
  private String predicate;
  private long count;
  private List<AssigneeData> assignees = new ArrayList<>();

  public PredicateData(String predicate, long count) {
    this.predicate = predicate;
    this.count = count;
  }

  public String getPredicate() {
    return predicate;
  }

  public void setPredicate(String predicate) {
    this.predicate = predicate;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public List<AssigneeData> getAssignees() {
    return assignees;
  }

  public void setAssignees(List<AssigneeData> assignees) {
    this.assignees = assignees;
  }
}

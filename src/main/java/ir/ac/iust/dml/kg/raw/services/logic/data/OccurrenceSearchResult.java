package ir.ac.iust.dml.kg.raw.services.logic.data;

import ir.ac.iust.dml.kg.raw.services.access.entities.Occurrence;
import org.springframework.data.domain.Page;

public class OccurrenceSearchResult {
  private Page<Occurrence> page;
  private Long numberOfApproved;

  public OccurrenceSearchResult() {
  }

  public OccurrenceSearchResult(Page<Occurrence> page, Long numberOfApproved) {
    this.page = page;
    this.numberOfApproved = numberOfApproved;
  }

  public Page<Occurrence> getPage() {
    return page;
  }

  public void setPage(Page<Occurrence> page) {
    this.page = page;
  }

  public Long getNumberOfApproved() {
    return numberOfApproved;
  }

  public void setNumberOfApproved(Long numberOfApproved) {
    this.numberOfApproved = numberOfApproved;
  }
}

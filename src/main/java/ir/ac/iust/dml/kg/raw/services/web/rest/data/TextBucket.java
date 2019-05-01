/*
 * Farsi Knowledge Graph Project
 *  Iran University of Science and Technology (Year 2017)
 *  Developed by Majid Asgari.
 */

package ir.ac.iust.dml.kg.raw.services.web.rest.data;

public class TextBucket {
  private String text;
  private boolean simpleSummarize = false;
  private boolean removeBrackets = false;

  public TextBucket(String text) {
    this.text = text;
  }

  public TextBucket() {

  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public boolean isSimpleSummarize() {
    return simpleSummarize;
  }

  public void setSimpleSummarize(boolean simpleSummarize) {
    this.simpleSummarize = simpleSummarize;
  }

  public boolean isRemoveBrackets() {
    return removeBrackets;
  }

  public void setRemoveBrackets(boolean removeBrackets) {
    this.removeBrackets = removeBrackets;
  }
}

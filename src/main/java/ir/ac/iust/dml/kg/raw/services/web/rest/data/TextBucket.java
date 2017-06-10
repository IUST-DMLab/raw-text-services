package ir.ac.iust.dml.kg.raw.services.web.rest.data;

public class TextBucket {
  private String text;

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
}

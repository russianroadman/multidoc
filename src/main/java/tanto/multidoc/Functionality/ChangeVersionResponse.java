package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangeVersionResponse {

    @JsonView
    String content;

    @JsonView
    String author;

    @JsonView
    boolean starred;

    public ChangeVersionResponse(String content, String author, boolean starred) {
        this.content = content;
        this.author = author;
        this.starred = starred;
    }

    public boolean getStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

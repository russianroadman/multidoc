package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangeVersionResponse {

    @JsonView
    String content;

    @JsonView
    String author;

    public ChangeVersionResponse(String content, String author) {
        this.content = content;
        this.author = author;
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

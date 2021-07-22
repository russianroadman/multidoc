package tanto.multidoc.JSONModel;

import com.fasterxml.jackson.annotation.JsonView;

public class JSONVersion {

    @JsonView
    String id;

    @JsonView
    String author;

    @JsonView
    String content;

    @JsonView
    String isPreferred;

    public JSONVersion() {}

    public JSONVersion(String id, String author, String content, String isPreferred) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.isPreferred = isPreferred;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsPreferred() {
        return isPreferred;
    }

    public void setIsPreferred(String isPreferred) {
        this.isPreferred = isPreferred;
    }

    @Override
    public String toString() {
        return "\nJSONVersion{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", isPreferred='" + isPreferred + '\'' +
                "}\n";
    }
}

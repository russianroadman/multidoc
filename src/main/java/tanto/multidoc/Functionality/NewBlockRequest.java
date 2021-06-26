package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class NewBlockRequest {

    @JsonView
    String blockTitle;

    @JsonView
    String author;

    @JsonView
    String link;

    public String getLink() {
        return link.substring(6);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBlockTitle() {
        return blockTitle;
    }

    public void setBlockTitle(String blockTitle) {
        this.blockTitle = blockTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
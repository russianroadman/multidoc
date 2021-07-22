package tanto.multidoc.JSONModel;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class JSONDocument {

    @JsonView
    String link;

    @JsonView
    String title;

    @JsonView
    List<JSONBlock> blocks;

    public JSONDocument() {}

    public JSONDocument(String link, String title, List<JSONBlock> blocks) {
        this.link = link;
        this.title = title;
        this.blocks = blocks;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<JSONBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<JSONBlock> blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "JSONDocument{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", blocks=" + blocks.toString() +
                '}';
    }
}

package tanto.multidoc.JSONModel;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class JSONBlock {

    @JsonView
    String id;

    @JsonView
    String title;

    @JsonView
    List<JSONVersion> versions;

    public JSONBlock() {}

    public JSONBlock(String id, String title, List<JSONVersion> versions) {
        this.id = id;
        this.title = title;
        this.versions = versions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<JSONVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<JSONVersion> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "\nJSONBlock{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", versions=" + versions.toString() +
                "}\n";
    }
}

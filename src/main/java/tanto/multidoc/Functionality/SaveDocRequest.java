package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveDocRequest {

    @JsonView
    String content;

    @JsonView
    String link;

    public String getLink() {
        return link.substring(6);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

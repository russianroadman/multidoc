package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveDocRequest {

    @JsonView
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

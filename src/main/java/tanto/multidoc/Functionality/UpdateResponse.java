package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class UpdateResponse {

    @JsonView
    String content;

    public UpdateResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

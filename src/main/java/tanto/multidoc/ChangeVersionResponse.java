package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangeVersionResponse {

    @JsonView
    String content;

    public ChangeVersionResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

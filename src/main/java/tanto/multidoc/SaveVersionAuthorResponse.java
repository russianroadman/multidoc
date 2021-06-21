package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveVersionAuthorResponse {

    @JsonView
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SaveVersionAuthorResponse(String content){
        this.content = content;
    }

}

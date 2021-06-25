package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveDocResponse {

    @JsonView
    String content;

    public SaveDocResponse(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

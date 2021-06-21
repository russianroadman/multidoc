package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveBlockTitleResponse {

    @JsonView
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SaveBlockTitleResponse(String content){
        this.content = content;
    }

}

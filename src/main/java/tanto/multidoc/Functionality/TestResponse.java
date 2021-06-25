package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class TestResponse {

    @JsonView
    String content;

    public TestResponse(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class DocTitleRequest {

    @JsonView
    String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

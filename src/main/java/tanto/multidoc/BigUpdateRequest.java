package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class BigUpdateRequest {

    @JsonView
    String link;

    @JsonView
    List<JsonBlock> blocks;

    public String getLink() {
        return link.substring(6);
    }

    public List<JsonBlock> getBlocks() {
        return blocks;
    }
}

package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class BigUpdateResponse {

    @JsonView
    List<JsonBlock> blocks;

    public BigUpdateResponse(List<JsonBlock> blocks) {
        this.blocks = blocks;
    }
}

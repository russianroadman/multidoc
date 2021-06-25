package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveBlockTitleResponse {

    @JsonView
    String content;

    @JsonView
    String blockNumber;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public SaveBlockTitleResponse(String content, String blockNumber){
        this.content = content;
        this.blockNumber = blockNumber;
    }

}

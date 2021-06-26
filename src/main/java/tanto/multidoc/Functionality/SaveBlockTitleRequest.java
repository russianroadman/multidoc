package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveBlockTitleRequest {

    @JsonView
    String content;

    @JsonView
    String blockNumber;

    @JsonView
    String link;

    public String getLink() {
        return link.substring(6);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBlockNumber() {
        int out = Integer.parseInt(blockNumber)-1;
        return out;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }
}

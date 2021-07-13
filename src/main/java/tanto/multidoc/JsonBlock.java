package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class JsonBlock {

    @JsonView
    String content;

    @JsonView
    String blockNumber;

    @JsonView
    String versionNumber;

    public JsonBlock() {
    }

    public JsonBlock(String content, String blockNumber, String versionNumber) {
        this.content = content;
        this.blockNumber = blockNumber;
        this.versionNumber = versionNumber;
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

    public int getVersionNumber() {
        int out = Integer.parseInt(versionNumber)-1;
        return out;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

}

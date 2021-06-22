package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class SaveVersionResponse {

    @JsonView
    String content;

    @JsonView
    String blockNumber;

    @JsonView
    String versionNumber;

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

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public SaveVersionResponse(String content, String blockNumber, String versionNumber) {
        this.content = content;
        this.blockNumber = blockNumber;
        this.versionNumber = versionNumber;
    }
}

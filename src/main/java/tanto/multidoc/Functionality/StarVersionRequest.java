package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class StarVersionRequest {

    @JsonView
    String blockNumber;

    @JsonView
    String versionNumber;

    @JsonView
    String link;

    public int getVersionNumber() {
        int out = Integer.parseInt(versionNumber)-1;
        return out;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getBlockNumber() {
        int out = Integer.parseInt(blockNumber)-1;
        return out;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getLink() {
        return link.substring(6);
    }

    public void setLink(String link) {
        this.link = link;
    }

}

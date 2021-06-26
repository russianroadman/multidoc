package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangeVersionRequest {

    @JsonView
    String blockNumber;

    @JsonView
    String versionNumber;

    @JsonView
    String right;

    @JsonView
    String link;

    public String getLink() {
        return link.substring(6);
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

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }



}

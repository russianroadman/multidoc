package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class DeleteBlockRequest {

    @JsonView
    String blockNumber;

    @JsonView
    String link;

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

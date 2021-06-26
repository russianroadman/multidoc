package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class NewVersionRequest {

    @JsonView
    String author;

    @JsonView
    String blockNumber;

    @JsonView
    String link;

    public String getLink() {
        return link.substring(6);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBlockNumber() {
        int out = Integer.parseInt(blockNumber)-1;
        return out;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

}

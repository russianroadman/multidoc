package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class NewBlockRequest {

    @JsonView
    String blocktitle;

    @JsonView
    String author;

    public String getBlocktitle() {
        return blocktitle;
    }

    public void setBlocktitle(String blocktitle) {
        this.blocktitle = blocktitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

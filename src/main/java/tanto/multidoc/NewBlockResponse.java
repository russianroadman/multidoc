package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class NewBlockResponse {

    @JsonView
    String blocktitle;

    @JsonView
    String author;

    public NewBlockResponse(String blocktitle, String author){
        this.blocktitle = blocktitle;
        this.author = author;
    }

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

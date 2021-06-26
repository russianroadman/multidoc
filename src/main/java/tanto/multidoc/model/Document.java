package tanto.multidoc.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Document {

    @Id
    private String link;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Block> blocks = new ArrayList<>();

    public Document(String title, String link){
        this.title = title;
        this.link = link;
    }

    public Document(){}

    public Document(String title){
        this.title = title;
    }

    public int getDocLength(){
        return blocks.size();
    }

    public void addBlock(Block block){
        blocks.add(block);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}

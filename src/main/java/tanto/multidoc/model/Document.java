package tanto.multidoc.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Document {

    @Id
    private String link;
    private String title;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<Block> blocks = new ArrayList<>();

    public Document(String title, String link){
        this.title = title;
        this.link = link;
    }

    public Document(){}

    public Document(String title){
        this.title = title;
    }

    public Document(String link, String title, List<Block> blocks) {
        this.link = link;
        this.title = title;
        this.blocks = blocks;
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

    public boolean equalContent(Document d) {
        if(
            this.toString().equals(d.toString())
        ){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(link, document.link) && Objects.equals(title, document.title) && Objects.equals(blocks, document.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, title, blocks);
    }

    @Override
    public String toString() {
        return "Document{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", blocks=" + blocks +
                '}';
    }
}

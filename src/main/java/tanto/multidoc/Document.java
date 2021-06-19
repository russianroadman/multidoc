package tanto.multidoc;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Document {

    private String title;
    private String link;
    private ArrayList<Block> blocks = new ArrayList<>();


    public Document(String title){
        this.title = title;
        for (Block block : blocks){
            block.getVersions().get(0).getContent().getContent();
            blocks.indexOf(block);
            block.getTitle();
            block.getVersions().size();
            block.getVersions().get(0).getAuthor();
            for (Version ver : block.getVersions()){
                ver.getContent().getContent();
                ver.getAuthor();
            }
        }
    }

    public int getDocLength(){
        return blocks.size();
    }

    public void addBlock(Block block){
        blocks.add(block);
    }

    public void addBlock(String title, String author){
        blocks.add(new Block(title, author));
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
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

    public ArrayList<Version> getPreferredVersionsList(){
        ArrayList<Version> list = new ArrayList<>(blocks.stream().map(Block::getPreferred).collect(Collectors.toList()));
        return list;
    }
}

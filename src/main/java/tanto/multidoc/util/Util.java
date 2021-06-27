package tanto.multidoc.util;

import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;

import java.util.UUID;

public class Util {

    public static String getUniqueLink(){
        return UUID.randomUUID().toString();
    }

    public static Document getExampleDocument(String link){

        Document example;

        Version ver11 = new Version("Sample Author", true);
        Version ver12 = new Version("Different Author", false);
        Version ver21 = new Version("Sample Author", true);

        Block block1 = new Block("Sample block", ver11);
        block1.addVersion(ver12);

        Block block2 = new Block("Another block", ver21);

        example = new Document("Example document", link);

        example.addBlock(block1);
        example.addBlock(block2);

        return example;
    }

    public static String stringifyDocument(Document d){
        String out = d.getTitle() + ":\n";
        for (Block b : d.getBlocks()){
            out += "  block " + b.getTitle() + ":\n";
            for (Version v : b.getVersions()){
                out += "    " + v.getAuthor() + " : " + v.getContent() + "\n";
            }
            out+="\n";
        }
        return out;
    }

}

package tanto.multidoc.util;

import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;

import java.util.List;
import java.util.UUID;

public class Util {

    public static String getUniqueLink(){
        return UUID.randomUUID().toString();
    }
    private static final long LATENCY = 1;

    public static Document getExampleDocument(String link){

        Document example;

        Version ver11 = new Version("Sample Author", true);
        Version ver12 = new Version("Different Author", false);
        Version ver21 = new Version("Different Author", true);

        Block block1 = new Block("Sample block", ver11);
        block1.addVersion(ver12);

        Block block2 = new Block("Another block", ver21);

        example = new Document("Example document", link);

        example.addBlock(block1);
        example.addBlock(block2);

        return example;

    }

    public static String getFinalDocument(List<Version> versions){
        String out = "";
        for (Version v : versions){
            out += v.getContent() + "\n";
        }
        return out;
    }

    public static void sleep(){
        try {
            Thread.sleep(LATENCY);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}

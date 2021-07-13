package tanto.multidoc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelUtil {

    public static Version getPreferredVersion(Block b){
        List<Version> versions = b.getVersions();
        for (Version v : versions){
            if (v.isPreferred()) return v;
        }
        return null;
    }

    public static ArrayList<Version> getPreferredVersionsList(Document d){
        ArrayList<Version> list = new ArrayList<>(d.getBlocks().stream().map(Block::getPreferred).collect(Collectors.toList()));
        return list;
    }

    public static ArrayList<Version> getEntireVersionsList(Document d){
        ArrayList<Version> list = new ArrayList<>();
        for (Block block : d.getBlocks()){
            for (Version version : block.getVersions()){
                list.add(version);
            }
        }
        return list;
    }

    public static ArrayList<String> getEntireVersionsListContents(ArrayList<Version> list){
        ArrayList<String> out = new ArrayList<>();
        for (Version version : list){
            out.add(version.getContent());
        }
        return out;
    }



}

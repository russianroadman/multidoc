package tanto.multidoc.model;

import tanto.multidoc.JSONModel.JSONBlock;
import tanto.multidoc.JSONModel.JSONVersion;
import tanto.multidoc.repos.DocumentRepository;

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

    public static String getDocumentTitle(String link ,DocumentRepository documentRepository) {
        return documentRepository.findById(link).get().getTitle();
    }

    public static String getHtmlAsString(String link, DocumentRepository documentRepository){

        Document doc = documentRepository.findById(link).get();

        ArrayList<String> c = getEntireVersionsListContents(getPreferredVersionsList(doc));
        String initialString = "";
        for (String item : c){
            initialString += "<br>" + item;
        }

        return initialString;

    }

    public static String stringifyDocument(Document d){
        String out = d.getLink() + "\n" + d.getTitle() + ":\n";
        for (Block b : d.getBlocks()){
            out += "  block " + b.getTitle() + ":\n";
            for (Version v : b.getVersions()){
                out += "    " + v.getAuthor() + " : " + v.getContent() + "\n";
            }
            out+="\n";
        }
        return out;
    }

    public static List<Block> getDocumentCopy(Document d){

        List<Block> copy = new ArrayList<>();
        List<Block> original = d.getBlocks();

        for (Block block : original){

            Block b = new Block();

            List<Version> versionList = new ArrayList<>();

            for (Version version : block.getVersions()){

                Version v = new Version();
                v.setAuthor(version.getAuthor());
                v.setContent(version.getContent());
                v.setPreferred(version.isPreferred());
                versionList.add(v);

            }

            b.setTitle(block.getTitle());
            b.setVersions(versionList); //setting versions
            copy.add(b); // adding ready block to list

        }

        return copy;

    }

}

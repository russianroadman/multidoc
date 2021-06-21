package tanto.multidoc;

import java.util.ArrayList;

public class Block {

    private Version preferred;
    private ArrayList<Version> versions = new ArrayList<>();
    private String title;

    public Block(String title, String author){
        this.title = title;
        Version start = new Version(author, true);
        versions.add(start);
        preferred = start;
    }

    public void addVersion(String author, boolean isPreferred){
        Version version = new Version(author, isPreferred);
        if (isPreferred){
            preferred.noMorePreferred();
            preferred = version;
        }
        versions.add(version);
    }

    public void deleteVersion(int index){
        if (versions.get(index).isPreferred()){
            versions.remove(index);
            versions.get(0).preferred();
            preferred = versions.get(0);
        } else {
            versions.remove(index);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Version> getVersions() {
        return versions;
    }

    public Version getPreferred(){
        return preferred;
    }

}

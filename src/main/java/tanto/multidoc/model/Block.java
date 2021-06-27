package tanto.multidoc.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_sequence")
    private Integer id;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<Version> versions = new ArrayList<>();

    @Transient
    private Version preferred;

    public Block(){}

    public Block(String title, Version version){
        this.title = title;
        versions.add(version);
        preferred = version;
    }

    public void addVersion(String author, boolean isPreferred){
        Version version = new Version(author, isPreferred);
        if (isPreferred){
            preferred.noMorePreferred();
            preferred = version;
        }
        versions.add(version);
    }

    public void addVersion(Version version){
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

    public List<Version> getVersions() {
        return versions;
    }

    public Version getPreferred(){
        return preferred;
    }

}

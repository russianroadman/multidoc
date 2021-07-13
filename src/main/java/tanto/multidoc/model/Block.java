package tanto.multidoc.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_sequence")
    private Integer id;
    private String title;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT) // this leads to N+1 problem
    @OrderBy("id")
    private List<Version> versions = new ArrayList<>();

    public Block(){}

    public Block(String title, Version version){
        this.title = title;
        versions.add(version);
    }

    public void addVersion(String author, boolean isPreferred){
        Version version = new Version(author, isPreferred);
        if (isPreferred){
            getPreferred().noMorePreferred();
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
        } else {
            versions.remove(index);
        }
    }

    public void setPreferred(int index){
        getPreferred().noMorePreferred();
        getVersions().get(index).preferred();
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
        Version preferred = null;
        List<Version> versions = getVersions();
        for (Version v : versions){
            if (v.isPreferred()){
                preferred = v;
                break;
            }
        }
        return preferred;
    }

}

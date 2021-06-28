package tanto.multidoc.model;

import javax.persistence.*;

@Entity
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_sequence")
    private Integer id;
    private String author;
    @Column(columnDefinition = "TEXT")
    private String content = "";
    private boolean isPreferred = false;

    public Version(){}

    public Version(String author, boolean isPreferred){
        this.author = author;
        this.isPreferred = isPreferred;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPreferred() {
        return isPreferred;
    }

    public void noMorePreferred() {
        isPreferred = false;
    }

    public void preferred(){
        isPreferred = true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

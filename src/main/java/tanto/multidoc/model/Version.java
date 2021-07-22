package tanto.multidoc.model;

import javax.persistence.*;
import java.util.Objects;

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

    public Version(Integer id, String author, String content, boolean isPreferred) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.isPreferred = isPreferred;
    }

    public Version(String author) {
        this.author = author;
        this.isPreferred = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPreferred(boolean preferred) {
        isPreferred = preferred;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return isPreferred == version.isPreferred && Objects.equals(id, version.id) && Objects.equals(author, version.author) && Objects.equals(content, version.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, content, isPreferred);
    }

    @Override
    public String toString() {
        return "Version{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", isPreferred=" + isPreferred +
                '}';
    }
}

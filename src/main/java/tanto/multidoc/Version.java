package tanto.multidoc;

public class Version {

    private String author;
    private Content content = new Content();
    private boolean isPreferred = false;

    public Version(String author, boolean isPreferred){
        this.author = author;
        this.isPreferred = true;
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

}

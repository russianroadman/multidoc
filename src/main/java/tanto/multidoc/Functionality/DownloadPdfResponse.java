package tanto.multidoc.Functionality;

import com.fasterxml.jackson.annotation.JsonView;

public class DownloadPdfResponse {

    @JsonView
    String title;

    @JsonView
    byte[] bytes;

    public DownloadPdfResponse(String title, byte[] bytes) {
        this.title = title;
        this.bytes = bytes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }



}

package tanto.multidoc;

import com.fasterxml.jackson.annotation.JsonView;

public class NewVersionResponse {

    @JsonView
    String author;

    @JsonView
    String blockNumber;

    @JsonView
    String versionNumber;



}

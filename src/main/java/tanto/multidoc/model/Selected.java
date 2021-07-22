package tanto.multidoc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Selected {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_sequence")
    private Integer id;
    private String link;
    private Integer blockId;
    private Integer versionId;

}

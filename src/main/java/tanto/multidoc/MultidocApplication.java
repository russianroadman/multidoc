package tanto.multidoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;

@SpringBootApplication
public class MultidocApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultidocApplication.class, args);
	}

}

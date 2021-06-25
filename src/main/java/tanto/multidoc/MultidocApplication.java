package tanto.multidoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;

@SpringBootApplication
public class MultidocApplication {

	private static Document example = null;

	public static void main(String[] args) {

		example = new Document("My Multidoc");

		Block block1 = new Block("Great First block", new Version("Sample", true));
		block1.getVersions().get(0).setContent("Block that was created by Sample Author");

		example.addBlock(block1);

		SpringApplication.run(MultidocApplication.class, args);

	}

	public static Document getExample(){
		return example;
	}

}

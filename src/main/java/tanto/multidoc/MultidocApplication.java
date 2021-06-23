package tanto.multidoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultidocApplication {

	private static Document doc;

	public static void main(String[] args) {

		doc = new Document("My Multidoc");

		Block block1 = new Block("Great First block", "Sample Author");
		block1.getVersions().get(0).getContent().setContent("Block that was created by Sample Author");

		Block block2 = new Block("Magnificent Second block", "Another Author");
		block2.addVersion("Different Author", true);
		block2.getVersions().get(0).getContent().setContent("Block and its content that was created by Another Author");
		block2.getVersions().get(1).getContent().setContent("Different content for the block that Different Author made");

		Block block3 = new Block("Incredible Third block", "Different Author");
		block3.getVersions().get(0).getContent().setContent("Block that was created by Different Author");

		doc.addBlock(block1);
		doc.addBlock(block2);
		doc.addBlock(block3);

		SpringApplication.run(MultidocApplication.class, args);

	}

	public static Document getDoc(){
		return doc;
	}

}

package tanto.multidoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultidocApplication {

	private static Document doc;

	public static void main(String[] args) {

		doc = new Document("Мой документ");

		Block block1 = new Block("Первый блок", "Саша (preferred)");
		block1.addVersion("Тоже Саша", false);
		block1.addVersion("И это Саша", false);
		block1.getVersions().get(0).getContent().setContent("<p>1 hello world 1</p>");
		block1.getVersions().get(1).getContent().setContent("<p>1 hello world 2</p>");
		block1.getVersions().get(2).getContent().setContent("<p>1 hello world 3</p>");

		Block block2 = new Block("Второй блок", "Саша");
		block2.addVersion("Кислов (preferred)", true);
		block2.getVersions().get(0).getContent().setContent("<p>2 hello world 1</p>");
		block2.getVersions().get(1).getContent().setContent("<p>2 hello world 2</p>");

		Block block3 = new Block("Третий блок", "Вася (preferred)");
		block3.getVersions().get(0).getContent().setContent("<p>3 hello world 1</p>");

		doc.addBlock(block1);
		doc.addBlock(block2);
		doc.addBlock(block3);

		SpringApplication.run(MultidocApplication.class, args);

	}

	public static Document getDoc(){
		return doc;
	}

}

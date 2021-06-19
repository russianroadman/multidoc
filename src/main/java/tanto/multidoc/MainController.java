package tanto.multidoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPageRequest(){
        return "index";
    }

    @GetMapping("/redactor")
    public String redactorRequest(Model model){
        Document document = new Document("Тестовый документ");

        model.addAttribute("title", document.getTitle());

        Block block1 = new Block("Первый блок", "Саша (preferred)");
        block1.addVersion("Тоже Саша", false);
        block1.addVersion("И это Саша", false);
        block1.getVersions().get(0).getContent().setContent("<center><p>1 hello world 1</p></center>");
        block1.getVersions().get(1).getContent().setContent("<center><p>1 hello world 2</p></center>");
        block1.getVersions().get(2).getContent().setContent("<center><p>1 hello world 3</p></center>");

        Block block2 = new Block("Второй блок", "Саша");
        block2.addVersion("Кислов (preferred)", true);
        block2.getVersions().get(0).getContent().setContent("<center><p>2 hello world 1</p></center>");
        block2.getVersions().get(1).getContent().setContent("<center><p>2 hello world 2</p></center>");

        Block block3 = new Block("Третий блок", "Вася (preferred)");
        block3.getVersions().get(0).getContent().setContent("<center><p>3 hello world 1</p></center>");

        document.addBlock(block1);
        document.addBlock(block2);
        document.addBlock(block3);

        model.addAttribute("blocks", document.getBlocks());

        return "redactor";
    }

}

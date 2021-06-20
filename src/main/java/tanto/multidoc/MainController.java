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
        Document document = new Document("Мой документ");

        model.addAttribute("title", document.getTitle());

        Block block1 = new Block("Первый блок", "Саша (preferred)");
        block1.addVersion("Тоже Саша", false);
        block1.addVersion("И это Саша", false);
        block1.getVersions().get(0).getContent().setContent("<p style=\"text-align:center;\">1 hello world 1</p>");
        block1.getVersions().get(1).getContent().setContent("<p>1 hello world 2</p>");
        block1.getVersions().get(2).getContent().setContent("<p>1 hello world 3</p>");

        Block block2 = new Block("Второй блок", "Саша");
        block2.addVersion("Кислов (preferred)", true);
        block2.getVersions().get(0).getContent().setContent("<p>2 hello world 1</p>");
        block2.getVersions().get(1).getContent().setContent("<p>2 hello world 2</p>");

        Block block3 = new Block("Третий блок", "Вася (preferred)");
        block3.getVersions().get(0).getContent().setContent("<p>3 hello world 1</p>");

        document.addBlock(block1);
        document.addBlock(block2);
        document.addBlock(block3);

        model.addAttribute("blocks", document.getBlocks());

        return "redactor";
    }

}

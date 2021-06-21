package tanto.multidoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    Document doc = MultidocApplication.getDoc();

    @ResponseBody
    @PostMapping("/test")
    public TestResponse testRequest(@RequestBody TestRequest search){
        return new TestResponse(search.getContent());
    }

//    @PostMapping("new-block")
//    public String newBlockRequest(@RequestBody NewBlockRequest block, Model model){
//        doc.addBlock(new Block(block.getAuthor(), block.getAuthor()));
//        return "redactor";
//    }

    @PostMapping("new-block")
    public String newBlockRequest(@RequestBody NewBlockRequest block, Model model){
        doc.addBlock(new Block(block.getAuthor(), block.getAuthor()));
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());
        return "redactor::content";
    }

    @ResponseBody
    @PostMapping("new-version")
    public NewVersionResponse newVersionRequest(@RequestBody NewVersionRequest version){
        return new NewVersionResponse();
    }

    @ResponseBody
    @PostMapping("save-version")
    public SaveVersionResponse saveVersionRequest(@RequestBody SaveVersionRequest version){
        return new SaveVersionResponse();
    }

    @ResponseBody
    @PostMapping("save-block-title")
    public SaveBlockTitleResponse saveBlockRequest(@RequestBody SaveBlockTitleRequest title){
        doc.getBlocks().get(title.getBlockNumber()).setTitle(title.getContent());
        return new SaveBlockTitleResponse(title.getContent(), Integer.toString(title.getBlockNumber()));
    }

    @ResponseBody
    @PostMapping("save-version-author")
    public SaveVersionAuthorResponse saveVersionAuthorRequest(@RequestBody SaveVersionAuthorRequest author){
        doc.getBlocks()
                .get(author.getBlockNumber())
                .getVersions()
                .get(author.getVersionNumber())
                .setAuthor(author.getContent());
        return new SaveVersionAuthorResponse(author.getContent(),
                                             Integer.toString(author.getBlockNumber()),
                                             Integer.toString(author.getVersionNumber()));
    }

    @ResponseBody
    @PostMapping("save-doc-title")
    public SaveDocResponse saveDocRequest(@RequestBody SaveDocRequest title){
        doc.setTitle(title.content);
        return new SaveDocResponse(title.getContent());
    }

    @GetMapping("/")
    public String mainPageRequest(){
        return "index";
    }

    @GetMapping("/redactor")
    public String redactorRequest(Model model){
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());
        return "redactor";
    }

}

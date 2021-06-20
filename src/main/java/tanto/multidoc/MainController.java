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
        return new TestResponse(search.content+"<p>kek</p>");
    }

    @ResponseBody
    @PostMapping("new-block")
    public NewBlockResponse newBlockRequest(@RequestBody NewBlockRequest block){
        return new NewBlockResponse();
    }

    @ResponseBody
    @PostMapping("new-version")
    public NewVersionResponse newVersionRequest(@RequestBody NewVersionRequest version){
        return new NewVersionResponse();
    }

    @ResponseBody
    @PostMapping("save-block")
    public SaveBlockResponse saveBlockRequest(@RequestBody SaveBlockRequest block){
        return new SaveBlockResponse();
    }

    @ResponseBody
    @PostMapping("save-version")
    public SaveVersionResponse saveVersionRequest(@RequestBody SaveVersionRequest version){
        return new SaveVersionResponse();
    }

    @ResponseBody
    @PostMapping("save-doc-title")
    public SaveDocResponse saveDocRequest(@RequestBody SaveDocRequest title){
        doc.setTitle(title.content);
        return new SaveDocResponse(title.content);
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

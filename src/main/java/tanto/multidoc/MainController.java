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

    @ResponseBody
    @PostMapping("change-version")
    public ChangeVersionResponse changeVersionRequest(@RequestBody ChangeVersionRequest loc){
        String out;
        if (loc.getRight().equals("true")){
            out = doc.getBlocks()
                .get(loc.getBlockNumber())
                .getVersions()
                .get(loc.getVersionNumber()+1)
                .getContent()
                .getContent();
        } else {
            out = doc.getBlocks()
                .get(loc.getBlockNumber())
                .getVersions()
                .get(loc.getVersionNumber()-1)
                .getContent()
                .getContent();
        }
        return new ChangeVersionResponse(out);
    }

    @PostMapping("new-block")
    public String newBlockRequest(@RequestBody NewBlockRequest block, Model model){
        doc.addBlock(new Block(block.getBlockTitle(), block.getAuthor()));
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());
        return "redactor::content";
    }

    @PostMapping("new-version")
    public String newVersionRequest(@RequestBody NewVersionRequest version, Model model){
        Version ver = new Version(version.getAuthor(), false);
        ver.getContent().setContent("");
        doc.getBlocks().get(version.getBlockNumber()).addVersion(ver);
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());
        return "redactor::content";
    }

    @ResponseBody
    @PostMapping("save-version")
    public SaveVersionResponse saveVersionRequest(@RequestBody SaveVersionRequest version){
        doc.getBlocks()
            .get(version.getBlockNumber())
            .getVersions()
            .get(version.getVersionNumber())
            .getContent()
            .setContent(version.getContent());
        return new SaveVersionResponse(
                version.getContent(),
                Integer.toString(version.getBlockNumber()),
                Integer.toString(version.getVersionNumber())
        );
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

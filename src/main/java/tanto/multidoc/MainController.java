package tanto.multidoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tanto.multidoc.Functionality.*;
import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;
import tanto.multidoc.repos.BlockRepository;
import tanto.multidoc.repos.DocumentRepository;
import tanto.multidoc.repos.VersionRepository;
import tanto.multidoc.util.Util;

import java.util.Optional;

@Controller
public class MainController {

    //Document doc = MultidocApplication.getExample();

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    VersionRepository versionRepository;

    @GetMapping("/")
    public String mainPageRequest(){
        return "index";
    }

    @GetMapping("/info")
    public String infoRequest(){
        return "info";
    }

    @GetMapping("redactor")
    public String redactorRequest(Model model, @RequestParam String link){
        Optional<Document> doc = documentRepository.findById(link);
        model.addAttribute("title", doc.get().getTitle());
        model.addAttribute("blocks", doc.get().getBlocks());
        return "redactor";
    }

    @PostMapping("new-multidoc")
    public String newMultidocRequest(RedirectAttributes attributes,
                                     @RequestParam String docTitle,
                                     @RequestParam String blockTitle,
                                     @RequestParam String author){

        Version version = new Version(author, true);
        //versionRepository.save(version);

        Block block = new Block(blockTitle, version);
        //blockRepository.save(block);

        String link = Util.getUniqueLink();
        Document doc = new Document(docTitle, link);
        doc.addBlock(block);
        //documentRepository.save(doc);

        attributes.addAttribute("link", link);

        return "redirect:redactor";
    }

    @PostMapping("new-block")
    public String newBlockRequest(@RequestBody NewBlockRequest block,
                                  Model model){

        Document doc = documentRepository.findById(block.getLink()).get();
        doc.addBlock(new Block(block.getBlockTitle(), new Version(block.getAuthor(), false)));
        documentRepository.save(doc);
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());

        return "redactor::content";
    }

    @PostMapping("new-version")
    public String newVersionRequest(@RequestBody NewVersionRequest version, Model model){
        Version ver = new Version(version.getAuthor(), false);
        ver.setContent("");

        /*doc.getBlocks().get(version.getBlockNumber()).addVersion(ver);
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());*/

        return "redactor::content";
    }

    @ResponseBody
    @PostMapping("save-version")
    public SaveVersionResponse saveVersionRequest(@RequestBody SaveVersionRequest version){

        /*doc.getBlocks()
            .get(version.getBlockNumber())
            .getVersions()
            .get(version.getVersionNumber())
            .setContent(version.getContent());*/

        return new SaveVersionResponse(
                version.getContent(),
                Integer.toString(version.getBlockNumber()),
                Integer.toString(version.getVersionNumber())
        );
    }

    @ResponseBody
    @PostMapping("save-block-title")
    public SaveBlockTitleResponse saveBlockRequest(@RequestBody SaveBlockTitleRequest title){
        //doc.getBlocks().get(title.getBlockNumber()).setTitle(title.getContent());
        return new SaveBlockTitleResponse(title.getContent(), Integer.toString(title.getBlockNumber()));
    }

    @ResponseBody
    @PostMapping("save-version-author")
    public SaveVersionAuthorResponse saveVersionAuthorRequest(@RequestBody SaveVersionAuthorRequest author){
        /*doc.getBlocks()
                .get(author.getBlockNumber())
                .getVersions()
                .get(author.getVersionNumber())
                .setAuthor(author.getContent());*/
        return new SaveVersionAuthorResponse(author.getContent(),
                                             Integer.toString(author.getBlockNumber()),
                                             Integer.toString(author.getVersionNumber()));
    }

    @ResponseBody
    @PostMapping("save-doc-title")
    public SaveDocResponse saveDocRequest(@RequestBody SaveDocRequest title){
        //doc.setTitle(title.getContent());
        return new SaveDocResponse(title.getContent());
    }

    @ResponseBody
    @PostMapping("change-version")
    public ChangeVersionResponse changeVersionRequest(@RequestBody ChangeVersionRequest loc){
        String out = null;
        String author = null;

        /*if (loc.getRight().equals("true")){
            out = doc.getBlocks()
                .get(loc.getBlockNumber())
                .getVersions()
                .get(loc.getVersionNumber()+1)
                .getContent();
            author = doc.getBlocks()
                    .get(loc.getBlockNumber())
                    .getVersions()
                    .get(loc.getVersionNumber()+1)
                    .getAuthor();
        } else {
            out = doc.getBlocks()
                .get(loc.getBlockNumber())
                .getVersions()
                .get(loc.getVersionNumber()-1)
                .getContent();
            author = doc.getBlocks()
                    .get(loc.getBlockNumber())
                    .getVersions()
                    .get(loc.getVersionNumber()-1)
                    .getAuthor();
        }*/

        return new ChangeVersionResponse(out, author);
    }

}

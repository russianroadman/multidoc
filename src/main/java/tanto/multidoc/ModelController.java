package tanto.multidoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tanto.multidoc.JSONModel.JSONDocument;
import tanto.multidoc.JSONModel.JSONModelConverter;
import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;
import tanto.multidoc.repos.DocumentRepository;
import tanto.multidoc.repos.VersionRepository;
import tanto.multidoc.util.Util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ModelController {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    VersionRepository versionRepository;

    private ExecutorService clients = Executors.newFixedThreadPool(5);

    @ResponseBody
    @GetMapping("force-update-doc/{link}")
    public JSONDocument forceUpdateDoc(@PathVariable String link){
        Document document = documentRepository.findById(link).get();
        return JSONModelConverter.docToJson(document);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("save-doc")
    public void saveDoc(@RequestBody JSONDocument doc){

        Document d = documentRepository.findById(doc.getLink()).get();
        List<Block> blocks = d.getBlocks();

        for (int i = 0; i < blocks.size(); i++){
            for (int j = 0; j < blocks.get(i).getVersions().size(); j++){
                blocks.get(i).getVersions().get(j)
                    .setContent(
                        doc.getBlocks().get(i).getVersions().get(j).getContent()
                    );
            }
        }

        documentRepository.save(d);

    }

    @ResponseBody
    @GetMapping("update-doc/{link}")
    public DeferredResult<JSONDocument> updateDoc(@PathVariable String link) {

        DeferredResult<JSONDocument> result = new DeferredResult<>();
        Document d = documentRepository.findById(link).get();
        Document[] actual = {documentRepository.findById(link).get()};

        clients.execute( () -> {

            while (true){

                actual[0] = documentRepository.findById(link).get();


                if (d.equalContent(actual[0])){

                    Util.sleep();

                } else {

                    result.setResult(JSONModelConverter.docToJson(actual[0]));
                    break;

                }

            }

        } );

        return result;

    }

    @PostMapping("new-multidoc")
    public String newMultidocRequest(RedirectAttributes attributes,
                                     @RequestParam String docTitle,
                                     @RequestParam String blockTitle,
                                     @RequestParam String author){

        Version version = new Version(author, true);

        Block block = new Block(blockTitle, version);

        String link = Util.getUniqueLink();
        Document doc = new Document(docTitle, link);
        doc.addBlock(block);

        documentRepository.save(doc);

        attributes.addAttribute("link", link);

        return "redirect:redactor";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("delete-block/{link}/{index}")
    public void deleteBlock(@PathVariable String link, @PathVariable String index){

        Document doc = documentRepository.findById(link).get();
        doc.getBlocks().remove( Integer.parseInt(index) );
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("delete-version/{link}/{bIndex}/{vIndex}")
    public void deleteVersion(@PathVariable String link,
                              @PathVariable String bIndex,
                              @PathVariable String vIndex){

        Document doc = documentRepository.findById(link).get();
        doc.getBlocks()
            .get( Integer.parseInt(bIndex) )
            .getVersions()
            .remove( Integer.parseInt(vIndex) );
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("star-version/{link}/{bIndex}/{vIndex}")
    public void starVersion(@PathVariable String link,
                            @PathVariable String bIndex,
                            @PathVariable String vIndex){

        Document doc = documentRepository.findById(link).get();
        doc.getBlocks().get(0).getVersions().get(0).isPreferred();
        doc.getBlocks().get( Integer.parseInt(bIndex) ).setPreferred( Integer.parseInt(vIndex) );
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("add-block/{link}/{author}/{blockTitle}")
    public void addBlock(@PathVariable String link,
                         @PathVariable String author,
                         @PathVariable String blockTitle){

        Document doc = documentRepository.findById(link).get();
        doc.addBlock(new Block(blockTitle, new Version(author)));
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("add-version/{link}/{bIndex}/{author}")
    public void addVersion(@PathVariable String link,
                           @PathVariable String bIndex,
                           @PathVariable String author){

        Version ver = new Version(author, false);

        Document doc = documentRepository.findById(link).get();
        doc.getBlocks().get( Integer.parseInt(bIndex) ).addVersion(ver);
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("save-version-author/{link}/{bIndex}/{vIndex}/{authorName}")
    public void saveVersionAuthor(@PathVariable String link,
                                  @PathVariable String bIndex,
                                  @PathVariable String vIndex,
                                  @PathVariable String authorName){

        Document doc = documentRepository.findById(link).get();
        doc.getBlocks()
                .get( Integer.parseInt(bIndex) )
                .getVersions()
                .get( Integer.parseInt(vIndex) )
                .setAuthor(authorName);
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("save-block-title/{link}/{bIndex}/{title}")
    public void saveBlockTitle(@PathVariable String link,
                               @PathVariable String bIndex,
                               @PathVariable String title){

        Document doc = documentRepository.findById(link).get();
        doc.getBlocks().get( Integer.parseInt(bIndex) ).setTitle( title );
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("save-doc-title/{link}/{title}")
    public void saveDocTitle(@PathVariable String link,
                             @PathVariable String title){

        Document doc = documentRepository.findById(link).get();
        doc.setTitle(title);
        documentRepository.save(doc);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("save-focus/{link}/{vId}/{condition}")
    public void saveFocus(@PathVariable String link,
                          @PathVariable String vId,
                          @PathVariable String condition){

        Document d = documentRepository.findById(link).get();
        Version v;
        if (d != null){
            v = versionRepository.findById(Integer.parseInt(vId)).get();
            if (condition.equals("true")) v.setBeingEdited(true);
            else v.setBeingEdited(false);
            versionRepository.save(v);
        }

    }


}

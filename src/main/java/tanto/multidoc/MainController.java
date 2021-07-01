package tanto.multidoc;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

@Controller
public class MainController {

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
        Document doc = documentRepository.findById(link).get();
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());
        return "redactor";
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

        /* comment line below if database has no tables */
        documentRepository.save(doc);

        attributes.addAttribute("link", link);

        return "redirect:redactor";
    }

    @GetMapping("new-multidoc-default")
    public String newMultidocRequest(RedirectAttributes attributes){

        String link = Util.getUniqueLink();
        Document doc = Util.getExampleDocument(link);

        /* comment line below if database has no tables */
        documentRepository.save(doc);

        attributes.addAttribute("link", link);

        return "redirect:redactor";
    }

    @PostMapping("new-block")
    public String newBlockRequest(@RequestBody NewBlockRequest block,
                                  Model model){

        Document doc = documentRepository.findById(block.getLink()).get();
        doc.addBlock(new Block(block.getBlockTitle(), new Version(block.getAuthor(), true)));
        documentRepository.save(doc);

        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());

        return "redactor::content";
    }

    @PostMapping("new-version")
    public String newVersionRequest(@RequestBody NewVersionRequest version, Model model){
        Version ver = new Version(version.getAuthor(), false);

        Document doc = documentRepository.findById(version.getLink()).get();
        doc.getBlocks().get(version.getBlockNumber()).addVersion(ver);
        documentRepository.save(doc);

        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());

        return "redactor::content";
    }

    @ResponseBody
    @PostMapping("save-version")
    public SaveVersionResponse saveVersionRequest(@RequestBody SaveVersionRequest version){

        Document doc = documentRepository.findById(version.getLink()).get();

        doc.getBlocks()
            .get(version.getBlockNumber())
            .getVersions()
            .get(version.getVersionNumber())
            .setContent(version.getContent());

        documentRepository.save(doc);

        return new SaveVersionResponse(
                version.getContent(),
                Integer.toString(version.getBlockNumber()),
                Integer.toString(version.getVersionNumber())
        );
    }

    @ResponseBody
    @PostMapping("save-block-title")
    public SaveBlockTitleResponse saveBlockRequest(@RequestBody SaveBlockTitleRequest title){
        Document doc = documentRepository.findById(title.getLink()).get();
        doc.getBlocks().get(title.getBlockNumber()).setTitle(title.getContent());
        documentRepository.save(doc);
        return new SaveBlockTitleResponse(title.getContent(), Integer.toString(title.getBlockNumber()));
    }

    @ResponseBody
    @PostMapping("save-version-author")
    public SaveVersionAuthorResponse saveVersionAuthorRequest(@RequestBody SaveVersionAuthorRequest author){
        Document doc = documentRepository.findById(author.getLink()).get();
        doc.getBlocks()
                .get(author.getBlockNumber())
                .getVersions()
                .get(author.getVersionNumber())
                .setAuthor(author.getContent());
        documentRepository.save(doc);
        return new SaveVersionAuthorResponse(author.getContent(),
                                             Integer.toString(author.getBlockNumber()),
                                             Integer.toString(author.getVersionNumber()));
    }

    @ResponseBody
    @PostMapping("save-doc-title")
    public SaveDocResponse saveDocRequest(@RequestBody SaveDocRequest title){
        Document doc = documentRepository.findById(title.getLink()).get();
        doc.setTitle(title.getContent());
        documentRepository.save(doc);
        return new SaveDocResponse(title.getContent());
    }

    @ResponseBody
    @PostMapping("change-version")
    public ChangeVersionResponse changeVersionRequest(@RequestBody ChangeVersionRequest loc){

        String out;
        String author;

        Document doc = documentRepository.findById(loc.getLink()).get();
        Version target;

        if (loc.getRight().equals("true")){

            target = doc.getBlocks()
                .get(loc.getBlockNumber())
                .getVersions()
                .get(loc.getVersionNumber()+1);

            out = target.getContent();
            author = target.getAuthor();

        } else {
            target = doc.getBlocks()
                    .get(loc.getBlockNumber())
                    .getVersions()
                    .get(loc.getVersionNumber()-1);

            out = target.getContent();
            author = target.getAuthor();
        }

        return new ChangeVersionResponse(out, author, target.isPreferred());
    }

    @PostMapping("delete-block")
    public String deleteBlockRequest(@RequestBody DeleteBlockRequest block, Model model){

        Document doc = documentRepository.findById(block.getLink()).get();
        doc.getBlocks().remove(block.getBlockNumber());
        documentRepository.save(doc);

        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());

        return "redactor::content";

    }

    @PostMapping("delete-version")
    public String deleteVersionRequest(@RequestBody DeleteVersionRequest version, Model model){

        Document doc = documentRepository
                .findById(version.getLink())
                .get();

        doc
            .getBlocks()
            .get(version.getBlockNumber())
            .deleteVersion(version.getVersionNumber());

        documentRepository.save(doc);

        model.addAttribute("title", doc.getTitle());
        model.addAttribute("blocks", doc.getBlocks());

        return "redactor::content";

    }

    @ResponseBody
    @PostMapping("star-version")
    public void starVersionRequest(@RequestBody StarVersionRequest version){

        Document doc = documentRepository
                .findById(version.getLink())
                .get();

        doc.getBlocks().get(0).getVersions().get(0).isPreferred();

        doc.getBlocks().get(version.getBlockNumber()).setPreferred(version.getVersionNumber());
        documentRepository.save(doc);

    }

    @ResponseBody
    @PostMapping("get-doc-title")
    public String getDocTitleRequest(@RequestBody DocTitleRequest link){
        return documentRepository.findById(link.getSource()).get().getTitle();
    }

    @GetMapping("delete-document/{link}")
    public String deleteDocumentRequest(@PathVariable String link){
        documentRepository.deleteById(link);
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("download-document/{link}")
    public DownloadPdfResponse downloadDocumentRequest(@PathVariable String link) throws Exception{

        Document doc = documentRepository.findById(link).get();

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, stream);

        BaseFont bf=BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        document.open();
        Font documentTitleFont = new Font(bf, 24, Font.BOLD, BaseColor.BLACK);

        Font blockTitleFont = new Font(bf, 18, Font.BOLD, BaseColor.BLACK);
        Font font = new Font(bf, 14, Font.NORMAL, BaseColor.BLACK);

        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(doc.getTitle(), documentTitleFont));
        document.add(preface);
        document.add( Chunk.NEWLINE );
        
        for (Block b : doc.getBlocks()){

            preface = new Paragraph();
            preface.add(new Paragraph(b.getTitle(), blockTitleFont));
            document.add(preface);
            document.add( Chunk.NEWLINE );

            preface = new Paragraph();
            preface.add(new Paragraph(b.getPreferred().getContent(), font));
            document.add(preface);
            document.add( Chunk.NEWLINE );

            document.add(new Chunk("by: " + b.getPreferred().getAuthor(), font));
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );

        }

        document.close();

        return new DownloadPdfResponse(doc.getTitle(), stream.toByteArray());

    }

}

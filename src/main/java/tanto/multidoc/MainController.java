package tanto.multidoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tanto.multidoc.Functionality.*;
import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.ModelUtil;
import tanto.multidoc.model.Version;
import tanto.multidoc.repos.DocumentRepository;
import tanto.multidoc.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class MainController {

    @Autowired
    DocumentRepository documentRepository;

    /* get mappings */

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

    @GetMapping("delete-document/{link}")
    public String deleteDocumentRequest(@PathVariable String link){
        documentRepository.deleteById(link);
        return "redirect:/";
    }

    @GetMapping("print/{link}")
    public String printRequest(@PathVariable String link, Model model){

        Document d = documentRepository.findById(link).get();
        ModelUtil.getPreferredVersionsList(d);

        ArrayList<String> c = ModelUtil.getEntireVersionsListContents(
                ModelUtil.getPreferredVersionsList(d));
        String initialString = "";
        for (String item : c){
            initialString += "<br>" + item;
        }

        model.addAttribute("content", initialString);

        return "print";
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

    /* post mappings */

    @ResponseBody
    @PostMapping("get-doc-title")
    public String getDocTitleRequest(@RequestBody DocTitleRequest link){
        String out;
        try{
            out = documentRepository.findById(link.getSource()).get().getTitle();
        } catch (NoSuchElementException e) {
            out = null;
        }
        return out;
    }

}

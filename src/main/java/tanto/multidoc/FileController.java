package tanto.multidoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileController {

    @GetMapping("/info")
    public String infoRequest(){
        return "info";
    }

}

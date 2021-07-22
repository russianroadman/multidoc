package tanto.multidoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import tanto.multidoc.Functionality.BigUpdateRequest;
import tanto.multidoc.Functionality.BigUpdateResponse;
import tanto.multidoc.Functionality.JsonBlock;
import tanto.multidoc.repos.DocumentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class LongPollingController {

    @Autowired
    DocumentRepository documentRepository;

    private ExecutorService clients = Executors.newFixedThreadPool(5);
    private final long LATENCY = 1;

    @ResponseBody
    @PostMapping("update-version")
    public DeferredResult<BigUpdateResponse> updateVersionRequest(@RequestBody BigUpdateRequest content){

        DeferredResult<BigUpdateResponse> deferredResult = new DeferredResult<>();
        List<String> actual = new ArrayList<>();

        clients.execute( () -> {

            boolean hasDifference = false;

            while (true){

                actual.clear();

                for (int i = 0; i < content.getBlocks().size(); i++){

                    // getting current json block
                    JsonBlock current = content.getBlocks().get(i);
                    // and then parsing it
                    int bi = current.getBlockNumber();
                    int vi = current.getVersionNumber();
                    String visibleContent = current.getContent();

                    // getting actual version content for current json block
                    String actualVerContent = documentRepository.findById(content.getLink()).get()
                            .getBlocks()
                            .get( bi )
                            .getVersions()
                            .get( vi )
                            .getContent();

                    // adding actual version content to list
                    // so we can use list later for passing it to the client
                    actual.add(actualVerContent);

                    // check if there is a difference
                    if ( ! hasDifference ){
                        if ( ! visibleContent.equals( actualVerContent ) ) {
                            hasDifference = true;
                        }
                    }

                }

                if ( hasDifference ){

                    List<JsonBlock> list = new ArrayList<>();

                    for (int i = 0; i < content.getBlocks().size(); i++){
                        JsonBlock item = content.getBlocks().get(i);
                        item.setContent(actual.get(i));
                        list.add(item);
                    }

                    deferredResult.setResult(new BigUpdateResponse(list));
                    break;

                } else {

                    try {
                        Thread.sleep(LATENCY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }

        } );

        return deferredResult;
    }

}

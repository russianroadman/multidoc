package tanto.multidoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import tanto.multidoc.Functionality.UpdateRequest;
import tanto.multidoc.Functionality.UpdateResponse;
import tanto.multidoc.repos.DocumentRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class LongPollingController {

    @Autowired
    DocumentRepository documentRepository;

    private ExecutorService clients = Executors.newFixedThreadPool(5);

    @ResponseBody
    @PostMapping("update-version")
    public DeferredResult<UpdateResponse> updateVersionRequest(@RequestBody UpdateRequest content){

        DeferredResult<UpdateResponse> deferredResult = new DeferredResult<>();
        final String[] actualContent = new String[1];
        String clientVisibleContent = content.getContent();

        clients.execute(() -> {

            while(true){

                actualContent[0] = documentRepository
                        .findById(content.getLink()).get()
                        .getBlocks()
                        .get(content.getBlockNumber())
                        .getVersions()
                        .get(content.getVersionNumber())
                        .getContent();

                if( clientVisibleContent.equals( actualContent[0] ) ){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    deferredResult.setResult(new UpdateResponse( actualContent[0] ));
                    break;
                }

            }

        });

        return deferredResult;
    }

}

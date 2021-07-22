package tanto.multidoc.JSONModel;

import tanto.multidoc.model.Block;
import tanto.multidoc.model.Document;
import tanto.multidoc.model.Version;

import java.util.ArrayList;
import java.util.List;

public class JSONModelConverter {

    public static JSONDocument docToJson(Document d){

        List<JSONBlock> jsonBlockList = new ArrayList<>();

        for (Block block : d.getBlocks()){

            JSONBlock jsonBlock = new JSONBlock();

            List<JSONVersion> jsonVersionList = new ArrayList<>();

            for (Version version : block.getVersions()){
                jsonVersionList.add(new JSONVersion(
                        version.getId().toString(),
                        version.getAuthor(),
                        version.getContent(),
                        version.isPreferred() ? "true" : "false",
                        version.isBeingEdited() ? "true" : "false"
                ));
            }

            jsonBlock.setId(block.getId().toString());
            jsonBlock.setTitle(block.getTitle());
            jsonBlock.setVersions(jsonVersionList); //setting versions
            jsonBlockList.add(jsonBlock); // adding ready block to list

        }

        JSONDocument jsonDocument = new JSONDocument(d.getLink(), d.getTitle(), jsonBlockList);
        return jsonDocument;

    }

    public static Document jsonToDoc(JSONDocument d){

        List<Block> blockList = new ArrayList<>();

        for (JSONBlock jsonBlock : d.getBlocks()){

            Block block = new Block();

            List<Version> versionList = new ArrayList<>();

            for (JSONVersion version : jsonBlock.getVersions()){

                boolean isPreferred = false;
                if (version.getIsPreferred().equals("true")){
                    isPreferred = true;
                }

                boolean isBeingEdited = false;
                if (version.getIsBeingEdited().equals("true")){
                    isBeingEdited = true;
                }

                versionList.add(new Version(
                        Integer.parseInt(version.getId()),
                        version.getAuthor(),
                        version.getContent(),
                        isPreferred,
                        isBeingEdited
                ));

            }

            block.setId(Integer.parseInt(jsonBlock.getId()));
            block.setTitle(jsonBlock.getTitle());
            block.setVersions(versionList); //setting versions
            blockList.add(block); // adding ready block to list

        }

        return new Document(d.getLink(), d.getTitle(), blockList);

    }

}

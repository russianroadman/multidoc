package tanto.multidoc.util;

import java.util.UUID;

public class Util {

    public static String getUniqueLink(){
        return UUID.randomUUID().toString();
    }

}

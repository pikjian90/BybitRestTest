package testcase;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

public class BaseTest {

    public HashMap<String,String> convertSingleResponseResultToMap(Response res){
        HashMap<String,String> hm = new HashMap<>();
        JsonPath jsonPath = res.jsonPath();
        String jsonPathString = jsonPath.get("result").toString()
                .replace("{","")
                .replace("}","")
                .replace("[","")
                .replace("]","");

        String[] s = jsonPathString.split(",");
        for(int i=0;i<s.length;i++){
            String[] sSplit = s[i].split("=");
            if(sSplit[0].startsWith(" ")){
                sSplit[0] = sSplit[0].substring(1,sSplit[0].length());
            }
            hm.put(sSplit[0],sSplit[1]);
        }
        return hm;
    }

}

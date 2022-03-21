package testcase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest {
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extentReports;

    @BeforeSuite
    public void beforeSuite(){
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/BybitRESTTestReport.html");
        htmlReporter.config().setDocumentTitle("API Testing Report");
        htmlReporter.config().setReportName("Bybit REST API Test");
        htmlReporter.config().setTheme(Theme.DARK);

        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);
        extentReports.setSystemInfo("Host Name","Local Host");
        extentReports.setSystemInfo("Environment","QA");
        extentReports.setSystemInfo("Tester","QA");

    }
    public HashMap<String,String> convertSingleResponseResultToMap(Response res) throws Exception{
            HashMap<String,String> hm = new HashMap<>();
            JsonPath jsonPath = res.jsonPath();
            String jsonPathString = jsonPath.get("result").toString()
                    .replace("{","")
                    .replace("}","")
                    .replace("[","")
                    .replace("]","");

            String[] s = jsonPathString.split(",");
            for(int i=0;i<s.length;i++){
                if(s[i].equals("") || s[i] == null){
                    throw new Exception("Response Result is empty");
                }
                if(!s[i].endsWith("=")) {
                    String[] sSplit = s[i].split("=");
                    if (sSplit[0].startsWith(" ")) {
                        sSplit[0] = sSplit[0].substring(1, sSplit[0].length());
                    }
                    hm.put(sSplit[0], sSplit[1]);
                }
            }
            return hm;
    }

    public HashMap<String,String> convertQuerySymbolResponseResult(Response res, String symbol){
        HashMap<String,String> hm = new HashMap<>();
        List<Map<String,String>> resultList = res.getBody().jsonPath().get("result");

        for(int i=0;i<resultList.size();i++){
            if((resultList.get(i).get("name")).equals(symbol)){
                hm.putAll(resultList.get(i));
            }
        }
        return hm;
    }

    @AfterSuite
    public void tearDownSuite(){
        extentReports.flush();
    }
}

package testcase.spot.marketData;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import common.XLUtils.XLUtils;
import common.endPoints.EndPoints;
import common.requests.Spot;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testcase.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.lessThan;

public class QuerySymbol extends BaseTest {
    String request = Spot.QUERY_SYMBOL;

    @DataProvider(name="testQuerySymbol")
    public Object[][] getData() throws IOException {
        //read data from excel
        String path = System.getProperty("user.dir") + "/src/test/java/resources/spot/testQuerySymbol.xlsx";
        int rowNum = XLUtils.getRowCount(path, "Sheet1");
        int colNum = XLUtils.getCellCount(path, "sheet1", 1);
        String[][] testData = new String[rowNum][colNum];

        for (int i = 1; i <= rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                testData[i - 1][j] = XLUtils.getCellData(path, "Sheet1", i, j);
            }
        }
        return testData;
    }

    //Verify Response Time
    @Test
    public void testRequestTime(){
        ExtentTest extentTest = extentReports.createTest("testRequestTime","to verify LatestBigDeal response");
        try{
            RestAssured.given()
                    .when()
                    .get(request)
                    .then()
                    .time(lessThan(10000L));
        }
        catch (AssertionError e){
            e.printStackTrace();
            extentTest.log(Status.FAIL, e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    //Print Response Log
    @Test
    public void testRequestLog(){
        try{
            RestAssured.given()
                    .when()
                    .get(request)
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test(dataProvider = "testQuerySymbol")
    public void testQuerySymbol(String symbol, String baseCurrency, String quoteCurrency){
        ExtentTest extentTest = extentReports.createTest("testQuerySymbol","to verify QuerySymbol response");
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .get(request);

            Map<String,String> hm = convertQuerySymbolResponseResult(response,symbol);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json;charset=UTF-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("name"),symbol,"name in Response is not same as expected");
            softAssert.assertEquals(hm.get("alias"),symbol,"name in Response is not same as expected");
            softAssert.assertEquals(hm.get("baseCurrency"),baseCurrency,"name in Response is not same as expected");
            softAssert.assertEquals(hm.get("quoteCurrency"),quoteCurrency,"name in Response is not same as expected");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(Status.FAIL, e.getMessage());
            Assert.fail(e.getMessage());        }


    }


}

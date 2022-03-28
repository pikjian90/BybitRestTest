package testcase.inverseperpetual.marketData;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import common.XLUtils.XLUtils;
import common.endPoints.EndPoints;
import common.order.Order;
import common.requests.InversePerpetualRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testcase.BaseTest;

import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.Matchers.lessThan;

public class PublicTradingRecords extends BaseTest {
    String request = InversePerpetualRequest.PUBLIC_TRADE_RECORDS;

    @DataProvider(name="testPublicTradingRecords")
    public Object[][] getData() throws IOException {
        //read data from excel
        String path = System.getProperty("user.dir") + "/src/test/java/resources/inverseperpetual/testPublicTradingRecords.xlsx";
        int rowNum = XLUtils.getRowCount(path,"Sheet1");
        int colNum = XLUtils.getCellCount(path,"sheet1",1);
        String[][] testData = new String [rowNum][colNum];

        for(int i=1;i<=rowNum;i++){
            for(int j=0;j<colNum;j++){
                testData[i-1][j] = XLUtils.getCellData(path,"Sheet1",i,j);
            }
        }
        return testData;
    }

    //Verify Response Time
    @Test(dataProvider = "testPublicTradingRecords")
    public void testRequestTime(String symbol, String limit){
        ExtentTest extentTest = extentReports.createTest("testRequestTime","to verify LatestBigDeal response");
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", symbol)
                    .queryParam("limit",limit)
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
    @Test(dataProvider = "testPublicTradingRecords")
    public void testRequestLog(String symbol, String limit){
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", symbol)
                    .queryParam("limit",limit)
                    .get(request)
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test(dataProvider = "testPublicTradingRecords")
    public void testPublicTradingRecords(String symbol, String limit){
        ExtentTest extentTest = extentReports.createTest("testPublicTradingRecords","to verify PublicTradingRecords response");
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .queryParam("symbol", symbol)
                    .queryParam("limit",limit)
                    .get(request);

            HashMap<String,String> hm = convertSingleResponseResultToMap(response);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json; charset=utf-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("symbol"),symbol,"Symbol in Response is not same as expected");
            softAssert.assertTrue(Order.getSide().contains(hm.get("side")),"Side in Response is not either Buy or Sell");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(Status.FAIL, e.getMessage());
            Assert.fail(e.getMessage());        }


    }


}

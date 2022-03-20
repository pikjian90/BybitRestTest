package testcase.usdtPerpetual.marketData;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import common.endPoints.EndPoints;
import common.requests.UsdtPerpetual;
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

public class GetTheLastFundingRate extends BaseTest {
    String request = UsdtPerpetual.GET_THE_LAST_FUNDING_RATE;

    @DataProvider(name="testGetTheLastFundingRate")
    public Object[][] getData() throws IOException {
        //read data from excel
        String path = System.getProperty("user.dir") + "/src/test/java/resources/usdtperpetual/testGetTheLastFundingRate.xlsx";
        int rowNum = common.util.XLUtils.getRowCount(path,"Sheet1");
        int colNum = common.util.XLUtils.getCellCount(path,"sheet1",1);
        String[][] testData = new String [rowNum][colNum];

        for(int i=1;i<=rowNum;i++){
            for(int j=0;j<colNum;j++){
                testData[i-1][j] = common.util.XLUtils.getCellData(path,"Sheet1",i,j);
            }
        }
        return testData;
    }

    //Verify Response Time
    @Test(dataProvider = "testGetTheLastFundingRate")
    public void testRequestTime(String symbol){
        ExtentTest extentTest = extentReports.createTest("testRequestTime","to verify LatestBigDeal response");
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", symbol)
                    .get(request)
                    .then()
                    .time(lessThan(10000L));
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
            extentTest.log(Status.FAIL, e.getMessage());
        }
    }

    //Print Response Log
    @Test(dataProvider = "testGetTheLastFundingRate")
    public void testRequestLog(String symbol){
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", symbol)
                    .get(request)
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test(dataProvider = "testGetTheLastFundingRate")
    public void testGetTheLastFundingRate(String symbol){
        extentReports.createTest("testGetTheLastFundingRate","to verify GetTheLastFundingRate response");
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .queryParam("symbol", symbol)
                    .get(request);

            HashMap<String,String> hm = convertSingleResponseResultToMap(response);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json; charset=utf-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("symbol"),symbol,"Symbol in Response is not same as expected");
            softAssert.assertTrue(Double.parseDouble(hm.get("funding_rate")) < 1,"Side in Response is not either Buy or Sell");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }


    }


}

package testcase.inverseperpetual.marketData;

import common.endpoints.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testcase.BaseTest;

import java.util.Map;

import static org.hamcrest.Matchers.lessThan;

public class QuerySymbol extends BaseTest {

    //Verify Response Time
    @Test
    public void testRequestTime(){
        try{
            RestAssured.given()
                    .when()
                    .get("/v2/public/symbols")
                    .then()
                    .time(lessThan(10000L));
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Print Response Log
    @Test
    public void testRequestLog(){
        try{
            RestAssured.given()
                    .when()
                    .get("/v2/public/symbols")
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test
    public void testQuerySymbol(){
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .get("/v2/public/symbols");

            String ExpectedSymbol = "ETHUSDT";
            Map<String,String> hm = convertQuerySymbolResponseResult(response,ExpectedSymbol);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json; charset=utf-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("name"),ExpectedSymbol,"name in Response is not same as expected");
            softAssert.assertEquals(hm.get("alias"),ExpectedSymbol,"alias in Response is not same as expected");
            softAssert.assertEquals(hm.get("status"),"Trading","status in Response is not same as expected");
//           TODO : Add after DataDriven Implemented
//            softAssert.assertEquals(hm.get("baseCurrency"),"ETH","name in Response is not same as expected");
//            softAssert.assertEquals(hm.get("quoteCurrency"),"USDT","name in Response is not same as expected");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

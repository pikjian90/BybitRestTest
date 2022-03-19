package testcase.usdtperpetual.marketData;

import common.endpoints.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testcase.BaseTest;

import java.time.Instant;
import java.util.HashMap;

import static org.hamcrest.Matchers.lessThan;

public class QueryMarkPriceKline extends BaseTest {

    //Verify Response Time
    @Test
    public void testRequestTime(){
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", "BTCUSDT")
                    .queryParam("interval", 1)
                    .queryParam("from", Instant.now().getEpochSecond()-60)
                    .queryParam("limit",1)
                    .get("/public/linear/mark-price-kline")
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
                    .queryParam("symbol", "BTCUSDT")
                    .queryParam("interval", 1)
                    .queryParam("from", Instant.now().getEpochSecond()-60)
                    .queryParam("limit",1)
                    .get("/public/linear/mark-price-kline")
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test
    public void testGetTheLastFundingRate(){
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .queryParam("symbol", "BTCUSDT")
                    .queryParam("interval", 1)
                    .queryParam("from", Instant.now().getEpochSecond()-60)
                    .queryParam("limit",1)
                    .get("/public/linear/mark-price-kline");

            HashMap<String,String> hm = convertSingleResponseResultToMap(response);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json; charset=utf-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("symbol"),"BTCUSDT","Symbol in Response is not same as expected");
            softAssert.assertEquals(hm.get("period"),"1","period in Response is not same as expected");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

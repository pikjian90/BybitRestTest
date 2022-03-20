package testcase.inverseperpetual.marketData;

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

public class QueryKline extends BaseTest {

    //Verify Response Time
    @Test
    public void testRequestTime(){
        System.out.println(Instant.now().getEpochSecond());
        System.out.println(Instant.now().getEpochSecond()-1);
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", "BTCUSD")
                    .queryParam("interval", 1)
                    .queryParam("from", Instant.now().getEpochSecond()-60)
                    .queryParam("limit",1)
                    .get("/v2/public/kline/list")
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
                    .queryParam("symbol", "BTCUSD")
                    .queryParam("interval", 1)
                    .queryParam("from", Instant.now().getEpochSecond()-60)
                    .queryParam("limit",1)
                    .get("/v2/public/kline/list")
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test
    public void testQueryKline(){
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .queryParam("symbol", "BTCUSD")
                    .queryParam("interval", 1)
                    .queryParam("from", Instant.now().getEpochSecond()-60)
                    .queryParam("limit",1)
                    .get("/v2/public/kline/list");

            HashMap<String,String> hm = convertSingleResponseResultToMap(response);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json; charset=utf-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("symbol"),"BTCUSD","Symbol in Response is not same as expected");
            softAssert.assertEquals(hm.get("interval"),"1","interval in Response is not same as expected");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}

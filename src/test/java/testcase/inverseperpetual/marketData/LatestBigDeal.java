package testcase.inverseperpetual.marketData;

import common.endpoints.EndPoints;
import common.endpoints.Order;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testcase.BaseTest;

import java.util.HashMap;

import static org.hamcrest.Matchers.lessThan;

public class LatestBigDeal extends BaseTest {

    //Verify Response Time
    @Test
    public void testRequestTime(){
        try{
            RestAssured.given()
                    .when()
                    .queryParam("symbol", "BTCUSD")
                    .queryParam("limit",1)
                    .get("/v2/public/big-deal")
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
                    .queryParam("limit",1)
                    .get("/v2/public/big-deal")
                    .then()
                    .log().body();
        }
        catch (AssertionError e){
            Assert.fail(e.toString());
        }
    }

    //Verify Response Fields
    @Test
    public void testLatestBigDeal(){
        RestAssured.baseURI = EndPoints.endPoint;
        try {
            Response response = RestAssured.given()
                    .when()
                    .queryParam("symbol", "BTCUSD")
                    .queryParam("limit",1)
                    .get("/v2/public/big-deal");

            HashMap<String,String> hm = convertSingleResponseResultToMap(response);

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(response.getStatusCode(),200,"Status code is not 200");
            softAssert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK","Status line is not as expected");
            softAssert.assertEquals(response.getContentType(),"application/json; charset=utf-8","ContentType is not same as expected");
            softAssert.assertEquals(hm.get("symbol"),"BTCUSD","Symbol in Response is not same as expected");
            softAssert.assertTrue(Order.getSide().contains(hm.get("side")),"Side in Response is not either Buy or Sell");
            softAssert.assertAll();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

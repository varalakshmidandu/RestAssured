package testCases;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReadAllProducts {
/*  
 * 01. ReadAllProducts
 *   http method = GET
 *   
 *   EndPointUrl = https://techfios.com/api-prod/api/product/read.php
 *    Authorization : (basic auth)
 *    username= demo@techfios.com
 *    password=abc123
 *    Header/s:Content-Type=application/json; charset=UTF-8
 *    http status code=200
 *    responseTime= <=1500ms
 *    
 *    given()=all input details=(baseUri,Header/s, Authorization,queryParams,Payload/Body)
 *    when()=submit request=httpMethod(endPoint)
 *    then()=response validation(statusCode,Header/s,responseTime,response Payload/Body)
 *     */
	String baseURI = "https://techfios.com/api-prod/api/product";
	@Test
	public void readAllProducts(){
	
		
	Response response =	(Response) given()
		    // .log().all()
			 .baseUri(baseURI)
		     //.baseUri("https://techfios.com/api-prod/api/product")
		     .header("Content-Type","application/json; charset=UTF-8")
		     .auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
		     //.log().all()
		     .get("/read.php").
		then()
	     .log().all()
//		     .statusCode(200)
//		     .header("Content-Type","application/json; charset=UTF-8");
		   .extract().response();  //getting whole response time 
	   
	    long responseTime =  response.getTimeIn(TimeUnit.MILLISECONDS);  
	    System.out.println("Response Time: "  + responseTime);
	    
	        if(responseTime<=2500) {
	        	
	        	System.out.println("Response time is within range");
	        }else {
	        	System.out.println("Response time is out of range");
	        }
	    
	   int responseSatusCode = response.getStatusCode();
	   System.out.println("Response Status Code: "  + responseSatusCode);
	   Assert.assertEquals(responseSatusCode, 200);
	   
	   String responseHeaderContentType = response.getHeader("Content-Type");
	   System.out.println("Response Header ContentType:" + responseHeaderContentType);
	   Assert.assertEquals(responseHeaderContentType,"application/json; charset=UTF-8");
	   
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);
	   String firstProductId = jp.getString("records[0].id");
	   System.out.println("First Product Id :" + firstProductId);
	   
	   if(firstProductId != null) {
		   System.out.println("Product list is not empty");
	   }else {
		   System.out.println("Product list is  empty");
	   }
	
	}
	

}

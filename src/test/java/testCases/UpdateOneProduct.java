package testCases;



import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateOneProduct {
/*  
 
	
  
{
    "name": "Amazing Headset 1.0 By MD",
    "description": "The best Headset for amazing programmers.",
    "price": "199",
    "category_id": "2",
    "category_name": "Electronics"
}
 *    
 *    given()=all input details=(baseUri,Header/s, Authorization,queryParams,Payload/Body)
 *    when()=submit request=httpMethod(endPoint)
 *    then()=response validation(statusCode,Header/s,responseTime,response Payload/Body)
 *     */
	String baseURI;// = "https://techfios.com/api-prod/api/product";
	SoftAssert  softAssert;
	String createPayloadPath="src\\\\main\\\\java\\\\data\\\\CreatePayload.json";
	HashMap<String,String> createPayload;
	String firstProductId;
	HashMap<String,String> updatePayload;
	
	public UpdateOneProduct() {
	baseURI = "https://techfios.com/api-prod/api/product";
	softAssert = new SoftAssert();
	createPayloadPath = "src\\\\main\\\\java\\\\data\\\\CreatePayload.json";
	createPayload = new HashMap<String,String>();
	updatePayload = new HashMap<String,String>();
	}
	
	public Map<String, String> createPayloadMap(){
		
		createPayload.put("name", "Amazing Headset 1.0 By MD");
		createPayload.put("description", "The Super Headset for amazing programmers.");
		createPayload.put("price", "999");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
		
	}
	
public Map<String, String> updatePayloadMap(){
		
	updatePayload.put("id", "6277");
	updatePayload.put("name", "Amazing Headset 5.0 By MD");
	updatePayload.put("description", "The Super Headset for amazing programmers.");
	updatePayload.put("price", "899");
	updatePayload.put("category_id", "2");
	updatePayload.put("category_name", "Electronics");
	return updatePayload;
		
	}
	
	
	//@Test(priority=1)
	public void createOneProducts(){
	
		
		Response response =	
		given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json; charset=UTF-8")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     //.body(new File(createPayloadPath)). // we can create file object and call
	          .body(createPayloadMap()).//providing map
		     when()
		     .post("/create.php").
		then()
		   .extract().response();  //getting whole response time 
	   
	    long responseTime =  response.getTimeIn(TimeUnit.MILLISECONDS);  
	  //  System.out.println("Response Time: "  + responseTime);
	    
	        if(responseTime<=2500) {
	        	
	        	//System.out.println("Response time is within range");
	        }else {
	        	//System.out.println("Response time is out of range");
	        }
	    
	   int responseSatusCode = response.getStatusCode();
	   //Assert.assertEquals(responseSatusCode, 200);
	   softAssert.assertEquals(responseSatusCode, 201,"Response status codes are not matching");
	   System.out.println("Response Status Code: "  + responseSatusCode);
	   
	   
	   String responseHeaderContentType = response.getHeader("Content-Type");
	  // Assert.assertEquals(responseHeaderContentType,"application/json");
	   softAssert.assertEquals(responseHeaderContentType,"application/json; charset=UTF-8","Response headers are not matching");
	   System.out.println("Response Header ContentType:" + responseHeaderContentType);
	   
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);
	   
	   String productMessage = jp.getString("message");
	   softAssert.assertEquals(productMessage,"Product was created.","product name is not matching"); 
	   System.out.println("Product Message :" + productMessage);
	   
	  
	   
	   
	   softAssert.assertAll();
	}
	
	//@Test(priority=2)
	public void readAllProducts(){
	
		
	Response response =	
			given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json; charset=UTF-8")
		     .auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
		     .get("/read.php").
		then()
	     //.log().all()
		   .extract().response();  //getting whole response time 
	
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);
	   firstProductId = jp.getString("records[0].id");
	   System.out.println("First Product Id :" + firstProductId);
	   
	}
	
	//@Test(priority=3)
	public void readOneProducts(){
	Response response =	
		given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     .queryParam("id","firstProductId").
	    when()
		     .get("/read_one.php").
		then()
		   .extract().response();  //getting whole response time 	   
	   
	   String actualresponseBody = response.getBody().asString();
	   System.out.println("Actual Response Body: "  + actualresponseBody);
	
	   JsonPath jp = new JsonPath(actualresponseBody);
	   
	   String actualproductName = jp.getString("name");
	   String expectedProductName =createPayloadMap().get("name");
	   softAssert.assertEquals(actualproductName,expectedProductName,"product name is not matching"); 
	   System.out.println("Product Name :" + actualproductName);
	   
	   String actualproductDescription = jp.getString("description");
	   String expectedProductDescription = createPayloadMap().get("description");
	   softAssert.assertEquals(actualproductDescription,expectedProductDescription,"Description is not matching");// to display error message
	   //softAssert.assertEquals(productDescription,"The best Headset for amazing programmers.","Description is not matching");
	   System.out.println("Product Description :" + actualproductDescription);
	  
	   
	   String actualproductPrice = jp.getString("price");
	   String expectedProductprice = createPayloadMap().get("price");
	   softAssert.assertEquals(actualproductPrice,expectedProductprice,"price is not matching");
	   System.out.println("Actual Product Price :" + actualproductPrice);
	   
	 
	 softAssert.assertAll();
	}
	
	@Test(priority=4)
	public void updateOneProducts(){
	
		
		Response response =	
		given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json; charset=UTF-8")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     //.body(new File(createPayloadPath)). // we can create file object and call
	          .body(updatePayloadMap()).//providing map
		     when()
		     .put("/update.php").
		then()
		   .extract().response();  //getting whole response time 
	   
	    long responseTime =  response.getTimeIn(TimeUnit.MILLISECONDS);  
	  //  System.out.println("Response Time: "  + responseTime);
	    
	        if(responseTime<=2500) {
	        	
	        	//System.out.println("Response time is within range");
	        }else {
	        	//System.out.println("Response time is out of range");
	        }
	    
	   int responseSatusCode = response.getStatusCode();
	   //Assert.assertEquals(responseSatusCode, 200);
	   softAssert.assertEquals(responseSatusCode, 200,"Response status codes are not matching");
	   System.out.println("Response Status Code: "  + responseSatusCode);
	   
	   
	   String responseHeaderContentType = response.getHeader("Content-Type");
	  // Assert.assertEquals(responseHeaderContentType,"application/json");
	   softAssert.assertEquals(responseHeaderContentType,"application/json; charset=UTF-8","Response headers are not matching");
	   System.out.println("Response Header ContentType:" + responseHeaderContentType);
	   
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);
	   
	   String productMessage = jp.getString("message");
	   softAssert.assertEquals(productMessage,"Product was updated.","product name is not matching"); 
	   System.out.println("Product Message :" + productMessage);
	   
	  
	   
	   
	   softAssert.assertAll();
	}
	
	
	
	@Test(priority=5)
		public void readOneUpdateProducts(){
		Response response =	
			given()
				 .baseUri(baseURI)
			     .header("Content-Type","application/json")
			     .auth().preemptive().basic("demo@techfios.com", "abc123")
			     .queryParam("id",updatePayloadMap().get("id")).
		    when()
			     .get("/read_one.php").
			then()
			   .extract().response();  //getting whole response time 	   
		   
		   String actualresponseBody = response.getBody().asString();
		   System.out.println("Actual Response Body: "  + actualresponseBody);
		
		   JsonPath jp = new JsonPath(actualresponseBody);
		   
		   String actualproductName = jp.getString("name");
		   String expectedProductName =updatePayloadMap().get("name");
		   softAssert.assertEquals(actualproductName,expectedProductName,"product name is not matching"); 
		   System.out.println("Product Name :" + actualproductName);
		   
		   String actualproductDescription = jp.getString("description");
		   String expectedProductDescription = updatePayloadMap().get("description");
		   softAssert.assertEquals(actualproductDescription,expectedProductDescription,"Description is not matching");// to display error message
		   //softAssert.assertEquals(productDescription,"The best Headset for amazing programmers.","Description is not matching");
		   System.out.println("Product Description :" + actualproductDescription);
		  
		   
		   String actualproductPrice = jp.getString("price");
		   String expectedProductprice = updatePayloadMap().get("price");
		   softAssert.assertEquals(actualproductPrice,expectedProductprice,"price is not matching");
		   System.out.println("Actual Product Price :" + actualproductPrice);
		   
		 
		 softAssert.assertAll();
		}
}

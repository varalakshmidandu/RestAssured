package testCases;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DeleteOneProductAdvanced {

 
	
  

	String baseURI;
	SoftAssert  softAssert;
	String createPayloadPath;
	HashMap<String,String> createPayload;
	String firstProductId;
	HashMap<String,String> deletePayload;
	String deleteProductId;
	
	public DeleteOneProductAdvanced() {
	baseURI = "https://techfios.com/api-prod/api/product";
	softAssert = new SoftAssert();
	createPayloadPath = "src\\\\main\\\\java\\\\data\\\\CreatePayload.json";
	createPayload = new HashMap<String,String>();
	deletePayload = new HashMap<String,String>();
	}
	

	
	public Map<String, String> createPayloadMap(){
		
		createPayload.put("name", "Amazing Headset 1.0 By MD");
		createPayload.put("description", "The fine Headset for amazing programmers.");
		createPayload.put("price", "199");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
		
	}
	
public Map<String, String> deletePayloadMap(){
		
	deletePayload.put("id", deleteProductId);
	
	return deletePayload;
		
	}
	
	@Test(priority=1)
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
	
	@Test(priority=2)
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
	   deleteProductId = firstProductId;
	}
	
	
	
	@Test(priority=3)
	public void updateOneProducts(){
	
		
		Response response =	
		given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json; charset=UTF-8")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     //.body(new File(createPayloadPath)). // we can create file object and call
	          .body(deletePayloadMap()).//providing map
		     when()
		     .delete("/delete.php").
		then()
		   .extract().response();  //getting whole response time 
		  
		   int responseStatusCode = response.getStatusCode(); 
		   softAssert.assertEquals(responseStatusCode, 200,"Response status codes are not matching");
		   System.out.println("Response Status Code: "  + responseStatusCode);
		   
	    long responseTime =  response.getTimeIn(TimeUnit.MILLISECONDS);  
	    
	        if(responseTime<=2500) {
	        	
	        }else {
	        	
	        }
	    
	   
	   
	   String responseHeaderContentType = response.getHeader("Content-Type");
	   softAssert.assertEquals(responseHeaderContentType,"application/json; charset=UTF-8","Response headers are not matching");
	   System.out.println("Response Header ContentType:" + responseHeaderContentType);
	   
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);
	   
	   String deleteproductMessage = jp.getString("message");
	   softAssert.assertEquals(deleteproductMessage,"Product was deleted.","product name is not matching"); 
	   System.out.println("Product Message :" + deleteproductMessage);	   
	   softAssert.assertAll();
	}
	@Test(priority=4)
		public void deleteOneUpdateProducts(){
		Response response =	
			given()
				 .baseUri(baseURI)
			     .header("Content-Type","application/json")
			     .auth().preemptive().basic("demo@techfios.com", "abc123")
			     .queryParam("id",deletePayloadMap().get("id")).
		    when()
			     .get("/read_one.php").
			then()
			   .extract().response();  //getting whole response time 	   
		   
		int responseSatusCode = response.getStatusCode();
		   softAssert.assertEquals(responseSatusCode, 404,"Response status codes are not matching");
		   System.out.println("Response Status Code: "  + responseSatusCode);
		
		String actualresponseBody = response.getBody().asString();
		   System.out.println("Actual Response Body: "  + actualresponseBody);
		
		   JsonPath jp = new JsonPath(actualresponseBody);
		   
		   String actualdeleteMessage = jp.getString("message");
		   String expecteddeleteMessage ="Product does not exist.";
		   softAssert.assertEquals(actualdeleteMessage,expecteddeleteMessage,"product name is not matching"); 
		   System.out.println("Product Name :" + actualdeleteMessage);
		   
		 
		 softAssert.assertAll();
		}
}

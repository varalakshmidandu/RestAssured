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

public class CreateOneProduct {
/*  
 
	03.CreateOneProduct
	http method=POST 
	EndPointUrl=https://techfios.com/api-prod/api/product/create.php
	Authorization:(basic auth)
	username=demo@techfios.com
	password=abc123
	Header/s:
	Content-Type=application/json; charset=UTF-8
	http status code=201
	responseTime= <=1500ms
	Payload/Body: 
  
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
	String readOneProductId;
	
	public CreateOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
	   softAssert = new SoftAssert();
	   createPayloadPath = "src\\\\main\\\\java\\\\data\\\\CreatePayload.json";
	   createPayload = new HashMap<String,String>();
	}
	
	public Map<String, String> createPayloadMap(){
		
		createPayload.put("name", "Amazing Headset 1.0 By MD");
		createPayload.put("description", "The best Headset for amazing programmers.");
		createPayload.put("price", "199");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
		
	}
	/*{
	    "name": "Amazing Headset 1.0 By MD",
	    "description": "The best Headset for amazing programmers.",
	    "price": "199",
	    "category_id": "2",
	    "category_name": "Electronics"
	}*/
	
	@Test(priority=1)
	public void createOneProducts(){
	// System.out.println("Create PayLoad Map:" + createPayloadMap());
		
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
	   
	}

	@Test(priority=3)
	public void readOneProducts(){
	
		readOneProductId = firstProductId;
	Response response =	 given()
		   
			 .baseUri(baseURI)
		    
		     .header("Content-Type","application/json")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     .queryParam("id",readOneProductId).
		when()
		    
		     .get("/read_one.php").
		then()

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
	   System.out.println("Response Header: " + responseHeaderContentType);
	  // Assert.assertEquals(responseHeaderContentType,"application/json");
	   
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);// creating a object of JSon Path
	   String productName = jp.getString("name");
	   System.out.println("Product Name :" + productName);
	  // Assert.assertEquals(productName,"Amazing Pillow 2.0 By MD");
	   
	   String productPrice = jp.getString("price");
	   System.out.println("Product Price :" + productPrice);
	  // Assert.assertEquals(productPrice,"199");
	   
	   String productDescription = jp.getString("description");
	   System.out.println("Product Description :" + productDescription);
	  // Assert.assertEquals(productDescription,"The best pillow for amazing programmers.");
	
	}
}
	
	//@Test(priority=3)
	/*public void readOneProducts(){
	Response response =	
		given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     .queryParam("id","firstProductId").
	    when()
		     .get("/read_one.php").
		then()
		   .extract().response(); 
	//getting whole response time 	   
	   int responseSatusCode = response.getStatusCode();
	   //Assert.assertEquals(responseSatusCode, 200);
	   softAssert.assertEquals(responseSatusCode, 200,"Response status codes are not matching");
	   System.out.println("Response Status Code: "  + responseSatusCode);
	   
	   String responseHeaderContentType = response.getHeader("Content-Type");
		  // Assert.assertEquals(responseHeaderContentType,"application/json");
		   softAssert.assertEquals(responseHeaderContentType,"application/json","Response headers are not matching");
		   System.out.println("Response Header ContentType:" + responseHeaderContentType);
		   
	
	   String actualresponseBody = response.getBody().asString();
	   System.out.println("Actual Response Body: "  + actualresponseBody);
	
	   JsonPath jp = new JsonPath(actualresponseBody);
	   
	   String actualproductName = jp.getString("name");
	   String expectedProductName = createPayloadMap().get("name");
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
	
	
}*/

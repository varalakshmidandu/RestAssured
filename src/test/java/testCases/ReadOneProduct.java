package testCases;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ReadOneProduct {
/*  
 * 01. ReadOneProducts
	 *  MethodGET 
	endpoint URL: https://techfios.com/api-prod/api/product/read_one.php
	Authorizarion:(basic auth)
	username=demo@techfios.com
	password=abc123
	Query Parameteres:
	id=6024
	Header/s:Content-Type,application/json
	http status code=200
	responseTime= <=1500ms
 *    
 *    given()=all input details=(baseUri,Header/s, Authorization,queryParams,Payload/Body)
 *    when()=submit request=httpMethod(endPoint)
 *    then()=response validation(statusCode,Header/s,responseTime,response Payload/Body)
 *     */
	String baseURI;// = "https://techfios.com/api-prod/api/product";
	SoftAssert  softAssert;
	
	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
	   softAssert = new SoftAssert();
	}
	
	
	@Test
	public void readOneProducts(){
	Response response =	
		given()
			 .baseUri(baseURI)
		     .header("Content-Type","application/json")
		     .auth().preemptive().basic("demo@techfios.com", "abc123")
		     .queryParam("id","6209").
	    when()
		     .get("/read_one.php").
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
	   softAssert.assertEquals(responseHeaderContentType,"application/json","Response headers are not matching");
	   System.out.println("Response Header ContentType:" + responseHeaderContentType);
	   
	  
	   /*{
		    "id": "6209",
		    "name": "Amazing Headset 1.0 By MD",
		    "description": "The best Headset for amazing programmers.",
		    "price": "199",
		    "category_id": "2",
		    "category_name": "Electronics"
		}*/
	   
	   String responseBody = response.getBody().asString();
	   System.out.println("Response Body: "  + responseBody);
	
	   JsonPath jp = new JsonPath(responseBody);
	   
	   String productName = jp.getString("name");
	   softAssert.assertEquals(productName,"Amazing Headset 1.0 By MD","product name is not matching"); 
	   System.out.println("Product Name :" + productName);
	   
	   String productDescription = jp.getString("description");
	   softAssert.assertEquals(productDescription,"The best Headset for amazing programmers.","Description is not matching");// to display error message
	   //softAssert.assertEquals(productDescription,"The best Headset for amazing programmers.","Description is not matching");
	   System.out.println("Product Description :" + productDescription);
	  
	  
	   
	   String productPrice = jp.getString("price");
	   softAssert.assertEquals(productPrice,"199","price is not matching");
	   System.out.println("Product Price :" + productPrice);
	   
	   String category_Id = jp.getString("category_id");
	   softAssert.assertEquals(category_Id,"2","Categoary is not matching");
	   System.out.println("Product Category_Id :" + category_Id);
	   
	   
	   String category_Name = jp.getString("category_name");
	   softAssert.assertEquals(category_Name,"Electronics","category name is not matching");
	   System.out.println("Product Category_Name :" + category_Name);
	  
	   softAssert.assertAll();
	}
	

}

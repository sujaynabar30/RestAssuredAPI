package com.psl.demo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.jayway.restassured.path.json.JsonPath.from;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.xpath.XPath;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SimpleClass {
	
	@BeforeTest
	public void before()
	{
		RestAssured.proxy("goaproxy.persistent.co.in", 8080);
	}
	
	//@Test
	public void test1()
	{
		given().
			get("https://reqres.in/api/users?page=2").			//to get the status code
		then().
			statusCode(200);
	}
	
	//@Test
	public void test2()
	{
		given().
			get("https://reqres.in/api/users?page=2").
		then().													//to log all (complete info about http response)
			statusCode(200).
			log().all();
	}
	
	//@Test
	public void test3() {
		
		given().
			get("http://services.groupkt.com/country/get/iso2code/us").
		then().																//to check string equal to
			body("RestResponse.result.name", equalTo("United States of America"));
		
	}
	
		//@Test
		public void test4() {
			
			given().
				get("http://services.groupkt.com/country/get/all").
			then().													//verify mutiple content
				body("RestResponse.result.name", hasItems("Afghanistan","Anguilla","Aruba"));
			
		}
		
		//@Test
		public void test5()
		{
			given().								//parameters and headers are set
			        param("key1", "value1").			
			        header("headA", "valueA").
			when().
			        get("http://services.groupkt.com/country/get/all").
			then().
			        statusCode(200).
			        log().all();
		}
		
		//@Test
		public void test6() {
			//using and() to increase readablity of the code and used when writing in one line xpath style
			given().param("key1", "value1").and().header("key2", "value2").when().get("http://services.groupkt.com/country/get/iso2code/cn").then().statusCode(200).and().body("RestResponse.result.alpha3_code", equalTo("CHN"));
		}
		
		//@Test
		public void test7() {
			
			given().
				get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/10/").		//xml 
			then().
				body("CUSTOMER.ID", equalTo("10")).
				body("CUSTOMER.FIRSTNAME", equalTo("Sue")).
				body("CUSTOMER.LASTNAME", equalTo("Fuller")).
				body("CUSTOMER.STREET", equalTo("135 Upland Pl.")).
				body("CUSTOMER.CITY", equalTo("Dallas"));
			//log().all();
				
		}
		
		//@Test
		public void test8() {
			given().															//checking if text is equal to 135....
					get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/10/").
			then().															
					body("CUSTOMER.text()", equalTo("135 Upland Pl."));
			//log().all();
		}
		
		//@Test
		public void test9() {
			given().
				get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/10/").			//xpath
			then().
				body(hasXPath("/CUSTOMER/FIRSTNAME"), containsString("sue"));
		}
		
		//@Test
		public void test10() {
			
			given().															//xpath with text=40
					get("http://www.thomas-bayer.com/sqlrest/INVOICE").
			then().
					body(hasXPath("/INVOICEList/INVOICE[text()='40']")).log().all();
		}
			
		//@Test
		public void test11()
		{
			given().
			    get("http://services.groupkt.com/country/get/iso3code/ita").
			then().		    
			        body("RestResponse.result.name", is("Italy")).						//checking if is(...) is present or not
			        body("RestResponse.result.alpha2_code", is("IT")).
			        body("RestResponse.result.alpha3_code", is("ITA"));
			        
		}

		//@Test
		public void test12()
		{
			given().
					get("http://services.groupkt.com/country/get/iso3code/ita").
			then().
			        root("RestResponse.result"). 			//root is set static so we can use directly unlike previous test case
			        body("name", is("Italy")).
			        body("alpha2_code", is("IT")).
			        body("alpha3_code", is("ITA"));
			        
		}
		
		//@Test
		public void test13()
		{
			given().
			    	get("http://services.groupkt.com/country/get/iso3code/ita").
			then().
			        root("RestResponse.result"). 
			        body("name", is("Italy")).
			        body("alpha2_code", is("IT")).
			        detachRoot("result").								//removing the root and again attaching
			        body("result.alpha3_code", is("ITA"));
			        
		}
	
		//@Test
		public void test14()
		{
			given().
			        headers("AppKey", "Key-Value").
			        param("wfsFirst_name", "first").			//setting header and parameters and checking status code	
			        param("wfsFirst_name", "last").
			        param("wfsemail", "test@test.com").
			when().
			        get("http://api.fonts.com/rest/json/Accounts").
			then().
			        statusCode(401).
			        log().all();
		}
	
		//@Test
		public void test15()
		{
			String response = get("http://services.groupkt.com/country/search?text=lands").asString();
			System.out.println("Response "+response);			//getting josn to string	
		}
		
		//@Test
		public void test16() throws IOException
		{
			InputStream response = get("http://services.groupkt.com/country/search?text=lands").asInputStream();
			System.out.println("Response "+response.toString().length());
			 response.close();						//as input string
		}
	
		//@Test
		public void test17()
		{
			byte[] response = get("http://services.groupkt.com/country/search?text=lands").asByteArray();
			System.out.println("Response "+response.length);
																	//asByteArray
		}
		
		//@Test
		public void test18()
		{
			String href = 
				when().
					get("http://jsonplaceholder.typicode.com/photos/1").
				then(). 
					contentType(ContentType.JSON). 
					body("albumId", equalTo("1")).  
				extract().
					path("url");					//fetching url and storing it href and run url insted of actual http://jsonplaceholder.typicode.com/photos/1 url
			
			System.out.println(href);
			
			when().get(href).then().statusCode(200);
				
		}
		
		//@Test
		public void test19()
		{
			//type1
			String href = get("http://jsonplaceholder.typicode.com/photos/1").path("thumbnailUrl");			
			System.out.println("Fetched URL 1 "+href);	
			when().get(href).then().statusCode(200);
			
			//type2
			String href2 = get("http://jsonplaceholder.typicode.com/photos/1").andReturn().jsonPath().getString("thumbnailUrl");		
			System.out.println("Fetched URL 1 "+href2);	
			when().get(href2).then().statusCode(200);						//2nd way
			
		}
		
		//@Test
		public void test20()
		{
			Response response = 
				when().
					get("http://jsonplaceholder.typicode.com/photos/1").
				then().  
				extract().
					response();
			
			System.out.println("Content Type :"+response.contentType());		//extracting and storing it in response and afterwards we can do operations on it
			System.out.println("href :"+response.path("url"));
			System.out.println("Status code :"+response.statusCode());
				
		}
	
		//@Test
		public void test21()
		{
			given().
		    	get("http://services.groupkt.com/country/get/iso3code/cn").
		    then().
		    	statusCode(200). 
		    	contentType(ContentType.JSON);
				//contentType(ContentType.HTML);
				//ContentType(ContentType.XML);
		}
		
		/*@Test
		public void testschema() {
			
			given(). 
				get("http://geo.groupkt.com/ip/172.217.4.14/json").
			then(). 
				assertThat().body(matchesJsonSchemaInClasspath("test3_geo_schema123.json"));
			
		}*/
	
		//@Test
		public void test22()
		{
			given().
				get("http://services.groupkt.com/country/search?text=islands").
			then().
				body("RestResponse.result.name", hasItems("Cayman Islands","Northern Mariana Islands"));
		}
		
		//@Test
		public void test23()
		{
			given().
				get("http://services.groupkt.com/country/search?text=islands").
			then().
				body("RestResponse.result.alpha3_code*.length().sum()", greaterThan(10));		//grovee feature
					//it founds out length of all alphacode3 and adds them up
		}
	
		//@Test
		public void test24()
		{
			String response = get("http://services.groupkt.com/country/search?text=lands").asString();
			
			List<String> list = from(response).getList("RestResponse.result.name");
			
			System.out.println("List size is "+list.size());
			
			for(String country : list)								//it will store all in list and iterating over it 
			{
				if(country.equals("Solomon Islands"))
					System.out.println("place is found");
			}
				
		}
	
	@Test
	public void test25()
	{
		String response = get("http://services.groupkt.com/country/search?text=lands").asString();
		List<String> list = from(response).getList("RestResponse.result.findAll { it.name.length()>40 }.name");
		System.out.println(list);
															//similarly it will find all names which is >40
	}		
	
	
}

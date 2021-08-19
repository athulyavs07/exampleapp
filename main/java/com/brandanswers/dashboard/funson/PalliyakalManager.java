package com.brandanswers.dashboard.funson;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.brandanswers.dashboard.orchestrator.Func;
public class PalliyakalManager extends BaseDataAccessFunson {
	
	public static JSONObject getDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData("getDetails", params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> listResult = new ArrayList<JSONObject>();
					try {
						while(result.next()) {
							JSONObject lastDigit = new JSONObject();
							lastDigit.put("lastDigit",
									result.getString("code").substring(1,result.getString("code").length()));
							listResult.add(lastDigit);
              }
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
					
						JSONObject lastDigit = new JSONObject();
						listResult.add(lastDigit.put("lastDigit", "99"));
					Collections.sort(listResult, new Comparator<JSONObject>() {
						@Override
						public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
							int compare = 0;
							Integer keyA = jsonObjectA.getInt("lastDigit");
							Integer keyB = jsonObjectB.getInt("lastDigit");
							compare = Integer.compare(keyA, keyB);
							return compare;
						}
					});
					
				JSONObject id = listResult.get(listResult.size() - 1);
			
				Integer idLastPart = Integer.parseInt(id.getString("lastDigit")) + 1;
				innerObj.put("Response", idLastPart);
				
			return innerObj;

		});
		return obj;
	}

	public static JSONObject getExistingDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj =  getData("getCode", params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				while(result.next()) {
				innerObj.put("code", result.getString("code"));
				}
			} catch (JSONException | SQLException e) {
				e.printStackTrace();
			}
			return innerObj;

		});
		return obj;
	}
	
	public static JSONObject uploadFile(JSONObject params, Func functiondetails) throws IOException, JSONException, ParseException {
		//getExistingDetails( params,functiondetails);
		BufferedReader lineReader = new BufferedReader(new FileReader(params.getString("file")));
		params.remove("file");
        String lineText = null;
        int count = 0;
        JSONObject obj= new JSONObject();
        lineReader.readLine(); // skip header line
        HashSet<String> crunchifyAllLines = new HashSet<>();
        while ((lineText = lineReader.readLine()) != null) {
        	if (crunchifyAllLines.add(lineText)) {
            String[] data = lineText.split(",");
            String pattern = "yyyy-MM-dd";
            String token;
            String code = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
            params.put("date",simpleDateFormat.format(fromUser.parse(data[0]))) ;
            params.put("Sghtype",data[1]);
            JSONObject params2=new JSONObject();
            params2.put("shgType", data[1]);
            JSONObject details=getDetails(params2,functiondetails);
            
            JSONObject params3=new JSONObject();
            params3.put("shgArea", data[2]);
            params3.put("farmername", data[4]);
            params3.put("date", simpleDateFormat.format(fromUser.parse(data[0])));
            params3.put("product", data[3]);
            JSONObject existingCode=getExistingDetails(params3,functiondetails);
            if (existingCode.isEmpty()) {
            if(params.getString("Sghtype").equals("Egg")) {
            	token="E";
            	code =token+details.get("Response").toString();
            }
            if(params.getString("Sghtype").equals("Milk")) {
            	token="M";
            	code =token+details.get("Response").toString();
            }
            if(params.getString("Sghtype").equals("Fruits & Vegetables")) {
            	token="V";
            	code =token+details.get("Response").toString();
            }}
            else {
            	code=existingCode.getString("code");
            }
            params.put("code", code);
            params.put("Sgharea",data[2]);
            params.put("product", data[3]);
            params.put("farmerName",data[4]);
          System.out.println(params);  
		 obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
        }
        }
		return obj;
        
	}
	
	
	public static JSONObject getAllDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
				try {
					while (result.next()) {
						JSONObject resObj =new JSONObject();
						resObj.put("date", result.getDate("date"));
						resObj.put("farmerName", result.getString("farmername"));
						resObj.put("shgType", result.getString("shg_type"));
						resObj.put("shgArea", result.getString("shg_area"));
						resObj.put("product", result.getString("product"));
						resObj.put("code", result.getString("code"));
						resObj.put("qrCode", result.getString("qrcode"));
						resultList.add(resObj);
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
				Collections.sort(resultList, new Comparator<JSONObject>() {
					@Override
					public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
						int compare = 0;
						String keyA = jsonObjectA.getString("code");
						String keyB = jsonObjectB.getString("code");
						compare = keyA.compareTo(keyB);
						return compare;
					}
				});

			innerObj.put("customerDetails", resultList);
			return innerObj;

		});
		return obj;
	}
	
	public static JSONObject getIndividualDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
				try {
					while (result.next()) {
						innerObj.put("date", result.getDate("date"));
						innerObj.put("farmerName", result.getString("farmername"));
						innerObj.put("shgType", result.getString("shg_type"));
						innerObj.put("shgArea", result.getString("shg_area"));
						innerObj.put("product", result.getString("product"));
						innerObj.put("code", result.getString("code"));
						innerObj.put("qrCode", result.getString("qrcode"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	
	public static JSONObject insertQR(JSONObject params, Func functiondetails) throws IOException {
		
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				while(result.next()) {
					innerObj.put("qrCode", result.getString("qrcode"));
					innerObj.put("link", result.getString("link"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return innerObj;
		});
		return obj;
	}


}

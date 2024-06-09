package com.ngwisefood.app.utility.andyorm;




import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by andy on 20/5/21.
 *
 * Version 4.6
 *
 */
public enum ApiQueryOrm {

    INSTANCE;

    public String storeValue = "";
    public AndyOrm andyOrm = new AndyOrm();
    JsonParser jsonParser = new JsonParser();
    Gson gson = new Gson();


    public String getResults(String jsonString) {

        String results = "";


        JsonElement jsonElement = this.jsonParser.parse(jsonString);
        JsonObject jsonDataObject = jsonElement.getAsJsonObject();
        JsonObject resultObject = new JsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonDataObject.getAsJsonObject().entrySet();

        for (Map.Entry<String, JsonElement> entry : entrySet) {
            //System.out.println(entry.getKey());
            //System.out.println(entry.getValue());



            JsonObject queryObject = entry.getValue().getAsJsonObject();
            results = this.getSqlResults(queryObject);

            resultObject.add(entry.getKey(),this.jsonParser.parse(results));
        }




        return resultObject.toString();
    }

    private String getSqlResults(JsonObject queryObject){

        String results = "";
        List parameterList = new ArrayList();

        if(queryObject.has("get")){
            JsonObject sqlQueryObj = queryObject.get("get").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            JsonArray parameters = sqlQueryObj.get("parameterList").getAsJsonArray();

            this.storeParameterList(parameterList,parameters);

            JsonArray jsonArray = this.andyOrm.getResult(sqlQueryString,parameterList);
            results = gson.fromJson(jsonArray,String.class);

        }else if(queryObject.has("insert")){

            JsonObject sqlQueryObj = queryObject.get("insert").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            JsonArray parameters = sqlQueryObj.get("parameterList").getAsJsonArray();

            this.storeParameterList(parameterList,parameters);

            boolean isSuccess = this.andyOrm.insert(sqlQueryString,parameterList);
            if(isSuccess){
                results = "1";
            }else{
                results = "0";
            }
        }else if(queryObject.has("insertMultiple")){

            JsonObject sqlQueryObj = queryObject.get("insertMultiple").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();

            List<List> parameterLists = new ArrayList();

            JsonArray jsonParameterLists = sqlQueryObj.get("parameterList").getAsJsonArray();

            for (JsonElement elementParameterList: jsonParameterLists) {

                JsonArray parameters = elementParameterList.getAsJsonArray();
                List tempParameterList = new ArrayList();
                this.storeParameterList(tempParameterList,parameters);
                parameterLists.add(tempParameterList);

            }

            boolean isSuccess = this.andyOrm.insertMultiple(sqlQueryString,parameterLists);
            if(isSuccess){
                results = "1";
            }else{
                results = "0";
            }

        }else if(queryObject.has("insertGetId")){

            JsonObject sqlQueryObj = queryObject.get("insertGetId").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            JsonArray parameters = sqlQueryObj.get("parameterList").getAsJsonArray();

            this.storeParameterList(parameterList,parameters);

            long returnId = this.andyOrm.insertGetId(sqlQueryString,parameterList);

            return Long.toString(returnId);

        }else if(queryObject.has("update")){

            JsonObject sqlQueryObj = queryObject.get("update").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            JsonArray parameters = sqlQueryObj.get("parameterList").getAsJsonArray();

            this.storeParameterList(parameterList,parameters);

            if(this.andyOrm.update(sqlQueryString,parameterList)){
                return "1";
            }else{
                return "0";
            }


        }else if(queryObject.has("delete")){

            JsonObject sqlQueryObj = queryObject.get("delete").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            JsonArray parameters = sqlQueryObj.get("parameterList").getAsJsonArray();

            this.storeParameterList(parameterList,parameters);

            if(this.andyOrm.delete(sqlQueryString,parameterList)){
                return "1";
            }else{
                return "0";
            }


        }else if(queryObject.has("sum")){
            return this.retriveSingleValue(parameterList,queryObject,"sum");
        }else if(queryObject.has("count")){
            return this.retriveSingleValue(parameterList,queryObject,"count");
        }else if(queryObject.has("avg")){
            return this.retriveSingleValue(parameterList,queryObject,"avg");
        }else if(queryObject.has("min")){
            return this.retriveSingleValue(parameterList,queryObject,"min");
        }else if(queryObject.has("max")){
            return this.retriveSingleValue(parameterList,queryObject,"max");
        }else if(queryObject.has("insert")){
            return this.retriveSingleValue(parameterList,queryObject,"insert");
        }else if(queryObject.has("executeQuery")){

            JsonObject sqlQueryObj = queryObject.get("executeQuery").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            return this.andyOrm.executeQuery(sqlQueryString);

        }else if(queryObject.has("execute")){

            JsonObject sqlQueryObj = queryObject.get("execute").getAsJsonObject();
            String sqlQueryString = sqlQueryObj.get("query").getAsString();
            return this.andyOrm.execute(sqlQueryString);

        }


        return results;
    }

    private String retriveSingleValue(List parameterList, JsonObject queryObject, String functionName){
        JsonObject sqlQueryObj = queryObject.get(functionName).getAsJsonObject();
        String sqlQueryString = sqlQueryObj.get("query").getAsString();
        JsonArray parameters = sqlQueryObj.get("parameterList").getAsJsonArray();

        this.storeParameterList(parameterList,parameters);

        return this.andyOrm.sum(sqlQueryString,parameterList);
    }

    private void storeParameterList(List paratemerList, JsonArray parameters){
        for (JsonElement parameter: parameters) {
            if(parameter.isJsonNull()){
                paratemerList.add(null);
            }else if(parameter.getAsJsonPrimitive().isBoolean()){
                paratemerList.add(parameter.getAsBoolean());
            }else if(parameter.getAsJsonPrimitive().isNumber()){
                paratemerList.add(parameter.getAsDouble());
            }else{
                paratemerList.add(parameter.getAsString());
            }
        }
    }


}

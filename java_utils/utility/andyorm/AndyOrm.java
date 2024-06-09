package com.ngwisefood.app.utility.andyorm;

import com.google.gson.*;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andy on 24/10/17.
 *
 * @version 4.6
 *
 */
public class AndyOrm {


    public String hostName = "localhost";
    public String driverName = "mysql";
    private String DB_URL = "";
    private String database = "";
    private String port = "";
    private String username = "";
    private String password = "";
    private Connection connection = null;


    public String errorMessage = "";
    public Gson gson = new Gson();
    public JsonParser jsonParser = new JsonParser();
    // ===============================





    public AndyOrm() {

    }


    public void init(String database, String username, String password) {
        init(driverName, hostName,port, database, username, password);
    }

    public void init(String driverName, String hostName, String port, String database, String username, String password) {

        this.driverName = driverName;
        this.hostName = hostName;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        // standard
        //String DB_URL = "jdbc:mysql://127.0.0.1:3306/" + database;

        String portString = "";
        if(port.isEmpty() == false){
            portString = ":" + port;
        }

        DB_URL = "jdbc:" + driverName.toLowerCase() + "://" + hostName + portString + "/" + database;

    }

    public void reconnect(){
        try{
            connection = DriverManager.getConnection(DB_URL, username, password);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void appendMysqlSetting(){
        DB_URL += "?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8";
    }

    private void checkConnection(){
        try {
            if(connection == null || connection.isClosed()){
                System.out.println("New DB Connection");
                if(driverName.equals("mysql")){
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                }
                connection = DriverManager.getConnection(DB_URL, username, password);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }


    /**
     *
     * @param dbUrl
     *
     * <p>DB_URL Samples : </P>
     * <p>- jdbc:mysql://localhost:3306/DBName</p>
     * <p>- jdbc:sqlite:C:/sqlite/db/DBName.db</p>
     *
     * @param username
     * @param password
     *
     *
     *
     */
    public void initWithDBURL(String dbUrl,String username, String password){
        this.DB_URL = dbUrl;
        this.username = username;
        this.password = password;
    }


    public QueryOrm table(String tableName) {

        //QueryOrm queryOrm = getQueryOrm();
        QueryOrm queryOrm = new QueryOrm(AndyOrm.this);

        queryOrm.table(tableName);

        return queryOrm;


    }

    public QueryOrm createQueryOrm(){
        QueryOrm queryOrm = new QueryOrm(AndyOrm.this);
        return queryOrm;
    }





    /**
     * @param sqlQuery
     * @param parameterList
     * @return
     */
    public JsonArray getResult(String sqlQuery, List parameterList) {
        JsonArray results = new JsonArray();

        //Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {

            // Check and Open a connection
            checkConnection();



            // Execute SQL query
            // statement = connection.createStatement();
            // resultSet = statement.executeQuery(sqlQuery);
            // parameterize solved the sql injection
            preparedStatement = connection.prepareStatement(sqlQuery);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            resultSet = preparedStatement.executeQuery();


            ResultSetMetaData columns = resultSet.getMetaData();
            int totalColumn = columns.getColumnCount();
            Map<String, String> columnInfo = new HashMap();

            int i = 1;
            while (i <= totalColumn) {
                //System.out.println(columns.getColumnName(i));
                //System.out.println(columns.getColumnTypeName(i));
                //columnInfo.put(columns.getColumnName(i), columns.getColumnTypeName(i));
                columnInfo.put(columns.getColumnLabel(i), columns.getColumnTypeName(i));
                i++;
            }

            buildResult(results,resultSet,columnInfo);


        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            //e.printStackTrace();
            System.out.println(e.toString());

        }finally {
            closeResultSet(resultSet);
            closePrepareStatement(preparedStatement);
        }

        return results;
    }

    public void buildResult(JsonArray results,ResultSet resultSet,Map<String, String> columnInfo) throws SQLException {

        while (resultSet.next()) {
            JsonObject result = new JsonObject();

            for (Map.Entry<String, String> entry : columnInfo.entrySet()) {

                switch (entry.getValue()) {
                    case "TIMESTAMP":
                    case "DATETIME":
                        String dateTimeString = resultSet.getString(entry.getKey());
                        if (dateTimeString == null) {
                            result.addProperty(entry.getKey(), "");
                        } else {
                            dateTimeString = dateTimeString.substring(0, 19);
                            result.addProperty(entry.getKey(), dateTimeString);
                        }

                        break;
                    case "TEXT":
                    case "DATE":
                    case "TIME":
                    case "VARCHAR":
                        String strValue = resultSet.getString(entry.getKey());
                        if (strValue == null) {
                            result.addProperty(entry.getKey(), "");
                        } else {
                            result.addProperty(entry.getKey(), strValue);
                        }
                        break;
                    case "INTEGER": // For Sqlite only
                        long longValue = resultSet.getLong(entry.getKey());
                        result.addProperty(entry.getKey(), longValue);
                        break;
                    case "INT":
                        int intValue = resultSet.getInt(entry.getKey());
                        result.addProperty(entry.getKey(), intValue);
                        break;
                    case "TINYINT":
                        boolean blnValue = resultSet.getBoolean(entry.getKey());
                        result.addProperty(entry.getKey(), blnValue);
                        break;
                    case "NUMERIC":
                    case "REAL":
                    case "DOUBLE":
                    case "DECIMAL":
                        double dblValue = resultSet.getDouble(entry.getKey());
                        result.addProperty(entry.getKey(), dblValue);
                        break;
                    case "BIGINT":
                        long lngValue = resultSet.getLong(entry.getKey());
                        result.addProperty(entry.getKey(), lngValue);
                        break;
                    case "FLOAT":
                        float fltValue = resultSet.getFloat(entry.getKey());
                        result.addProperty(entry.getKey(), fltValue);
                        break;
                    case "BOOLEAN":
                        boolean booleanValue = resultSet.getBoolean(entry.getKey());
                        result.addProperty(entry.getKey(), booleanValue);
                        break;
                    default:
                        //System.out.println(entry.getValue());
                        String strDefaultValue = resultSet.getString(entry.getKey());
                        if (strDefaultValue == null) {
                            result.addProperty(entry.getKey(), "");
                        } else {
                            result.addProperty(entry.getKey(), strDefaultValue);
                        }
                        break;
                }

            }
            results.add(result);
        }
        resultSet.close();




    }




    public boolean insert(String sqlQuery, List parameterList) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {

            // Check and Open a connection
            checkConnection();


            preparedStatement = connection.prepareStatement(sqlQuery);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            preparedStatement.execute();
            success = true;

            closePrepareStatement(preparedStatement);

        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            System.out.println(e.toString());
            success = false;

            closePrepareStatement(preparedStatement);

        }

        return success;
    }

    public boolean insertMultiple(String sqlQuery, List<List> parameterLists) {
        boolean success = false;

        PreparedStatement preparedStatement = null;

        try {



            // Check and Open a connection
            checkConnection();


            preparedStatement = connection.prepareStatement(sqlQuery);


            if (parameterLists != null) {

                int i = 0;
                int size = parameterLists.size();
                for (List parameterList : parameterLists) {
                    int parameterIndex = 1;
                    for (Object object : parameterList) {
                        preparedStatement.setObject(parameterIndex, object);
                        parameterIndex++;
                    }
                    preparedStatement.addBatch();
                    i++;

                    if (i % 500 == 0 || i == size) {
                        preparedStatement.executeBatch();
                    }
                }
            }


            success = true;
            closePrepareStatement(preparedStatement);

        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            System.out.println(e.toString());
            success = false;

            closePrepareStatement(preparedStatement);
        }

        return success;
    }

    private void closePrepareStatement(PreparedStatement preparedStatement){
        if(preparedStatement == null){
            return;
        }

        try{
            preparedStatement.close();
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }

    private void closeResultSet(ResultSet resultSet){
        if(resultSet == null){
            return;
        }

        try{
            resultSet.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeStatement(Statement statement){
        if(statement == null){
            return;
        }
        try{
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public long insertGetId(String sqlQuery, List parameterList) {

        long id = 0;
        PreparedStatement preparedStatement = null;

        try {


            ResultSet resultSet = null;


            // Check and Open a connection
            checkConnection();


            preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            id = resultSet.getLong(1);

            resultSet.close();
            closePrepareStatement(preparedStatement);
        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            System.out.println(e.toString());

            closePrepareStatement(preparedStatement);
        }

        return id;
    }

    public boolean update(String sqlQuery, List parameterList) {
        boolean success = false;

        PreparedStatement preparedStatement = null;
        try {

            // Check and Open a connection
            checkConnection();


            preparedStatement = connection.prepareStatement(sqlQuery);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            preparedStatement.executeUpdate();
            success = true;

            closePrepareStatement(preparedStatement);
        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println(e.toString());
            success = false;
            closePrepareStatement(preparedStatement);
        }

        return success;
    }

    public boolean delete(String sqlQuery, List parameterList) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {




            // Check and Open a connection
            checkConnection();



            preparedStatement = connection.prepareStatement(sqlQuery);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            preparedStatement.executeUpdate();
            success = true;

            closePrepareStatement(preparedStatement);
        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println(e.toString());
            success = false;
            closePrepareStatement(preparedStatement);
        }

        return success;

    }

    public String sum(String sqlQuery, List parameterList) {
        String result = "0";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {





            // Check and Open a connection
            checkConnection();


            preparedStatement = connection.prepareStatement(sqlQuery);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            result = resultSet.getString(1);

            if(result == null){
                result = "0";
            }

            closeResultSet(resultSet);
            closePrepareStatement(preparedStatement);
        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println(e.toString());
            closeResultSet(resultSet);
            closePrepareStatement(preparedStatement);
        }


        return result;
    }



    public String getSimpleExecuteQuery(String sqlQuery, Object...objects) {
        String result = "0";
        PreparedStatement preparedStatement = null;
        try {


            ResultSet resultSet = null;


            // Check and Open a connection
            checkConnection();


            preparedStatement = connection.prepareStatement(sqlQuery);


            int parameterIndex = 1;
            for (Object object : objects) {
                preparedStatement.setObject(parameterIndex, object);
                parameterIndex++;
            }


            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            result = resultSet.getString(1);

            resultSet.close();
            closePrepareStatement(preparedStatement);

        } catch (Exception e) {

            errorMessage = e.getMessage();
            System.out.println(e.toString());
            closePrepareStatement(preparedStatement);
        }


        return result;
    }

    /**
     * @param sqlQuery
     * @param
     * @return
     */
    public String getSimpleResult(String sqlQuery, Object...objects) {
        String resultString = "";

        //Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {

            // Check and Open a connection
            checkConnection();



            // Execute SQL query
            // statement = connection.createStatement();
            // resultSet = statement.executeQuery(sqlQuery);
            // parameterize solved the sql injection
            preparedStatement = connection.prepareStatement(sqlQuery);

            if (objects != null) {
                int parameterIndex = 1;
                for (Object object : objects) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            resultSet = preparedStatement.executeQuery();


            ResultSetMetaData columns = resultSet.getMetaData();
            int totalColumn = columns.getColumnCount();
            Map<String, String> columnInfo = new HashMap();

            int i = 1;
            while (i <= totalColumn) {
                //System.out.println(columns.getColumnName(i));
                //System.out.println(columns.getColumnTypeName(i));
                //columnInfo.put(columns.getColumnName(i), columns.getColumnTypeName(i));
                columnInfo.put(columns.getColumnLabel(i), columns.getColumnTypeName(i));
                i++;
            }

            JsonArray results = new JsonArray();

            buildResult(results,resultSet,columnInfo);

            resultString = gson.toJson(results);

            closePrepareStatement(preparedStatement);

        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            //e.printStackTrace();
            System.out.println(e.toString());
            closePrepareStatement(preparedStatement);
        }

        return resultString;
    }

    public String execute(String sqlQuery){
        Statement statement = null;
        try{
            // Check and Open a connection
            checkConnection();

            // Execute SQL query
            statement = connection.createStatement();
            statement.execute(sqlQuery);

            statement.close();

            return "1";
        }catch (Exception e){
            e.printStackTrace();
            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            closeStatement(statement);
        }
        return "0";
    }

    public String executeQuery(String sqlQuery){

        Statement statement = null;
        ResultSet resultSet = null;
        try{
            // Check and Open a connection
            checkConnection();

            // Execute SQL query
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
            statement.close();

            resultSet.next();
            String result = resultSet.getString(1);
            resultSet.close();

            return result;

        }catch (Exception e){
            e.printStackTrace();
            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            closeResultSet(resultSet);
            closeStatement(statement);

        }

        return "";
    }

    public void resizeDatabase(){
        String sqlQuery = "vacuum";
        executeQuery(sqlQuery);
    }

    public String getResultByRawQuery(String sqlQuery) {
        String resultString = "";

        //Statement statement = null;
        ResultSet resultSet = null;


        try {

            // Check and Open a connection
            checkConnection();



            // Execute SQL query
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
            statement.close();

            ResultSetMetaData columns = resultSet.getMetaData();
            int totalColumn = columns.getColumnCount();
            Map<String, String> columnInfo = new HashMap();

            int i = 1;
            while (i <= totalColumn) {
                //System.out.println(columns.getColumnName(i));
                //System.out.println(columns.getColumnTypeName(i));
                //columnInfo.put(columns.getColumnName(i), columns.getColumnTypeName(i));
                columnInfo.put(columns.getColumnLabel(i), columns.getColumnTypeName(i));
                i++;
            }

            JsonArray results = new JsonArray();
            buildResult(results,resultSet,columnInfo);

            resultString = gson.toJson(results);

        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            //e.printStackTrace();
            System.out.println(e.toString());
        }

        return resultString;
    }

    /**
     *
     * @param sqlQuery
     * @param parameterList
     * @return null if not found
     */
    public JsonObject getSingleResult(String sqlQuery, List parameterList){
        JsonObject result = new JsonObject();

        //Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {

            // Check and Open a connection
            checkConnection();



            // Execute SQL query
            // statement = connection.createStatement();
            // resultSet = statement.executeQuery(sqlQuery);
            // parameterize solved the sql injection
            preparedStatement = connection.prepareStatement(sqlQuery);

            if (parameterList != null) {
                int parameterIndex = 1;
                for (Object object : parameterList) {
                    preparedStatement.setObject(parameterIndex, object);
                    parameterIndex++;
                }
            }

            resultSet = preparedStatement.executeQuery();
            //closePrepareStatement(preparedStatement);

            ResultSetMetaData columns = resultSet.getMetaData();
            int totalColumn = columns.getColumnCount();
            Map<String, String> columnInfo = new HashMap();

            resultSet.next();

            int i = 1;
            while (i <= totalColumn) {
                //System.out.println(columns.getColumnName(i));
                //System.out.println(columns.getColumnTypeName(i));
                //columnInfo.put(columns.getColumnName(i), columns.getColumnTypeName(i));
                columnInfo.put(columns.getColumnLabel(i), columns.getColumnTypeName(i));
                i++;
            }

            for (Map.Entry<String, String> entry : columnInfo.entrySet()) {

                switch (entry.getValue()) {
                    case "TIMESTAMP":
                    case "DATETIME":
                        String dateTimeString = resultSet.getString(entry.getKey());
                        if (dateTimeString == null) {
                            result.addProperty(entry.getKey(), "");
                        } else {
                            dateTimeString = dateTimeString.substring(0, 19);
                            result.addProperty(entry.getKey(), dateTimeString);
                        }

                        break;
                    case "DATE":
                    case "TIME":
                    case "VARCHAR":
                        String strValue = resultSet.getString(entry.getKey());
                        if (strValue == null) {
                            result.addProperty(entry.getKey(), "");
                        } else {
                            result.addProperty(entry.getKey(), strValue);
                        }
                        break;
                    case "INT":
                        int intValue = resultSet.getInt(entry.getKey());
                        result.addProperty(entry.getKey(), intValue);
                        break;
                    case "TINYINT":
                        boolean blnValue = resultSet.getBoolean(entry.getKey());
                        result.addProperty(entry.getKey(), blnValue);
                        break;
                    case "DOUBLE":
                    case "DECIMAL":
                        double dblValue = resultSet.getDouble(entry.getKey());
                        result.addProperty(entry.getKey(), dblValue);
                        break;
                    case "BIGINT":
                        long lngValue = resultSet.getLong(entry.getKey());
                        result.addProperty(entry.getKey(), lngValue);
                        break;
                    case "FLOAT":
                        float fltValue = resultSet.getFloat(entry.getKey());
                        result.addProperty(entry.getKey(), fltValue);
                        break;
                    case "BOOLEAN":
                        boolean booleanValue = resultSet.getBoolean(entry.getKey());
                        result.addProperty(entry.getKey(), booleanValue);
                        break;
                    default:
                        String strDefaultValue = resultSet.getString(entry.getKey());
                        if (strDefaultValue == null) {
                            result.addProperty(entry.getKey(), "");
                        } else {
                            result.addProperty(entry.getKey(), strDefaultValue);
                        }

                        break;
                }

            }

        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.out.println("sqlQuery="+sqlQuery);
            System.out.println(e.toString());
            //e.printStackTrace();

        }finally {

            closeResultSet(resultSet);
            closePrepareStatement(preparedStatement);
        }

        return result;
    }


    public Connection getConnection() {
        return connection;
    }
}

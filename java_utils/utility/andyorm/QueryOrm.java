package com.ngwisefood.app.utility.andyorm;




import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Created by andy on 20/5/21.
 *
 * Version 4.6
 *
 */
public class QueryOrm {


    public int maxRecord;
    public List parameterList;
    public List<List> parameterLists;

    public String sqlString = "";
    public String sqlTable = "";
    public String tableName = "";
    private List<String> sqlSelect;
    private List<Where> sqlWhere;
    private List<OrderBy> sqlOrderBy;
    private List<GroupBy> sqlGroupBy;
    private List<Having> sqlHaving;
    private Limit offsetLimit;
    private List<Join> sqlJoins;
    private Gson gson = new Gson();

    //    private JsonParser jsonParser;
    private AndyOrm andyOrm;


    public QueryOrm(AndyOrm andyOrm){
        this.andyOrm = andyOrm;
        this.maxRecord = 1000;
        this.parameterList = new ArrayList();
        this.parameterLists = new ArrayList<>();


        this.sqlSelect = new ArrayList<>();
        this.sqlWhere = new ArrayList<>();
        this.sqlOrderBy = new ArrayList<>();
        this.sqlGroupBy = new ArrayList<>();
        this.sqlHaving = new ArrayList<>();
        this.offsetLimit = null;
        this.sqlJoins = new ArrayList<>();
    }


    public void init() {
        this.sqlString = "";
        this.parameterList.clear();
        this.sqlSelect.clear();
        this.sqlTable = "";
        this.tableName = "";
        this.sqlWhere.clear();
        this.sqlOrderBy.clear();
        this.sqlGroupBy.clear();
        this.sqlHaving.clear();
        this.offsetLimit = null;
        this.sqlJoins.clear();
    }

    private void clear(){

        this.sqlSelect.clear();
        this.sqlWhere.clear();
        this.sqlOrderBy.clear();
        this.sqlGroupBy.clear();
        this.sqlHaving.clear();
        this.sqlJoins.clear();


        this.offsetLimit = null;
        this.sqlTable = null;
        this.tableName = null;

        this.sqlSelect = null;
        this.sqlWhere = null;
        this.sqlOrderBy = null;
        this.sqlGroupBy = null;
        this.sqlHaving = null;
        this.sqlJoins = null;

        this.andyOrm = null;
    }




    public QueryOrm table(String tableName) {

        this.init();
        this.tableName = tableName;
        this.sqlTable = "From " + tableName + " ";

        return this;
    }

    public QueryOrm select(String... selectStrings) {

        for (String selectString : selectStrings) {
            this.sqlSelect.add(selectString);
        }

        return this;
    }

    public QueryOrm selectDistinct(String field) {

        this.sqlSelect.add("DISTINCT " + field + " ");

        return this;
    }


    public QueryOrm where(String field, Object value) {
        Where<String, String, String, Object, QueryOrm> where = new Where("AND", field, "=", value, this);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm where(String field, String operator, Object value) {
        Where<String, String, String, Object, QueryOrm> where = new Where("AND", field, operator, value, this);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm orWhere(String field, String operator, Object value) {
        Where<String, String, String, Object, QueryOrm> where = new Where("OR", field, operator, value, this);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm orWhere(String field, Object value) {
        Where<String, String, String, Object, QueryOrm> where = new Where("OR", field, "=", value, this);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereNot(String field, String operator, Object value) {
        Where<String, String, String, Object, QueryOrm> where = new Where("NOT", field, operator, value, this);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereIn(String field, List values) {
        if(values.size() <= 0){
            return this;
        }

        String value = "(";

        for (Object object : values) {
            value += "?,";
        }
        value = value.substring(0, value.length() - 1);
        value += ")";

        Where<String, String, String, Object, List> where = new Where("whereIn", field, "IN", value, values);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereNotIn(String field, List values) {
        if(values.size() <= 0){
            return this;
        }

        String value = "(";

        for (Object object : values) {
            value += "?,";
        }
        value = value.substring(0, value.length() - 1);
        value += ")";

        Where<String, String, String, Object, List> where = new Where("whereIn", field, "NOT IN", value, values);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereBetween(String field, double minValue, double maxValue) {

        Where<String, String, String, Double, Double> where = new Where("whereBetween", field, "BETWEEN", minValue, maxValue);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereNotBetween(String field, double minValue, double maxValue) {

        Where<String, String, String, Double, Double> where = new Where("whereBetween", field, " NOT BETWEEN ", minValue, maxValue);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereNull(String field) {

        Where<String, String, String, String, String> where = new Where("whereNull", field, " IS NULL ", "", "");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereNotNull(String field) {

        Where<String, String, String, String, String> where = new Where("whereNull", field, " IS NOT NULL ", "", "");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereDate(String field, String operator, String value) {
        Where<String, String, String, Object, String> where = new Where("whereDate", field, operator, value, "DATE");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereYear(String field, String operator, Integer value) {
        Where<String, String, String, Integer, String> where = new Where("whereDate", field, operator, value, "YEAR");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereMonth(String field, String operator, Integer value) {
        Where<String, String, String, Integer, String> where = new Where("whereDate", field, operator, value, "MONTH");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereDay(String field, String operator, Integer value) {
        Where<String, String, String, Integer, String> where = new Where("whereDate", field, operator, value, "DAY");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereHour(String field, String operator, Integer value) {
        Where<String, String, String, Integer, String> where = new Where("whereDate", field, operator, value, "HOUR");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereMinute(String field, String operator, Integer value) {
        Where<String, String, String, Integer, String> where = new Where("whereDate", field, operator, value, "MINUTE");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereSecond(String field, String operator, Integer value) {
        Where<String, String, String, Integer, String> where = new Where("whereDate", field, operator, value, "SECOND");
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm whereField(String field1, String operator, String field2) {
        Where<String, String, String, Integer, String> where = new Where("whereField", field1, operator, field2, this);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm groupBy(String field) {

        GroupBy<String, String> groupBy = new GroupBy<>(field, "");
        this.sqlGroupBy.add(groupBy);

        return this;
    }

    public QueryOrm having(String field, String operator, Object value) {

        Having having = new Having("AND", field, operator, value);
        this.sqlHaving.add(having);

        return this;
    }

    public QueryOrm orHaving(String field, String operator, Object value) {

        Having having = new Having("OR", field, operator, value);
        this.sqlHaving.add(having);

        return this;
    }

    public QueryOrm offset(int value) {

        if (this.offsetLimit == null) {
            this.offsetLimit = new Limit();
        }
        this.offsetLimit.setOffset(value);

        return this;
    }

    public QueryOrm limit(int value) {
        if (this.offsetLimit == null) {
            this.offsetLimit = new Limit();
        }
        this.offsetLimit.setLimit(value);

        return this;
    }

    public QueryOrm orderBy(String field, String value) {
        String orderType = "";
        if (value.equalsIgnoreCase("ASC")) {
            orderType = "ASC";
        } else {
            orderType = "DESC";
        }

        OrderBy<String, String> orderBy = new OrderBy<>(field, orderType);
        this.sqlOrderBy.add(orderBy);

        return this;
    }

    public QueryOrm orderByAsc(String field) {

        OrderBy<String, String> orderBy = new OrderBy<>(field, "ASC");
        this.sqlOrderBy.add(orderBy);

        return this;
    }

    public QueryOrm orderByDesc(String field) {

        OrderBy<String, String> orderBy = new OrderBy<>(field, "DESC");
        this.sqlOrderBy.add(orderBy);

        return this;
    }

    public QueryOrm orderByRandom() {

        OrderBy<String, String> orderBy = new OrderBy<>("", "RANDOM()");
        this.sqlOrderBy.add(orderBy);

        return this;
    }

    public QueryOrm encloseWhere(QueryOrm queryOrm) {
        String value = queryOrm.getWhereLogic(queryOrm.getSqlWhere());
        value = "AND (" + value.substring(5) + ") ";
        Where<String, String, String, String, QueryOrm> where = new Where("enclose", "", "", value, queryOrm);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm encloseOrWhere(QueryOrm queryOrm) {
        String value = queryOrm.getWhereLogic(queryOrm.getSqlWhere());
        value = "OR (" + value.substring(5) + ") ";
        Where<String, String, String, String, QueryOrm> where = new Where("enclose", "", "", value, queryOrm);
        this.sqlWhere.add(where);

        return this;
    }

    public QueryOrm join(String joinTable, String tableField, String operator, String joinTableField) {

        Join join = new Join(" INNER JOIN ", joinTable, tableField, operator, joinTableField);
        this.sqlJoins.add(join);

        return this;
    }

    public QueryOrm leftJoin(String joinTable, String tableField, String operator, String joinTableField) {

        Join join = new Join(" LEFT JOIN ", joinTable, tableField, operator, joinTableField);
        this.sqlJoins.add(join);

        return this;
    }

    public QueryOrm rightJoin(String joinTable, String tableField, String operator, String joinTableField) {

        Join join = new Join(" RIGHT JOIN ", joinTable, tableField, operator, joinTableField);
        this.sqlJoins.add(join);

        return this;
    }

    public QueryOrm fullJoin(String joinTable, String tableField, String operator, String joinTableField) {

        Join join = new Join(" FULL OUTER JOIN ", joinTable, tableField, operator, joinTableField);
        this.sqlJoins.add(join);

        return this;
    }

    public List<Where> getSqlWhere() {
        return this.sqlWhere;
    }


    public String get() {

        String results = "";

        try{
            JsonArray jsonArray = get(JsonArray.class);
            results = jsonArray.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return results;
    }

    public String getSqlRawQueryString(){
        sqlString = this.getSelectLogic(this.sqlSelect)
                + this.sqlTable
                + this.getJoinLogic(this.sqlJoins)
                + this.getWhereLogic(this.sqlWhere)
                + this.getGroupByLogic(this.sqlGroupBy)
                + this.getHavingLogic(sqlHaving)
                + this.getOrderByLogic(this.sqlOrderBy)
                + this.getOffsetLimitLogic(this.offsetLimit);
        sqlString += ";";

        String rawSqlString = this.sqlString;
        for (Object object : parameterList) {
            rawSqlString = rawSqlString.replaceFirst(Pattern.quote("?"), "'" + object.toString() + "'");
        }

        this.clear();

        return rawSqlString;
    }

    public String first(){

        String result = "";
        try{
            JsonObject jsonObject = first(JsonObject.class);
            result = jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public String sum(String field) {

        this.sqlString = "SELECT SUM(" + field + ") "
                //+ this.sqlTable
                //+ this.getWhereLogic(this.sqlWhere);
                + this.sqlTable
                + this.getJoinLogic(this.sqlJoins)
                + this.getWhereLogic(this.sqlWhere)
                + this.getGroupByLogic(this.sqlGroupBy)
                + this.getHavingLogic(sqlHaving)
                + this.getOrderByLogic(this.sqlOrderBy)
                + this.getOffsetLimitLogic(this.offsetLimit);

        this.sqlString += ";";

        String result = andyOrm.sum(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public int count(String field) {
        if (field.isEmpty()) {
            field = "*";
        }
        this.sqlString = "SELECT COUNT(" + field + ") "
//                + this.sqlTable
//                + this.getWhereLogic(this.sqlWhere);
                + this.sqlTable
                + this.getJoinLogic(this.sqlJoins)
                + this.getWhereLogic(this.sqlWhere)
                + this.getGroupByLogic(this.sqlGroupBy)
                + this.getHavingLogic(sqlHaving)
                + this.getOrderByLogic(this.sqlOrderBy)
                + this.getOffsetLimitLogic(this.offsetLimit);
        this.sqlString += ";";

        String result = andyOrm.sum(this.sqlString, this.parameterList);

        int total = 0;

        try{
            total = Integer.parseInt(result);
        }catch (Exception e){
            //System.out.println(e.toString());
        }

        this.clear();

        return total;
    }

    public String avg(String field) {

        this.sqlString = "SELECT AVG(" + field + ") "
                //+ this.sqlTable
                //+ this.getWhereLogic(this.sqlWhere);
                + this.sqlTable
                + this.getJoinLogic(this.sqlJoins)
                + this.getWhereLogic(this.sqlWhere)
                + this.getGroupByLogic(this.sqlGroupBy)
                + this.getHavingLogic(sqlHaving)
                + this.getOrderByLogic(this.sqlOrderBy)
                + this.getOffsetLimitLogic(this.offsetLimit);
        this.sqlString += ";";

        String result = andyOrm.sum(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public String min(String field) {

        this.sqlString = "SELECT MIN(" + field + ") "
                //+ this.sqlTable
                //+ this.getWhereLogic(this.sqlWhere);
                + this.sqlTable
                + this.getJoinLogic(this.sqlJoins)
                + this.getWhereLogic(this.sqlWhere)
                + this.getGroupByLogic(this.sqlGroupBy)
                + this.getHavingLogic(sqlHaving)
                + this.getOrderByLogic(this.sqlOrderBy)
                + this.getOffsetLimitLogic(this.offsetLimit);
        this.sqlString += ";";

        String result = andyOrm.sum(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public String max(String field) {

        this.sqlString = "SELECT MAX(" + field + ") "
                //+ this.sqlTable
                //+ this.getWhereLogic(this.sqlWhere);
                + this.sqlTable
                + this.getJoinLogic(this.sqlJoins)
                + this.getWhereLogic(this.sqlWhere)
                + this.getGroupByLogic(this.sqlGroupBy)
                + this.getHavingLogic(sqlHaving)
                + this.getOrderByLogic(this.sqlOrderBy)
                + this.getOffsetLimitLogic(this.offsetLimit);
        this.sqlString += ";";

        String result = andyOrm.sum(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public boolean insert(String jsonString) {

        List<String> columnList = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        this.parameterList.clear();

        JsonObject jsonObject = andyOrm.jsonParser.parse(jsonString).getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

        for (Map.Entry<String, JsonElement> entry : entrySet) {

            columnList.add(entry.getKey());
            columnValues.add("?");

            /*
            String value = entry.getValue().getAsJsonPrimitive().getAsString();
            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
                this.parameterList.add(entry.getValue().getAsBoolean());
            }else{
                this.parameterList.add(entry.getValue().getAsString());
            }
            */
            if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                this.parameterList.add(entry.getValue().getAsBoolean());
            } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                this.parameterList.add(entry.getValue().getAsDouble());
            } else {
                this.parameterList.add(entry.getValue().getAsString());
            }
        }

        this.sqlString = "INSERT INTO "
                + this.tableName
                + " (" + String.join(",", columnList) + ") "
                + "VALUES (" + String.join(",", columnValues) + ") ;";


        boolean result = andyOrm.insert(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public void insertMultiple(String jsonString) {
        JsonArray jsonArray = null;
        try{
            jsonArray = andyOrm.jsonParser.parse(jsonString).getAsJsonArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(jsonArray == null){
            return;
        }
        if(jsonArray.size() <= 0){
            return;
        }
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        List<String> columnList = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        List<List> multiParameterList = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : entrySet) {

            columnList.add(entry.getKey());
            columnValues.add("?");

        }

        // Configure the sql query
        String sqlString = "INSERT INTO "
                + this.tableName
                + " (" + String.join(",", columnList) + ") "
                + "VALUES (" + String.join(",", columnValues) + ") ;";

        // Configure the parameter list
        int total = jsonArray.size();
        for (int i = 0; i < total; i++) {
            JsonObject jsonObjectItem = jsonArray.get(0).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySetItem = jsonObjectItem.entrySet();
            List list = new ArrayList();
            for (Map.Entry<String, JsonElement> entry : entrySetItem) {

                if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                    list.add(entry.getValue().getAsBoolean());
                } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                    list.add(entry.getValue().getAsDouble());
                } else {
                    list.add(entry.getValue().getAsString());
                }

            }

            multiParameterList.add(list);
        }


        andyOrm.insertMultiple(sqlString,multiParameterList);

        this.clear();

    }

    public long insertGetId(String jsonString) {

        List<String> columnList = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        parameterList.clear();

        JsonObject jsonObject = andyOrm.jsonParser.parse(jsonString).getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

        for (Map.Entry<String, JsonElement> entry : entrySet) {

            columnList.add(entry.getKey());
            columnValues.add("?");
            String value = entry.getValue().getAsJsonPrimitive().getAsString();
            /*
            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
                this.parameterList.add(entry.getValue().getAsBoolean());
            }else{
                this.parameterList.add(entry.getValue().getAsString());
            }
            */
            if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                parameterList.add(entry.getValue().getAsBoolean());
            } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                parameterList.add(entry.getValue().getAsDouble());
            } else {
                parameterList.add(entry.getValue().getAsString());
            }
        }

        this.sqlString = "INSERT INTO "
                + this.tableName
                + " (" + String.join(",", columnList) + ") "
                + "VALUES (" + String.join(",", columnValues) + ") ;";


        long result = andyOrm.insertGetId(this.sqlString, parameterList);

        this.clear();

        return result;
    }

    public boolean update(String jsonString) {
        List<String> columnList = new ArrayList<>();
        this.parameterList.clear();

        JsonObject jsonObject = andyOrm.jsonParser.parse(jsonString).getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

        for (Map.Entry<String, JsonElement> entry : entrySet) {

            columnList.add(entry.getKey() + " = ? ");
            String value = entry.getValue().getAsJsonPrimitive().getAsString();

            if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                this.parameterList.add(entry.getValue().getAsBoolean());
            } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                this.parameterList.add(entry.getValue().getAsDouble());
            } else {
                this.parameterList.add(entry.getValue().getAsString());
            }

        }

        this.sqlString = "UPDATE "
                + this.tableName
                + " SET " + String.join(", ", columnList)
                + this.getWhereLogic(this.sqlWhere)
                + this.getOffsetLimitLogic(this.offsetLimit);

        boolean result = andyOrm.update(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public boolean delete() {
        this.sqlString = "DELETE FROM "
                + this.tableName + " "
                + this.getWhereLogic(this.sqlWhere)
                + this.getOffsetLimitLogic(this.offsetLimit);

        boolean result = andyOrm.delete(this.sqlString, this.parameterList);

        this.clear();

        return result;
    }

    public <T> T get(Class<T> classOfT){

        try{

            this.sqlString = this.getSelectLogic(this.sqlSelect)
                    + this.sqlTable
                    + this.getJoinLogic(this.sqlJoins)
                    + this.getWhereLogic(this.sqlWhere)
                    + this.getGroupByLogic(this.sqlGroupBy)
                    + this.getHavingLogic(sqlHaving)
                    + this.getOrderByLogic(this.sqlOrderBy)
                    + this.getOffsetLimitLogic(this.offsetLimit);
            this.sqlString += ";";

            JsonArray jsonArray = andyOrm.getResult(this.sqlString, this.parameterList);

            T t = gson.fromJson(jsonArray,classOfT);

            return t;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.clear();
        }

        return null;

    }

    public <T> T first(Class<T> classOfT){
        try{

            this.sqlString = this.getSelectLogic(this.sqlSelect)
                    + this.sqlTable
                    + this.getJoinLogic(this.sqlJoins)
                    + this.getWhereLogic(this.sqlWhere)
                    + this.getGroupByLogic(this.sqlGroupBy)
                    + this.getHavingLogic(sqlHaving)
                    + this.getOrderByLogic(this.sqlOrderBy)
                    + this.getOffsetLimitLogic(this.offsetLimit);
            this.sqlString += ";";

            JsonObject jsonObject = andyOrm.getSingleResult(this.sqlString, this.parameterList);

            //System.out.println(gson.toJson(jsonObject));

            T t = gson.fromJson(jsonObject,classOfT);

            return t;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.clear();
        }

        return null;
    }





    public String getSqlString() {
        return this.sqlString;
    }

    public String getPureSqlString(){
        String pureSqlString = this.sqlString;
        for (Object object : this.parameterList) {
            pureSqlString = pureSqlString.replaceFirst(Pattern.quote("?"), "'" + object.toString() + "'");
        }

        return pureSqlString;
    }







    class OrderBy<F, V> {

        private F field;
        private V value;

        public OrderBy(F field, V value) {
            this.field = field;
            this.value = value;
        }

        public F getField() {
            return field;
        }

        public V getValue() {
            return value;
        }

        public String asc() {
            return "ASC";
        }

        public String desc() {
            return "DESC";
        }

    }

    class Where<T, F, O, V, A> {

        private T type;
        private F field;
        private O operator;
        private V value;
        private A value2;

        public Where(T type, F field, O operator, V value, A value2) {
            this.type = type;
            this.field = field;
            this.operator = operator;
            this.value = value;
            this.value2 = value2;
        }

        public T getType() {
            return type;
        }

        public F getField() {
            return field;
        }

        public O getOperator() {
            return operator;
        }

        public V getValue() {
            return value;
        }

        public A getValue2() {
            return value2;
        }

    }

    class GroupBy<F, S> {

        private F field;
        private S sequence;

        public GroupBy(F field, S sequence) {
            this.field = field;
            this.sequence = sequence;
        }

        public F getField() {
            return field;
        }

        public S getSequence() {
            return sequence;
        }

    }

    class Having {

        private String condition;
        private String field;
        private String operator;
        private Object value;

        public Having(String condition, String field, String operator, Object value) {

            this.condition = condition;
            this.field = field;
            this.operator = operator;
            this.value = value;

        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }

    class Limit {

        private int limit = 0;
        private int offset = 0;

        public Limit() {
            this.limit = 0;
            this.offset = 0;
        }

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

    }

    class Join {

        private String joinMethod = "";
        private String joinTable = "";
        private String tableField = "";
        private String operator = "";
        private String joinTableField = "";

        public Join(String joinMethod, String joinTable, String tableField, String operator, String joinTableField) {
            this.joinMethod = joinMethod;
            this.joinTable = joinTable;
            this.tableField = tableField;
            this.operator = operator;
            this.joinTableField = joinTableField;
        }

        public String getJoinMethod() {
            return joinMethod;
        }

        public void setJoinMethod(String joinMethod) {
            this.joinMethod = joinMethod;
        }

        public String getJoinTable() {
            return joinTable;
        }

        public void setJoinTable(String joinTable) {
            this.joinTable = joinTable;
        }

        public String getTableField() {
            return tableField;
        }

        public void setTableField(String tableField) {
            this.tableField = tableField;
        }

        public String getJoinTableField() {
            return joinTableField;
        }

        public void setJoinTableField(String joinTableField) {
            this.joinTableField = joinTableField;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }
    }













    private String getSelectLogic(List<String> sqlSelect) {

        if (sqlSelect.size() <= 0) {
            return "SELECT * ";
        }

        String selectString = "SELECT ";

        for (String value : sqlSelect) {
            selectString += value + ",";
        }
        selectString = selectString.substring(0, selectString.length() - 1);
        selectString += " ";

        return selectString;

    }

    private String getWhereLogic(List<Where> sqlWhere) {

        String sqlQuery = "";

        if (sqlWhere.isEmpty() == false) {
            sqlQuery += "WHERE ";
            int i = 0;
            for (Where where : this.sqlWhere) {
                String whereLogic = "";

                if (where.getType().equals("enclose")) {
                    sqlQuery += where.getValue();
                    this.parameterList.addAll(((QueryOrm) where.getValue2()).parameterList);
                    i++;
                    continue;
                }
                if (where.getType().equals("whereIn")) {
                    if (i > 0) {
                        whereLogic += "AND ";
                    }
                    whereLogic += where.getField().toString() + " " + where.getOperator().toString();
                    whereLogic += " " + where.getValue() + " ";
                    this.parameterList.addAll((List) where.getValue2());
                    sqlQuery += whereLogic;
                    i++;
                    continue;
                }
                if (where.getType().equals("whereBetween")) {
                    if (i > 0) {
                        whereLogic += "AND ";
                    }
                    whereLogic += where.getField().toString() + " " + where.getOperator().toString();
                    whereLogic += " ? AND ? ";
                    this.parameterList.add(where.getValue());
                    this.parameterList.add(where.getValue2());
                    sqlQuery += whereLogic;
                    i++;
                    continue;
                }
                if (where.getType().equals("whereNull")) {
                    if (i > 0) {
                        whereLogic += "AND ";
                    }
                    whereLogic += where.getField().toString() + " " + where.getOperator().toString();
                    sqlQuery += whereLogic;
                    i++;
                    continue;
                }
                if (where.getType().equals("whereDate")) {
                    if (i > 0) {
                        whereLogic += "AND ";
                    }

                    whereLogic += where.getValue2().toString() + "(" + where.getField().toString() + ") " + where.getOperator().toString() + " ? ";
                    sqlQuery += whereLogic;
                    this.parameterList.add(where.getValue());
                    i++;
                    continue;
                }
                if (where.getType().equals("whereField")) {
                    if (i > 0) {
                        whereLogic += "AND ";
                    }

                    whereLogic += where.getField() + " " + where.getOperator() + " " + where.getValue() + " ";
                    sqlQuery += whereLogic;
                    i++;
                    continue;
                }

                if (i <= 0) {
                    i++;
                } else {
                    whereLogic += where.getType() + " ";
                }
                whereLogic += where.getField().toString() + " " + where.getOperator().toString();
                whereLogic += " ? ";
                this.parameterList.add(where.getValue());

                sqlQuery += whereLogic;
            }

        }

        return sqlQuery;
    }

    private String getJoinLogic(List<Join> sqlJoins) {

        if (sqlJoins.size() <= 0) {
            return "";
        }
        String sqlQuery = "";

        for (Join sqlJoin : sqlJoins) {

            sqlQuery += sqlJoin.getJoinMethod()
                    + sqlJoin.getJoinTable()
                    + " ON " + sqlJoin.getTableField()
                    + " " + sqlJoin.getOperator() + " " + sqlJoin.getJoinTableField() + " ";
        }


        return sqlQuery;
    }

    private String getOrderByLogic(List<OrderBy> sqlOrderBys) {

        if (sqlOrderBys.size() <= 0) {
            return "";
        }

        String sqlQuery = "";
        List ascFields = new ArrayList<>();
        List descFields = new ArrayList<>();
        String ascField  = "ORDER BY ";
        String descField = "ORDER BY ";

        for (OrderBy orderBy : sqlOrderBys) {

            if (orderBy.getValue().equals("ASC")) {
                ascFields.add(orderBy.getField());
            }
            if (orderBy.getValue().equals("DESC")) {
                descFields.add(orderBy.getField());
            }
            if(orderBy.getValue().equals("RANDOM()")){
                descFields.add(orderBy.getField());
            }
        }

        ascField += String.join(", ", ascFields);
        descField += String.join(", ", descFields);

        if (sqlOrderBys.get(0).getValue().equals("ASC")) {
            sqlQuery += ascField + " ASC ";

        } else if(sqlOrderBys.get(0).getValue().equals("DESC")){
            sqlQuery += descField + " DESC ";

        }else{
            sqlQuery += descField + " RANDOM() ";
        }

        return sqlQuery;

    }

    private String getGroupByLogic(List<GroupBy> sqlGroupBys) {
        if (sqlGroupBys.size() <= 0) {
            return "";
        }

        List groupFields = new ArrayList();

        for (GroupBy sqlGroupBy : sqlGroupBys) {
            groupFields.add(sqlGroupBy.getField());
        }
        String sqlQuery = " GROUP BY " + String.join(",", groupFields) + " ";

        return sqlQuery;
    }

    private String getHavingLogic(List<Having> sqlHavings) {

        if (sqlHavings.size() <= 0) {
            return "";
        }

        String sqlQuery = " Having ";

        int i = 0;
        for (Having having : sqlHavings) {
            if (i == 0) {
                i++;
            } else {
                sqlQuery += " " + having.getCondition() + " ";
            }
            sqlQuery += having.getField() + " " + having.getOperator() + " " + having.getValue();
        }

        return sqlQuery;
    }

    private String getOffsetLimitLogic(Limit offsetLimit) {

        if (offsetLimit == null) {
            return "";
        }
        String sqlQuery = "";

        if (offsetLimit.limit > 0) {
            sqlQuery = " LIMIT " + offsetLimit.getOffset() + "," + offsetLimit.getLimit() + " ";
        } else if (offsetLimit.limit == 0 && offsetLimit.offset > 0) {
            sqlQuery = " LIMIT " + offsetLimit.getOffset() + "," + this.maxRecord + " ";
        }

        return sqlQuery;

    }


}



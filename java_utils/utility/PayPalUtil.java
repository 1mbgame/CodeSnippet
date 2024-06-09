package com.ngwisefood.app.utility;

import com.google.gson.Gson;
import com.ngwisefood.app.model.paypal.PayPalAccessToken;
import com.ngwisefood.app.model.paypal.PayPalCheckOrder;
import okhttp3.*;
import org.apache.tomcat.util.codec.binary.Base64;

public class PayPalUtil {

    private static final PayPalUtil ourInstance = new PayPalUtil();

    public static PayPalUtil getInstance() {
        return ourInstance;
    }

    private PayPalUtil() {
        // initialize
    }


    private Gson gson = new Gson();
    private PayPalAccessToken payPalAccessToken;
    private long expiryTime = 0;
    private String authorizationCode = "";

    //private String accessTokenUrl = "https://api-m.sandbox.paypal.com/v1/oauth2/token"; // Sandbox
    private String accessTokenUrl = "https://api-m.paypal.com/v1/oauth2/token"; // Production
    private String clientId = "Afetk6HbqRB5OENors-_qCtwp2j0Pr4xmwe_sia0bHOYnOn23vIIE9Jy5kAlPVc-PhdL35RBaUnKhWUJ";
    private String secretCode = "EBOBjAGsLD9uzdcJ3gzk5T8atTWwzNvp37n84GH6qhz7ckz4F0Y0_rFxn4WEhOGnlQuPVVQklqWv_sMu";

    public boolean verifyOrder(String url, String id){

        PayPalAccessToken payPalAccessToken = getAccessToken();

        try{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + payPalAccessToken.access_token)
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Host", "api.sandbox.paypal.com")
                    .addHeader("accept-encoding", "gzip, deflate")
                    .build();

            Response response = client.newCall(request).execute();

            String responseString = response.body().string();
            System.out.println(responseString);
            
            PayPalCheckOrder payPalCheckOrder = gson.fromJson(responseString,PayPalCheckOrder.class);
            if(payPalCheckOrder.status.equalsIgnoreCase("COMPLETED") &&
                payPalCheckOrder.id.equalsIgnoreCase(id)
            ){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return false;
    }

    public PayPalAccessToken getAccessToken(){

        if(payPalAccessToken != null){
            if(expiryTime < System.currentTimeMillis()){
                return payPalAccessToken;
            }
        }

        if(authorizationCode.isEmpty()){
            authorizationCode = getAuthorizationCode();
        }

        try{

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded,application/json");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
            Request request = new Request.Builder()
                    .url(accessTokenUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded,application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Basic QVhEUU1WeFRreG5kenpqNnUzREdvTUJQLUJlZzYyM2xDaUU5Wk1GOS1NOGJjWTdjci0zem51bE5pVlNadnFKQTV0WmtIZjFoVjhuRkpGa2g6RU9TbVVFT0xKOHZaWUx6dVEzR0F3QzdRSFg5UWd1bzBTeUdZc2VaSEx1bDRlaEpPSnFZajRtbTdhZDQ5WEdWVnNLVi1YN1IwSFF5T09UX1I=")
                    //.addHeader("Authorization", "Basic " + authorizationCode)
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();

            String result = response.body().string();

            payPalAccessToken = gson.fromJson(result,PayPalAccessToken.class);
            expiryTime = System.currentTimeMillis() + payPalAccessToken.expires_in;

            return payPalAccessToken;

        }catch (Exception e){
            e.printStackTrace();
        }


        return null;

    }

    private String getAuthorizationCode(){
        if(authorizationCode.isEmpty() == false){
            return authorizationCode;
        }

        try{
            String code = clientId + ":" + secretCode;
            String base64Code = Base64.encodeBase64String(code.getBytes());

            return base64Code;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";

    }

}

package com.myclient.app;

import jakarta.servlet.http.PushBuilder;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;

public class MySSLCertUtil {

    private TrustManager[] trustManagers;

    /** Sample
     String filePath = "C:\\Users\\Administrator\\Desktop\\spring_ssl\\ssl_cert\\mydomain.pem";
     MyCustomCert myCustomCert = new MyCustomCert();
     SSLSocketFactory sslSocketFactory = myCustomCert.getSslSocketFactory(filePath);

     // For OkHttp
     OkHttpClient client = new OkHttpClient.Builder()
     .sslSocketFactory(sslSocketFactory, myCustomCert.get509TrustManager())
     .hostnameVerifier(myCustomCert.getHostNameVerifier())
     .build();
     */

    public SSLSocketFactory getSslSocketFactory(String... certFilePaths){

        SSLSocketFactory sslSocketFactory = null;

        try{

            int totalCerts = certFilePaths.length;
            trustManagers = new TrustManager[totalCerts];

            for (int i = 0; i < totalCerts; i++) {
                String certFilePath = certFilePaths[i];
                TrustManager trustManager = getTrustManager(certFilePath);
                if(trustManager != null){
                    trustManagers[i] = trustManager;
                }
            }

            // Use the TrustManager to build SSL socket
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers , null);
            sslSocketFactory = sslContext.getSocketFactory();

        }catch (Exception e){
            e.printStackTrace();
        }

        return sslSocketFactory;
    }

    public TrustManager[] getTrustManagers() {
        return trustManagers;
    }

    public HostnameVerifier getHostNameVerifier(){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                System.out.println("Allow Hostname: " + hostname);
                System.out.println("Session: " + session.getPeerHost());
                return hostname.equalsIgnoreCase(session.getPeerHost());
            }
        };

        return hostnameVerifier;
    }

    public HostnameVerifier getAnyHostNameVerifier(){
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        return hostnameVerifier;
    }

    public X509TrustManager get509TrustManager() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] cArrr = new X509Certificate[0];
                return cArrr;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        }};
        X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];

        return x509TrustManager;
    }

    private X509TrustManager getTrustManager(String certFilePath) {

        try{
            // Create input stream for the file
            InputStream inputStream = new FileInputStream(certFilePath);

            // Convert the file content into certificate
            // File content must be start from -----BEGIN CERTIFICATE-----
            // And end with -----END CERTIFICATE-----
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(inputStream);


            // Put the certificates a key store.
            char[] password = "password_1273172145".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
            keyStore.setCertificateEntry("alias",certificate);


            // Use key store to build an X509 trust manager.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];

            return x509TrustManager;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}

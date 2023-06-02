package com.myclient.app;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 Develop By Andy Ng
 Free
 */
public class MySSLCertUtil {

    public static final String PUBLIC_KEY = "PUBLIC KEY";
    public static final String PRIVATE_KEY = "PRIVATE KEY";
    public static final String SSL_FACTORY = "SSL_FACTORY";
    public static final String SSL_TRUST_MANAGERS = "SSL_TRUST_MANAGERS";
    public static final String CERTIFICATE = "CERTIFICATE";
    public static final String CERTIFICATE_BEGIN = "-----BEGIN CERTIFICATE-----";
    public static final String CERTIFICATE_END = "-----END CERTIFICATE-----";
    public static final String PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
    public static final String PRIVATE_KEY_END = "-----END PRIVATE KEY-----";

    private TrustManager[] trustManagers;
    private SSLSocketFactory sslSocketFactory;

    /** Sample for p12 testing

     String p12FilePath = "C:\\Users\\Administrator\\Desktop\\spring_ssl\\ssl_cert\\keystore.p12";
     MySSLCertUtil mySSLCertUtil = new MySSLCertUtil();
     mySSLCertUtil.createSSLFactoryFromP12File(p12FilePath,"admin123","mydomain");


     HttpService httpService = new HttpService();
     httpService.client = new OkHttpClient.Builder()
                 .sslSocketFactory(mySSLCertUtil.getSslSocketFactory(), mySSLCertUtil.get509TrustManager())
                 .hostnameVerifier(mySSLCertUtil.getHostNameVerifier())
                 .build();

     */

    public void createSSLFactoryFromP12File(String p12FilePath,String password, String alias){
        try{
            InputStream stream = new FileInputStream(p12FilePath);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream, password.toCharArray());

            PrivateKey privateKey = (PrivateKey)keyStore.getKey(alias, password.toCharArray());
            Certificate certificate = keyStore.getCertificate(alias);

            // Create a keyStore
            KeyStore trustStore = createEmptyKeyStore(password.toCharArray());
            trustStore.setCertificateEntry(alias, certificate);

            // Create TrustManager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            trustManagers = trustManagerFactory.getTrustManagers();


            // ==========================================================
            // Create private key store
            KeyStore privateKeyStore = createEmptyKeyStore(password.toCharArray());
            privateKeyStore.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[]{certificate});

            // Create Key Manager from keyStore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(privateKeyStore, password.toCharArray());
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();


            // ==========================================
            // Create SSLContext with the keyManagers and trustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, null);

            sslSocketFactory = sslContext.getSocketFactory();

//            System.out.println("privateKey:");
//            System.out.println(privateKey.toString());
//            System.out.println("========================================");
//            System.out.println("Public Key: ");
//            System.out.println( certificate.getPublicKey().toString());
//            System.out.println("========================================");
//            System.out.println("cert:");
//            System.out.println(certificate.toString());


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager get509TrustManager(){
        if(trustManagers == null){
            return null;
        }

        X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];

        return x509TrustManager;
    }


    public HostnameVerifier getHostNameVerifier() {
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

    public void createSSLFactoryForSelfSign(){
        try{
            System.out.println("**** Allow untrusted SSL connection ****");
            trustManagers = new TrustManager[]{new X509TrustManager() {
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

            SSLContext sslContext = SSLContext.getInstance("SSL");

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            sslContext.init(null, trustManagers, new java.security.SecureRandom());


            sslSocketFactory = sslContext.getSocketFactory();




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HostnameVerifier getAnyHostNameVerifier() {
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        return hostnameVerifier;
    }

    private X509TrustManager get509TrustManagerBackup() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] cert = new X509Certificate[0];
                return cert;
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

    private KeyStore createEmptyKeyStore(char[] keyStorePassword) {
        try{
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, keyStorePassword);
            return keyStore;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> readPemFile(String filePath) {

        Map<String, String> resultMap = new LinkedHashMap<>();
        List<String> lineList = getLines(filePath);
        StringBuilder sbPublicKey = new StringBuilder();
        StringBuilder sbPrivateKey = new StringBuilder();
        StringBuilder sbCertificate = new StringBuilder();

        boolean isPublicKeyContent = false;
        boolean isPrivateKeyContent = false;
        boolean isCertificateContent = false;

        for (String line : lineList) {

            if (line.contains(PUBLIC_KEY)) {
                if (isPublicKeyContent == false) {
                    isPublicKeyContent = true;
                    continue;
                } else {
                    isPublicKeyContent = false;
                    continue;
                }
            }

            if (line.contains(PRIVATE_KEY)) {
                if (isPrivateKeyContent == false) {
                    isPrivateKeyContent = true;
                    continue;
                } else {
                    isPrivateKeyContent = false;
                    continue;
                }
            }

            if (line.contains(CERTIFICATE)) {
                if (isCertificateContent == false) {
                    isCertificateContent = true;
                    continue;
                } else {
                    isCertificateContent = false;
                    continue;
                }
            }

            if (isPublicKeyContent) {
                sbPublicKey.append(line);
            }

            if (isPrivateKeyContent) {
                sbPrivateKey.append(line);
            }

            if (isCertificateContent) {
                sbCertificate.append(line);
            }
        }

        resultMap.put(PUBLIC_KEY, sbPublicKey.toString());
        resultMap.put(PRIVATE_KEY, sbPrivateKey.toString());
        resultMap.put(CERTIFICATE, sbCertificate.toString());

        System.out.println("=========> Public Key:");
        System.out.println(resultMap.get(PUBLIC_KEY));
        System.out.println("=========> Private Key:");
        System.out.println(resultMap.get(PRIVATE_KEY));
        System.out.println("=========> Certificate:");
        System.out.println(resultMap.get(CERTIFICATE));

        return resultMap;

    }

    private List<String> getLines(String filePath) {

        List<String> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                //System.out.println(line);
                // read next line
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }

    private void writeFile(String filename, String inputFileContent) {
        try (FileOutputStream out = new FileOutputStream(filename)) {
            byte data[] = inputFileContent.getBytes();
            out.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

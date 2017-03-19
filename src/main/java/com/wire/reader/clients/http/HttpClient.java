package com.wire.reader.clients.http;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HttpClient {

    public String get(String endpoint)
            throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        configureHttpsURLConnectionToTrustAll();
        HttpsURLConnection connection = createConnection(endpoint);
        String response = streamToString((InputStream) connection.getContent());
        connection.disconnect();
        return (response);
    }

    private HttpsURLConnection createConnection(String endpoint)
            throws URISyntaxException, IOException {
        URL url = (new URI(endpoint)).normalize().toURL();
        HttpsURLConnection connection = (HttpsURLConnection) (url).openConnection();
        connection.setHostnameVerifier(new TrustAllHostNameVerifier());
        return(connection);
    }

    private void configureHttpsURLConnectionToTrustAll() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new TrustAllTrustManager()}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new TrustAllHostNameVerifier());
    }

    private String streamToString(java.io.InputStream stream) {
        java.util.Scanner scanner = new java.util.Scanner(stream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}


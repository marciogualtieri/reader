package com.wire.reader.clients.http;

import android.annotation.SuppressLint;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class TrustAllHostNameVerifier implements HostnameVerifier {

    @SuppressLint("BadHostnameVerifier")
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

}

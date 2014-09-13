/*
 * Copyright (c) 2014 Santo Pfingsten
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose, including commercial
 * applications, and to alter it and redistribute it freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not claim that you wrote the
 *    original software. If you use this software in a product, an acknowledgment in the product
 *    documentation would be appreciated but is not required.
 *
 * 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being
 *    the original software.
 *
 * 3. This notice may not be removed or altered from any source distribution.
 */

package info.lusito.jamendo.api.query;

import info.lusito.jamendo.api.results.OAuthResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class OAuthQuery {
    private static final String OAUTH_GRANT_URL = "https://api.jamendo.com/v3.0/oauth/grant";

    static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static OAuthResponse postSSL(String url, String parameters) throws Exception {
        SSLContext sslctx = SSLContext.getInstance("SSL");
        sslctx.init(null, new X509TrustManager[]{new MyTrustManager()}, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
        HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        PrintStream ps = new PrintStream(con.getOutputStream());
        ps.println(parameters);
        ps.close();
        con.connect();

        if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            JsonFactory factory = new JsonFactory();
            JsonParser parser = null;
            BufferedReader in = null;

            OAuthResponse response = new OAuthResponse();
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                parser = factory.createParser(in);
                parser.nextToken();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldname = parser.getCurrentName();
                    parser.nextToken();

                    if ("error".equals(fieldname)) {
                        response.error = parser.getText();
                    } else if ("error_description".equals(fieldname)) {
                        response.error_description = parser.getText();
                    } else if ("access_token".equals(fieldname)) {
                        response.access_token = parser.getText();
                    } else if ("expires_in".equals(fieldname)) {
                        response.expires_in = parser.getIntValue();
                    } else if ("token_type".equals(fieldname)) {
                        response.token_type = parser.getText();
                    } else if ("scope".equals(fieldname)) {
                        response.scope = parser.getText();
                    } else if ("refresh_token".equals(fieldname)) {
                        response.refresh_token = parser.getText();
                    } else {
                        throw new IllegalStateException("Unrecognized field '" + fieldname + "'!");
                    }
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (parser != null) {
                    parser.close();
                }
            }

            return response;
        }
        con.disconnect();

        return null;
    }

    public static OAuthResponse getAccessToken(String authorization_code) throws Exception {
        String parameters = String.format("client_id=%s&client_secret=%s&grant_type=authorization_code&code=%s",
                QuerySettings.CLIENT_ID, QuerySettings.CLIENT_SECRET, authorization_code);
        return postSSL(OAUTH_GRANT_URL, parameters);
    }

    public static OAuthResponse getRefreshToken(String refresh_token) throws Exception {
        String parameters = String.format("client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s",
                QuerySettings.CLIENT_ID, QuerySettings.CLIENT_SECRET, refresh_token);
        return postSSL(OAUTH_GRANT_URL, parameters);
    }
}

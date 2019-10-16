/*
 * Copyright (c) 2018-2019 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment is required by displaying the trademark/log as per the details provided here: [https://www.qualcomm.com/documents/dirbs-logo-and-brand-guidelines]
 *  Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 *  This notice may not be removed or altered from any source distribution.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.dvsauthorized.mUtils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.openid.appauth.Preconditions;
import net.openid.appauth.connectivity.ConnectionBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * An example implementation of {@link ConnectionBuilder} that permits connecting to http
 * links, and ignores certificates for https connections. *THIS SHOULD NOT BE USED IN PRODUCTION
 * CODE*. It is intended to facilitate easier testing of AppAuth against development servers
 * only.
 */
public final class ConnectionBuilderForTesting implements ConnectionBuilder {

    static final ConnectionBuilderForTesting INSTANCE = new ConnectionBuilderForTesting();

    private static final String TAG = "ConnBuilder";
    private static final int CONNECTION_TIMEOUT_MS = (int) TimeUnit.SECONDS.toMillis(15);
    private static final int READ_TIMEOUT_MS = (int) TimeUnit.SECONDS.toMillis(10);

    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    @SuppressLint("TrustAllX509TrustManager")
    private static final TrustManager[] ANY_CERT_MANAGER = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
    };

    @SuppressLint("BadHostnameVerifier")
    private static final HostnameVerifier ANY_HOSTNAME_VERIFIER = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    @Nullable
    private static final SSLContext TRUSTING_CONTEXT;

    static {
        SSLContext context;
        try {
            context = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            Log.e("ConnBuilder", "Unable to acquire SSL context");
            context = null;
        }

        SSLContext initializedContext = null;
        if (context != null) {
            try {
                context.init(null, ANY_CERT_MANAGER, new java.security.SecureRandom());
                initializedContext = context;
            } catch (KeyManagementException e) {
                Log.e(TAG, "Failed to initialize trusting SSL context");
            }
        }

        TRUSTING_CONTEXT = initializedContext;
    }

    private ConnectionBuilderForTesting() {
        // no need to construct new instances
    }

    @NonNull
    @Override
    public HttpURLConnection openConnection(@NonNull Uri uri) throws IOException {
        Preconditions.checkNotNull(uri, "url must not be null");
        Preconditions.checkArgument(HTTP.equals(uri.getScheme()) || HTTPS.equals(uri.getScheme()),
                "scheme or uri must be http or https");
        HttpURLConnection conn = (HttpURLConnection) new URL(uri.toString()).openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setInstanceFollowRedirects(false);

        if (conn instanceof HttpsURLConnection && TRUSTING_CONTEXT != null) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setSSLSocketFactory(TRUSTING_CONTEXT.getSocketFactory());
            httpsConn.setHostnameVerifier(ANY_HOSTNAME_VERIFIER);
        }

        return conn;
    }
}
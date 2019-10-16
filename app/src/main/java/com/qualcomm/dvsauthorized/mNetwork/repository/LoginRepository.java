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

package com.qualcomm.dvsauthorized.mNetwork.repository;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.qualcomm.dvsauthorized.mLogin.view.LoginActivity;
import com.qualcomm.dvsauthorized.mLogin.view.TokenCallback;
import com.qualcomm.dvsauthorized.mUtils.AuthStateManager;
import com.qualcomm.dvsauthorized.mUtils.Configuration;
import com.qualcomm.dvsauthorized.mUtils.Constants;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.browser.BrowserWhitelist;
import net.openid.appauth.browser.VersionedBrowserMatcher;

// Repository class for requesting api to get OAuth access token
public class LoginRepository {

    private Activity mActivity;

    private AuthState mAuthState;
    private AuthStateManager mAuthStateManager;
    private AuthorizationService mAuthService;

    public LoginRepository(Activity context) {
        this.mActivity = context;
    }

    // Opens webview dialog for taking user credentials to fetch OAuth token
    // Using aerogear library
    public void authLogin() {

        Configuration mConfiguration = Configuration.getInstance(mActivity);

        AuthorizationServiceConfiguration serviceConfig =
                new AuthorizationServiceConfiguration(
                        Uri.parse(Constants.AUTH_REALMS_URL + Constants.AUTH_REALMS + "/protocol/openid-connect/auth"), // authorization endpoint
                        Uri.parse(Constants.AUTH_REALMS_URL + Constants.AUTH_REALMS + "/protocol/openid-connect/token")); // token endpoint

        mAuthState = new AuthState(serviceConfig);
        mAuthStateManager = AuthStateManager.getInstance(mActivity);

        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        serviceConfig, // the authorization service configuration
                        Constants.CLIENT_ID, // the client ID, typically pre-registered and static
                        ResponseTypeValues.CODE, // the response_type value: we want a code
                        Uri.parse(Constants.AUTHZ_REDIRECT_URL)); // the redirect URI to which the auth response is sent

        AuthorizationRequest authRequest = authRequestBuilder
                .setScope("openid email profile")
                .build();

        AppAuthConfiguration appAuthConfig = new AppAuthConfiguration.Builder()
                .setBrowserMatcher(new BrowserWhitelist(
                        VersionedBrowserMatcher.CHROME_BROWSER,
                        VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                        VersionedBrowserMatcher.SAMSUNG_BROWSER,
                        VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB,
                        VersionedBrowserMatcher.FIREFOX_BROWSER,
                        VersionedBrowserMatcher.FIREFOX_CUSTOM_TAB))
                .setConnectionBuilder(mConfiguration.getConnectionBuilder())
                .build();

        mAuthService = new AuthorizationService(mActivity, appAuthConfig);
        Intent authIntent = mAuthService.getAuthorizationRequestIntent(authRequest);

        if (isBrowserInstalled())
            (mActivity).startActivityForResult(authIntent, LoginActivity.RC_AUTH);
        else {
            Toast.makeText(mActivity, "Please install browser application to continue using the app", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isBrowserInstalled() {
        String url = "https://www.google.com/";
        Uri webAddress = Uri.parse(url);
        Intent intentWeb = new Intent(Intent.ACTION_VIEW, webAddress);
        return (intentWeb.resolveActivity(mActivity.getPackageManager()) != null);
    }

    public void onActivityResult(TokenCallback callback, Intent data) {

        AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
        AuthorizationException ex = AuthorizationException.fromIntent(data);
        mAuthState.update(resp, ex);
        mAuthStateManager.replace(mAuthState);

        if (ex == null) {
            mAuthService.performTokenRequest(
                    resp.createTokenExchangeRequest(),
                    (resp1, ex1) -> {
                        if (resp1 != null) {
                            callback.tokenCallback(resp1.accessToken);
                            // exchange succeeded
                        } else {
                            callback.tokenCallback("failed");
                            // authorization failed, check ex for more details
                        }
                    });
        } else {
            callback.tokenCallback("failed");
        }

        mAuthService.dispose();
    }
}

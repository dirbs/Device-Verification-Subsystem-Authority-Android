/**
 * Copyright (c) 2018 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.dvsauthorized;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qualcomm.dvsauthorized.utils.AuthStateManager;
import com.qualcomm.dvsauthorized.utils.Configuration;
import com.qualcomm.dvsauthorized.utils.MyPrefrences;
import com.tapadoo.alerter.Alerter;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;
import net.openid.appauth.browser.BrowserWhitelist;
import net.openid.appauth.browser.VersionedBrowserMatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private static final String MY_REDIRECT_URI = "com.qualcomm.dvsauthorized:/callback";
    private static final int RC_AUTH = 100;
    Button loginBtn;
    private AuthorizationRequest authRequest;
    AuthorizationServiceConfiguration serviceConfig;
    AuthStateManager mAuthStateManager;

    private AuthState authState;
    private AuthorizationService authService;
    Configuration mConfiguration;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.login_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Log.e("Login", getIntent().toString());
        mConfiguration = Configuration.getInstance(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setEnabled(false);
                authLogin();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Login", "intent = "+getIntent().getExtras());
    }
    public void authLogin(){
        serviceConfig =
                new AuthorizationServiceConfiguration(
                        Uri.parse(getResources().getString(R.string.iam_url)+"auth/realms/"+getResources().getString(R.string.realm)+"/protocol/openid-connect/auth"), // authorization endpoint
                        Uri.parse(getResources().getString(R.string.iam_url)+"auth/realms/"+getResources().getString(R.string.realm)+"/protocol/openid-connect/token")
                ); // token endpoint



        authState = new AuthState(serviceConfig);
        mAuthStateManager = AuthStateManager.getInstance(this);



        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        serviceConfig, // the authorization service configuration
                        getResources().getString(R.string.realm), // the client ID, typically pre-registered and static
                        ResponseTypeValues.CODE, // the response_type value: we want a code
                        Uri.parse(MY_REDIRECT_URI))
                ;



        authRequest = authRequestBuilder
                .setScope("openid email profile")

                .build();


        doAuthorization();



    }
    private void doAuthorization() {
        AppAuthConfiguration appAuthConfig = new AppAuthConfiguration.Builder()
                .setBrowserMatcher(new BrowserWhitelist(
                        VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                        VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB))
                .setConnectionBuilder(mConfiguration.getConnectionBuilder())
                .build();


        progressBar.setVisibility(View.VISIBLE);
        authService = new AuthorizationService(this, appAuthConfig);
        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);




        if(isBrowserInstalled())
            startActivityForResult(authIntent, RC_AUTH);
        else
        {
            Toast.makeText(this, "Please install browser application to continue using the app", Toast.LENGTH_SHORT).show();
        }

    }
    public Boolean isBrowserInstalled() {
        String url = getResources().getString(R.string.iam_url);
        Uri webAddress = Uri.parse(url);
        Intent intentWeb = new Intent(Intent.ACTION_VIEW, webAddress);
        return (intentWeb.resolveActivity(getPackageManager()) != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_AUTH) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
            AuthorizationException ex = AuthorizationException.fromIntent(data);
            authState.update(resp, ex);
            mAuthStateManager.replace(authState);




            if(ex == null){

                Log.e("data", "auth code resp = "+resp.jsonSerializeString());
                authService.performTokenRequest(
                        resp.createTokenExchangeRequest(),
                        new AuthorizationService.TokenResponseCallback() {
                            @Override public void onTokenRequestCompleted(
                                    TokenResponse resp, AuthorizationException ex) {
                                loginBtn.setEnabled(true);
                                if (resp != null) {
                                    // exchange succeeded
                                    Log.e("data", "access_token = "+resp.accessToken);

                                    getUserinfo(resp.accessToken);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    // authorization failed, check ex for more details
                                }
                            }
                        });



            }
            else {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
            }
            // ... process the response or exception ...
        } else {
            // ...
            progressBar.setVisibility(View.GONE);
            loginBtn.setEnabled(true);
        }

    }
    private void getUserinfo(String accessToken) {
        String url = getResources().getString(R.string.iam_url)+"auth/realms/"+getResources().getString(R.string.realm)+"/protocol/openid-connect/userinfo";
        Log.e(TAG, url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject res = new JSONObject(response);

                            String username = res.getString("preferred_username");
                            String email = res.getString("email");
                            String name = res.getString("name");

                            MyPrefrences myPrefrences = new MyPrefrences(LoginActivity.this);
                            myPrefrences.setString("name", name);
                            myPrefrences.setString("email", email);
                            myPrefrences.setString("username", username);


                            Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setEnabled(true);
                        }
                        catch (JSONException e){
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setEnabled(true);
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loginBtn.setEnabled(true);
                        try {
                            if (error != null) {
                                error.printStackTrace();
                                if(error.networkResponse!=null)
                                {
                                    if(error.networkResponse.statusCode >= 400 && error.networkResponse.statusCode < 500)
                                    {
                                        if(error.networkResponse.statusCode == 403)
                                        {
                                            String body = new String(error.networkResponse.data,"UTF-8");
                                            Log.e("422", body+"");
                                            JSONObject mJsonObject = new JSONObject(body);
                                            JSONObject errorObj = mJsonObject.getJSONObject("error");
                                            Alerter.create(LoginActivity.this)
                                                    .setTitle("Oops...")
                                                    .setText(errorObj.getString("message"))
                                                    .setIcon(R.drawable.ic_error)
                                                    .setBackgroundColorRes(R.color.colorRed)
                                                    .setIconColorFilter(0) // Optional - Removes white tint
                                                    .show();

                                        }
                                        else if(error.networkResponse.statusCode == 422)
                                        {
                                            String body = new String(error.networkResponse.data,"UTF-8");
                                            Log.e("422", body+"");
                                            JSONObject mJsonObject = new JSONObject(body);
                                            JSONObject errorObj = mJsonObject.getJSONObject("error");
                                            if (mJsonObject.has("error")) {
                                                if(errorObj.optJSONObject("errors")!=null)
                                                {

                                                    Iterator<String> iter =  errorObj.optJSONObject("errors").keys();
                                                    String errors="";
                                                    while (iter.hasNext()) {
                                                        String key = iter.next();
                                                        for (int i = 0; i <  errorObj.optJSONObject("errors").getJSONArray(key).length(); i++) {

                                                            errors=errors+"- "+ errorObj.optJSONObject("errors").getJSONArray(key).getString(0)+"\n";
                                                        }


                                                    }

                                                    Alerter.create(LoginActivity.this)
                                                            .setTitle("Oops...")
                                                            .setText(errors)
                                                            .setIcon(R.drawable.ic_error)
                                                            .setBackgroundColorRes(R.color.colorRed)
                                                            .setIconColorFilter(0) // Optional - Removes white tint
                                                            .show();
                                                }
                                            }

                                        }

                                    }
                                    else {
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                            Alerter.create(LoginActivity.this)
                                                    .setTitle("Oops...")
                                                    .setText(LoginActivity.this.getString(R.string.error_network_timeout))
                                                    .setIcon(R.drawable.ic_error)
                                                    .setBackgroundColorRes(R.color.colorRed)
                                                    .setIconColorFilter(0) // Optional - Removes white tint
                                                    .show();
                                        } else if (error instanceof AuthFailureError) {


                                            Alerter.create(LoginActivity.this)
                                                    .setTitle("Oops...")
                                                    .setText(LoginActivity.this.getString(R.string.error_auth_failure))
                                                    .setIcon(R.drawable.ic_error)
                                                    .setBackgroundColorRes(R.color.colorRed)
                                                    .setIconColorFilter(0) // Optional - Removes white tint
                                                    .show();
                                        } else if (error instanceof ServerError) {

                                            Alerter.create(LoginActivity.this)
                                                    .setTitle("Oops...")
                                                    .setText(LoginActivity.this.getString(R.string.error_server_error))
                                                    .setIcon(R.drawable.ic_error)
                                                    .setBackgroundColorRes(R.color.colorRed)
                                                    .setIconColorFilter(0) // Optional - Removes white tint
                                                    .show();
                                        } else if (error instanceof NetworkError) {

                                            Alerter.create(LoginActivity.this)
                                                    .setTitle("Oops...")
                                                    .setText(LoginActivity.this.getString(R.string.error_network_error))
                                                    .setIcon(R.drawable.ic_error)
                                                    .setBackgroundColorRes(R.color.colorRed)
                                                    .setIconColorFilter(0) // Optional - Removes white tint
                                                    .show();
                                        } else if (error instanceof ParseError) {

                                            Alerter.create(LoginActivity.this)
                                                    .setTitle("Oops...")
                                                    .setText(LoginActivity.this.getString(R.string.error_parse_error))
                                                    .setIcon(R.drawable.ic_error)
                                                    .setBackgroundColorRes(R.color.colorRed)
                                                    .setIconColorFilter(0) // Optional - Removes white tint
                                                    .show();
                                        }
                                    }
                                }
                                else
                                {
                                    Alerter.create(LoginActivity.this)
                                            .setTitle("Oops...")
                                            .setText(LoginActivity.this.getString(R.string.error_server_error))
                                            .setIcon(R.drawable.ic_error)
                                            .setBackgroundColorRes(R.color.colorRed)
                                            .setIconColorFilter(0) // Optional - Removes white tint
                                            .show();
                                }
                            }
                            else
                            {
                                Alerter.create(LoginActivity.this)
                                        .setTitle("Oops...")
                                        .setText(LoginActivity.this.getString(R.string.error_server_error))
                                        .setIcon(R.drawable.ic_error)
                                        .setBackgroundColorRes(R.color.colorRed)
                                        .setIconColorFilter(0) // Optional - Removes white tint
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                MyPrefrences myPrefrences = new MyPrefrences(LoginActivity.this);
//                params.put("Authorization", "Bearer " + myPrefrences.getString("token",""));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("access_token", accessToken);


                return params;
            }

        };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

            requestQueue.add(stringRequest);

    }
}

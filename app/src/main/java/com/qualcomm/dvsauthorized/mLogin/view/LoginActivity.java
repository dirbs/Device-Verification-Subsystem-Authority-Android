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

package com.qualcomm.dvsauthorized.mLogin.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mLogin.viewModel.LoginViewModel;
import com.qualcomm.dvsauthorized.mMainScreens.MainTabActivity;
import com.qualcomm.dvsauthorized.mModels.loginModel.UserInfoResponse;
import com.qualcomm.dvsauthorized.mUtils.ConnectionDetector;
import com.qualcomm.dvsauthorized.mUtils.MyPreferences;
import com.qualcomm.dvsauthorized.mUtils.Status;
import com.qualcomm.dvsauthorized.mUtils.Utils;
import com.tapadoo.alerter.Alerter;

import java.util.Locale;

// View class for login activity
public class LoginActivity extends FragmentActivity implements TokenCallback {

    // Global variables for views and view models
    public static final int RC_AUTH = 100;
    private MaterialButton mLoginBtn;
    private ProgressBar mProgressBar;

    private LoginViewModel mLoginViewModel;

    // Invoked upon opening login activity, initialized view variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_login);

        mLoginBtn = findViewById(R.id.login_btn);
        mProgressBar = findViewById(R.id.progressBar);

        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        observeViewModel(mLoginViewModel);

        if (Utils.getToken(this) != null && !Utils.getToken(this).equals("")) {
            Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        // Login button click listener
        mLoginBtn.setOnClickListener(v -> {
            mProgressBar.setVisibility(View.VISIBLE);
            mLoginBtn.getBackground().setAlpha(128);
            mLoginBtn.setEnabled(false);
            ConnectionDetector cd = new ConnectionDetector(LoginActivity.this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                authLogin();
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mLoginBtn.getBackground().setAlpha(255);
                mLoginBtn.setEnabled(true);
                Utils.showNoInternetDialog(LoginActivity.this);
            }
        });
    }

    // For getting OAuth access token
    public void authLogin() {
        Utils.clearCookies(this);
        mLoginViewModel.login(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_AUTH) {
            mLoginViewModel.loginToken(this, data);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mLoginBtn.setEnabled(true);
            mLoginBtn.getBackground().setAlpha(255);
        }
    }

    // Callback from repository for api response against OAuth access token request
    @Override
    public void tokenCallback(String token) {

        if (!token.equals("failed")) {
            getUserInfo(token);
            Utils.saveToken(this, token);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mLoginBtn.setEnabled(true);
            mLoginBtn.getBackground().setAlpha(255);
        }
    }

    // If OAuth access token available user info view model is requested to fetch user info
    private void getUserInfo(String accessToken) {
        mLoginViewModel.getUserInfo(accessToken);
    }

    // Observer to observer on user info live data for any response from api
    private void observeViewModel(LoginViewModel loginViewModel) {

        loginViewModel.getUserInfoLiveData().observe(this, apiResponse -> {

            Utils.clearCookies(this);
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    UserInfoResponse userInfoResponse = (UserInfoResponse) apiResponse.data;

                    MyPreferences myPreferences = new MyPreferences(LoginActivity.this);
                    myPreferences.setString("name", userInfoResponse.getName());
                    myPreferences.setString("email", userInfoResponse.getEmail());
                    myPreferences.setString("username", userInfoResponse.getPreferredUsername());

                    Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();

                } else if (apiResponse.status == Status.ERROR) {
                    String error = Utils.errorType(apiResponse.error);
                    if (error == null || error.equals("")) {
                        if (apiResponse.error.fillInStackTrace().toString().contains("JsonSyntaxException")) {
                            error = String.valueOf(R.string.error_json_parsing);
                        }
                    }

                    if (error.equals("-0")) {
                        Utils.deleteToken(this);
                        showAlerter(String.valueOf(R.string.session_expired));
                    } else {
                        showAlerter(error);
                    }
                }
            }
        });
    }

    // Alert user in case if any error response from api
    private void showAlerter(String message) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoginBtn.getBackground().setAlpha(255);
        mLoginBtn.setEnabled(true);

        int resourceId;
        try {
            resourceId = Integer.parseInt(message);
        } catch (Exception e) {
            resourceId = -1;
            e.printStackTrace();
        }

        if (resourceId != -1) {
            Alerter.create(LoginActivity.this)
                    .setTitle("Oops...")
                    .setText(getString(resourceId))
                    .setIcon(R.drawable.ic_error)
                    .setBackgroundColorRes(R.color.colorRed)
                    .setIconColorFilter(0) // Optional - Removes white tint
                    .show();

        } else {
            Alerter.create(LoginActivity.this)
                    .setTitle("Oops...")
                    .setText(message)
                    .setIcon(R.drawable.ic_error)
                    .setBackgroundColorRes(R.color.colorRed)
                    .setIconColorFilter(0) // Optional - Removes white tint
                    .show();
        }
    }
}
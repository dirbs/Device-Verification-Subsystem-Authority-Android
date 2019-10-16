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

package com.qualcomm.dvsauthorized.mUtils;/*
 * Copyright 2016 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.dvsauthorized.R;

import net.openid.appauth.connectivity.ConnectionBuilder;
import net.openid.appauth.connectivity.DefaultConnectionBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;

/**
 * Reads and validates the demo app configuration from `res/raw/auth_config.json`. Configuration
 * changes are detected by comparing the hash of the last known configuration to the read
 * configuration. When a configuration change is detected, the app state is reset.
 */
public final class Configuration {

    private static final String TAG = "Configuration";
    private static final String PREFS_NAME = "config";
    private static final String KEY_LAST_HASH = "lastHash";

    private static WeakReference<Configuration> sInstance = new WeakReference<>(null);

    private final Context mContext;
    private final SharedPreferences mPrefs;
    private final Resources mResources;

    private JSONObject mConfigJson;
    private String mConfigHash;
    private String mConfigError;

    private String mClientId;
    private String mScope;
    private Uri mRedirectUri;
    private Uri mDiscoveryUri;
    private Uri mAuthEndpointUri;
    private Uri mTokenEndpointUri;
    private Uri mRegistrationEndpointUri;
    private Uri mUserInfoEndpointUri;
    private boolean mHttpsRequired;

    public static Configuration getInstance(Context context) {
        Configuration config = sInstance.get();
        if (config == null) {
            config = new Configuration(context);
            sInstance = new WeakReference<>(config);
        }
        return config;
    }

    private Configuration(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mResources = context.getResources();

        try {
            readConfiguration();
        } catch (InvalidConfigurationException ex) {
            mConfigError = ex.getMessage();
        }
    }

    /**
     * Indicates whether the configuration has changed from the last known valid state.
     */
    public boolean hasConfigurationChanged() {
        String lastHash = getLastKnownConfigHash();
        return !mConfigHash.equals(lastHash);
    }

    /**
     * Indicates whether the current configuration is valid.
     */
    public boolean isValid() {
        return mConfigError == null;
    }

    /**
     * Returns a description of the configuration error, if the configuration is invalid.
     */
    @Nullable
    public String getConfigurationError() {
        return mConfigError;
    }

    /**
     * Indicates that the current configuration should be accepted as the "last known valid"
     * configuration.
     */
    public void acceptConfiguration() {
        mPrefs.edit().putString(KEY_LAST_HASH, mConfigHash).apply();
    }

    @Nullable
    public String getClientId() {
        return mClientId;
    }

    @NonNull
    public String getScope() {
        return mScope;
    }

    @NonNull
    public Uri getRedirectUri() {
        return mRedirectUri;
    }

    @Nullable
    public Uri getDiscoveryUri() {
        return mDiscoveryUri;
    }

    @Nullable
    public Uri getAuthEndpointUri() {
        return mAuthEndpointUri;
    }

    @Nullable
    public Uri getTokenEndpointUri() {
        return mTokenEndpointUri;
    }

    @Nullable
    public Uri getRegistrationEndpointUri() {
        return mRegistrationEndpointUri;
    }

    @Nullable
    public Uri getUserInfoEndpointUri() {
        return mUserInfoEndpointUri;
    }

    private boolean isHttpsRequired() {
        return mHttpsRequired;
    }

    public ConnectionBuilder getConnectionBuilder() {
        if (isHttpsRequired()) {
            return DefaultConnectionBuilder.INSTANCE;
        }
        return ConnectionBuilderForTesting.INSTANCE;
    }

    private String getLastKnownConfigHash() {
        return mPrefs.getString(KEY_LAST_HASH, null);
    }

    private void readConfiguration() throws InvalidConfigurationException {
        BufferedSource configSource =
                Okio.buffer(Okio.source(mResources.openRawResource(R.raw.auth_config)));
        Buffer configData = new Buffer();
        try {
            configSource.readAll(configData);
            mConfigJson = new JSONObject(configData.readString(Charset.forName("UTF-8")));
        } catch (IOException ex) {
            throw new InvalidConfigurationException(
                    "Failed to read configuration: " + ex.getMessage());
        } catch (JSONException ex) {
            throw new InvalidConfigurationException(
                    "Unable to parse configuration: " + ex.getMessage());
        }

        mConfigHash = configData.sha256().base64();
        mClientId = getConfigString("client_id");
        mScope = getRequiredConfigString("authorization_scope");
        mRedirectUri = getRequiredConfigUri("redirect_uri");

        if (!isRedirectUriRegistered()) {
            throw new InvalidConfigurationException(
                    "redirect_uri is not handled by any activity in this app! "
                            + "Ensure that the appAuthRedirectScheme in your build.gradle file "
                            + "is correctly configured, or that an appropriate intent filter "
                            + "exists in your app manifest.");
        }

        if (getConfigString("discovery_uri") == null) {
            mAuthEndpointUri = getRequiredConfigWebUri("authorization_endpoint_uri");

            mTokenEndpointUri = getRequiredConfigWebUri("token_endpoint_uri");
            mUserInfoEndpointUri = getRequiredConfigWebUri("user_info_endpoint_uri");

            if (mClientId == null) {
                mRegistrationEndpointUri = getRequiredConfigWebUri("registration_endpoint_uri");
            }
        } else {
            mDiscoveryUri = getRequiredConfigWebUri("discovery_uri");
        }

        mHttpsRequired = mConfigJson.optBoolean("https_required", true);
    }

    @Nullable
    private String getConfigString(String propName) {
        String value = mConfigJson.optString(propName);
        if (value == null) {
            return null;
        }

        value = value.trim();
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return value;
    }

    @NonNull
    private String getRequiredConfigString(String propName)
            throws InvalidConfigurationException {
        String value = getConfigString(propName);
        if (value == null) {
            throw new InvalidConfigurationException(
                    propName + " is required but not specified in the configuration");
        }

        return value;
    }

    @NonNull
    private Uri getRequiredConfigUri(String propName)
            throws InvalidConfigurationException {
        String uriStr = getRequiredConfigString(propName);
        Uri uri;
        try {
            uri = Uri.parse(uriStr);
        } catch (Throwable ex) {
            throw new InvalidConfigurationException(propName + " could not be parsed", ex);
        }

        if (!uri.isHierarchical() || !uri.isAbsolute()) {
            throw new InvalidConfigurationException(
                    propName + " must be hierarchical and absolute");
        }

        if (!TextUtils.isEmpty(uri.getEncodedUserInfo())) {
            throw new InvalidConfigurationException(propName + " must not have user info");
        }

        if (!TextUtils.isEmpty(uri.getEncodedQuery())) {
            throw new InvalidConfigurationException(propName + " must not have query parameters");
        }

        if (!TextUtils.isEmpty(uri.getEncodedFragment())) {
            throw new InvalidConfigurationException(propName + " must not have a fragment");
        }

        return uri;
    }

    private Uri getRequiredConfigWebUri(String propName)
            throws InvalidConfigurationException {
        Uri uri = getRequiredConfigUri(propName);
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme) || !("http".equals(scheme) || "https".equals(scheme))) {
            throw new InvalidConfigurationException(
                    propName + " must have an http or https scheme");
        }

        return uri;
    }

    private boolean isRedirectUriRegistered() {
        // ensure that the redirect URI declared in the configuration is handled by some activity
        // in the app, by querying the package manager speculatively
        Intent redirectIntent = new Intent();
        redirectIntent.setPackage(mContext.getPackageName());
        redirectIntent.setAction(Intent.ACTION_VIEW);
        redirectIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        redirectIntent.setData(mRedirectUri);

        return !mContext.getPackageManager().queryIntentActivities(redirectIntent, 0).isEmpty();
    }

    public static final class InvalidConfigurationException extends Exception {
        InvalidConfigurationException(String reason) {
            super(reason);
        }

        InvalidConfigurationException(String reason, Throwable cause) {
            super(reason, cause);
        }
    }
}
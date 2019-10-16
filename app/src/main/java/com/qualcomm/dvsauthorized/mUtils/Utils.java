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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.orhanobut.hawk.Hawk;
import com.qualcomm.dvsauthorized.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Objects;

// Utility class for some utility common methods used across application
public class Utils {

    /**
     * checks for error type
     *
     * @param error throwable exception upon communication with server in presenter
     * @return string message or resource ID of message to be alerted to user
     */
    public static String errorType(Throwable error) {

        if (error instanceof SocketTimeoutException) {
            return String.valueOf(R.string.error_network_timeout);

        } else if (error instanceof IOException) {
            return String.valueOf(R.string.error_network_timeout);

        } else if (error instanceof JSONException) {
            return String.valueOf(R.string.error_json_parsing);

        } else if (error instanceof HttpException) {
            Log.i("info", "HttpException");

            String responseBody = null;
            try {
                responseBody = Objects.requireNonNull(((HttpException) error).response().errorBody()).string();
                Log.i("info", "responseBody: " + responseBody + " statusCode " + ((HttpException) error).code());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int statusCode = ((HttpException) error).code();
            if (statusCode >= 400 && statusCode < 500) {
                if (statusCode == 400 || statusCode == 403) {
                    return getMessage(responseBody);
                } else if (statusCode == 422) {
                    return getErrors(responseBody);
                } else if (statusCode == 401) {
                    String message = getMessage(responseBody);
                    if (message == null
                            || message.contains("Token is not active.")
                            || message.contains("Token invalid")
                            || message.contains("Token not provided")
                            || message.contains("User session not found")
                            || message.equals("null")) {
                        return "-0";
                    } else {
                        return getMessage(responseBody);
                    }
                } else if (statusCode == 405) {
                    return String.valueOf(R.string.error_server_error);
                }
            } else {
                return String.valueOf(R.string.error_server_error);
            }
        }
        return "";
    }

    /**
     * retrieve message from response body
     *
     * @param responseBody json response body received by server
     * @return string message present in json
     */
    private static String getMessage(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            if (jsonObject.has("error_description")) {
                return jsonObject.getString("error_description");
            } else {
                return jsonObject.getString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * retrieve messages from reponse body
     *
     * @param responseBody json response body received by server
     * @return string message/s present in json
     */
    private static String getErrors(String responseBody) {
        try {
            StringBuilder errors = new StringBuilder();
            JSONObject jsonObject = new JSONObject(responseBody);

            if (jsonObject.has("messages")) {
                JSONObject messagesObj = jsonObject.optJSONObject("messages");

                if (messagesObj.has("imei")) {
                    Iterator<String> iter = messagesObj.keys();

                    while (iter.hasNext()) {
                        String key = iter.next();

                        if (responseBody.contains("[") && responseBody.contains("]")) {
                            for (int i = 0; i < messagesObj.getJSONArray(key).length(); i++) {
                                errors.append("- ").append(messagesObj.getJSONArray(key).getString(0)).append("\n");
                            }
                        }
                    }
                }
            }
            return errors.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    // For clearing web view cookies
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("clear", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d("clear", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    // For saving OAuth access token in encrypted storage
    public static void saveToken(Context context, String token) {

        Hawk.init(context).build();
        Hawk.put("access_token", token);

    }

    // For retrieving OAuth access token from  encrypted storage
    public static String getToken(Context context) {

        Hawk.init(context).build();
        return Hawk.get("access_token");
    }

    // For removing OAuth access token from  encrypted storage upon logout and session expiry
    public static void deleteToken(Context context) {

        Hawk.init(context).build();
        Hawk.delete("access_token");
    }

    // For showing no internet connection dialog
    public static void showNoInternetDialog(final Activity context) {

        androidx.appcompat.app.AlertDialog.Builder logoutDialog = new androidx.appcompat.app.AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.status_dialog, null);
        logoutDialog.setView(dialogView).setPositiveButton(context.getResources().getString(R.string.yes_enable), (dialog, whichButton) -> {
            dialog.dismiss();
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

        })
                .setNegativeButton(context.getResources().getString(R.string.no), (dialog, whichButton) -> dialog.dismiss());

        ImageView icon = (ImageView) dialogView.findViewById(R.id.icon);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        TextView message = (TextView) dialogView.findViewById(R.id.message);

        icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_error));
        title.setText(R.string.no_internet);
        message.setText(R.string.error_network_error);


        androidx.appcompat.app.AlertDialog alertDialog = logoutDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.show();
    }
}

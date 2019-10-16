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

package com.qualcom.dvsauthorized.mMainTabActivityTests;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qualcom.dvsauthorized.mTestUtilities.MockServerRule;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mLogin.view.LoginActivity;
import com.qualcomm.dvsauthorized.mMainScreens.MainTabActivity;
import com.qualcomm.dvsauthorized.mUtils.Constants;
import com.qualcomm.dvsauthorized.mUtils.MyPreferences;
import com.qualcomm.dvsauthorized.mUtils.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// Unit tests for MainTabActivity and profile fragment
@RunWith(AndroidJUnit4.class)
public class MainTabActivityTests {

    @Rule
    public final ActivityTestRule<MainTabActivity> mainTabActivityActivityTestRule = new ActivityTestRule<>(MainTabActivity.class,
            true, false);

    @Rule
    public final MockServerRule mMockServerRule = new MockServerRule();

    @Before
    public void setUp() {
        Constants.AUTH_REALMS_URL = "http://localhost:8000";
        Intents.init();
    }

    @After
    public void restLoginUrl() {
        Constants.AUTH_REALMS_URL = "http://192.168.100.58:8080/auth/realms/";
        Intents.release();
        mMockServerRule.stopServer();
    }

    // For launching main tab activity
    public void mainTabActivity(){

        Intent intent = new Intent();
        mainTabActivityActivityTestRule.launchActivity(intent);
        MyPreferences myPreferences = new MyPreferences(mainTabActivityActivityTestRule.getActivity());
        myPreferences.setString("name", "abc");
        myPreferences.setString("email", "abc@xyz.com");
        myPreferences.setString("username", "abc_user");
        Utils.clearCookies(mainTabActivityActivityTestRule.getActivity());
        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
        Utils.saveToken(mainTabActivityActivityTestRule.getActivity(), "abc");
    }

    // Unit test for profile fragment
    @Test
    public void profileFragment(){

        mainTabActivity();
        BottomNavigationView bottomNavigationView = mainTabActivityActivityTestRule.getActivity().findViewById(R.id.navigation);

        try {
            mainTabActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_profile));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("abc")).check(matches(isDisplayed()));
        onView(withText("abc@xyz.com")).check(matches(isDisplayed()));
        onView(withText("abc_user")).check(matches(isDisplayed()));

        try {
            mainTabActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_verify_imei));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withId(R.id.submit_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test for successful logout request with 200 status code
    @Test
    public void logoutSuccess(){

        mainTabActivity();
        BottomNavigationView bottomNavigationView = mainTabActivityActivityTestRule.getActivity().findViewById(R.id.navigation);

        try {
            mainTabActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_profile));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        mMockServerRule.server().setDispatcher(logoutSuccessDispatcher());
        onView(withId(R.id.logout_btn)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

    }

    // Unit test for successful logout with 400 status code
    @Test
    public void logoutSuccess2(){

        mainTabActivity();
        BottomNavigationView bottomNavigationView = mainTabActivityActivityTestRule.getActivity().findViewById(R.id.navigation);

        try {
            mainTabActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_profile));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        mMockServerRule.server().setDispatcher(logoutSuccess2Dispatcher());
        onView(withId(R.id.logout_btn)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

    }

    // Unit test for logout failure
    @Test
    public void logoutFailure(){

        mainTabActivity();
        BottomNavigationView bottomNavigationView = mainTabActivityActivityTestRule.getActivity().findViewById(R.id.navigation);

        try {
            mainTabActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_profile));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        mMockServerRule.server().setDispatcher(logoutNetworkErrorDispatcher());
        onView(withId(R.id.logout_btn)).perform(click());
        intended(hasComponent(MainTabActivity.class.getName()));
        onView(withId(R.id.logout_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Dispatcher for success logout (200 status code)
    private Dispatcher logoutSuccessDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/logout?id_token_hint=abc")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody("");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for success logout (400 status code)
    private Dispatcher logoutSuccess2Dispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/logout?id_token_hint=abc")) {
                    return new MockResponse().setResponseCode(400)
                            .setBody("");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for failed logout (403 status code)
    private Dispatcher logoutNetworkErrorDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/logout?id_token_hint=abc")) {
                    return new MockResponse().setResponseCode(403)
                            .setBody("");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }
}

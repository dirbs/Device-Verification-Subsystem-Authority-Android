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

package com.qualcom.dvsauthorized.mLoginTests;

import android.content.Intent;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.gson.Gson;
import com.qualcom.dvsauthorized.mTestUtilities.MockServerRule;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mModels.loginModel.UserInfoResponse;
import com.qualcomm.dvsauthorized.mLogin.view.LoginActivity;
import com.qualcomm.dvsauthorized.mUtils.Constants;
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
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

// Unit tests for login activity
@RunWith(AndroidJUnit4.class)
public class LoginTests {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class,
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

    // For launching login activity
    public void loginAvtivity() {
        Intent intent = new Intent();
        loginActivityActivityTestRule.launchActivity(intent);
    }

    // Unit test first login method
    @Test
    public void firstLogin(){

        loginAvtivity();
        Utils.saveToken(loginActivityActivityTestRule.getActivity(), "abc");
        loginActivityActivityTestRule.finishActivity();
        mMockServerRule.server().setDispatcher(userInfoSuccessDispatcher());
        loginAvtivity();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.submit_btn)).check(matches(isDisplayed()));

        Utils.clearCookies(loginActivityActivityTestRule.getActivity());
        Utils.deleteToken(loginActivityActivityTestRule.getActivity());

    }

    // Unit test for OAuth (Login repository)
    @Test
    public void oAuth() {

        loginAvtivity();
        mMockServerRule.server().setDispatcher(oAuthDispatcher());
        onView(withId(R.id.login_btn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(isRoot()).perform(ViewActions.pressBack());
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(loginActivityActivityTestRule.getActivity());
    }

    // Unit test for success response for user info from api
    @Test
    public void userInfoSuccess() {

        loginAvtivity();
        Utils.saveToken(loginActivityActivityTestRule.getActivity(), "abc");
        mMockServerRule.server().setDispatcher(userInfoSuccessDispatcher());
        onView(withId(R.id.login_btn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.submit_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(loginActivityActivityTestRule.getActivity());
    }

    // Unit test for failure response (401 status code) from api for user info
    @Test
    public void userInfoFailure() {

        loginAvtivity();
        Utils.saveToken(loginActivityActivityTestRule.getActivity(), "abc");
        mMockServerRule.server().setDispatcher(userInfoFailureDispatcher());
        onView(withId(R.id.login_btn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(loginActivityActivityTestRule.getActivity());
    }

    // Unit test for failure response (403 status code) from api for user info
    @Test
    public void userInfoFailure2() {

        loginAvtivity();
        Utils.saveToken(loginActivityActivityTestRule.getActivity(), "abc");
        mMockServerRule.server().setDispatcher(userInfoFailure2Dispatcher());
        onView(withId(R.id.login_btn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(loginActivityActivityTestRule.getActivity());
    }

    // Unit test for json failure response from api
    @Test
    public void userInfoJsonError() {

        loginAvtivity();
        Utils.saveToken(loginActivityActivityTestRule.getActivity(), "abc");
        mMockServerRule.server().setDispatcher(userInfoJsonErrorDispatcher());
        onView(withId(R.id.login_btn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(loginActivityActivityTestRule.getActivity());
    }

    // Unit test for network error response
    @Test
    public void userInfoNetworkError() {

        loginAvtivity();
        Utils.saveToken(loginActivityActivityTestRule.getActivity(), "abc");
        mMockServerRule.server().setDispatcher(userInfoNetworkErrorDispatcher());
        onView(withId(R.id.login_btn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        intended(hasComponent(LoginActivity.class.getName()));
        onView(withId(R.id.login_btn)).check(matches(isDisplayed()));

        Utils.deleteToken(loginActivityActivityTestRule.getActivity());
    }

    // Dispatcher for success response
    private Dispatcher userInfoSuccessDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/userinfo")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody("{\n" +
                                    "    \"sub\": \"fe5f9832-be43-49e6-a1dc-5d5bec8f6aa7\",\n" +
                                    "    \"name\": \"Hamza Shahid\",\n" +
                                    "    \"preferred_username\": \"hamza\",\n" +
                                    "    \"given_name\": \"Hamza\",\n" +
                                    "    \"family_name\": \"Shahid\",\n" +
                                    "    \"email\": \"hamza@3gca.org\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for failure response (401 status code)
    private Dispatcher userInfoFailureDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/userinfo")) {
                    return new MockResponse().setResponseCode(401)
                            .setBody("{\n" +
                                    "    \"error\": \"invalid_request\",\n" +
                                    "    \"error_description\": \"User session not found or doesn't have client attached on it\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for failure response (403 status code)
    private Dispatcher userInfoFailure2Dispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/userinfo")) {
                    return new MockResponse().setResponseCode(403)
                            .setBody("{\n" +
                                    "    \"error\": \"invalid_request\",\n" +
                                    "    \"error_description\": \"User session not found or doesn't have client attached on it\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for json failure response
    private Dispatcher userInfoJsonErrorDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/userinfo")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody("\"error\": \"invalid_request\",\n" +
                                    "\"error_description\": \"User session not found or doesn't have client attached on it\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for network error response
    private Dispatcher userInfoNetworkErrorDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/userinfo")) {
                    return new MockResponse().setResponseCode(500)
                            .setBody("{\n" +
                                    "    \"error\": true,\n" +
                                    "    \"message\": \"Error...\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for success response
    private Dispatcher oAuthDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/protocol/openid-connect/auth?scope=http%3A%2F%2F192.168.100.58%3A8080%2Fauth%2Frealms%2Fdirbs&redirect_uri=com.qualcomm.dvsauthorized%3A%2Fcallback&client_id=sso&state=daa46602-faed-4e78-8965-78bceb41bfe5&response_type=code")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                                    "<html xmlns=\"http://www.w3.org/1999/xhtml\" class=\"login-pf\">\n" +
                                    "    <head>\n" +
                                    "        <meta charset=\"utf-8\">\n" +
                                    "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                                    "        <meta name=\"robots\" content=\"noindex, nofollow\">\n" +
                                    "        <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"/>\n" +
                                    "        <title>        Log in to dirbs\n" +
                                    "</title>\n" +
                                    "        <link rel=\"icon\" href=\"/auth/resources/3.4.3.final/login/dirbs/img/favicon.ico\" />\n" +
                                    "        <link href=\"/auth/resources/3.4.3.final/login/dirbs/lib/patternfly/css/patternfly.css\" rel=\"stylesheet\" />\n" +
                                    "        <link href=\"/auth/resources/3.4.3.final/login/dirbs/lib/zocial/zocial.css\" rel=\"stylesheet\" />\n" +
                                    "        <link href=\"/auth/resources/3.4.3.final/login/dirbs/css/login.css\" rel=\"stylesheet\" />\n" +
                                    "    </head>\n" +
                                    "    <body class=\"\">\n" +
                                    "        <div id=\"kc-logo\">\n" +
                                    "            <a href=\"http://www.keycloak.org\">\n" +
                                    "                <div id=\"kc-logo-wrapper\"></div>\n" +
                                    "            </a>\n" +
                                    "        </div>\n" +
                                    "        <div id=\"kc-container\" class=\"\">\n" +
                                    "            <div id=\"kc-container-wrapper\" class=\"\">\n" +
                                    "                <div id=\"kc-header\" class=\"\">\n" +
                                    "                    <div id=\"kc-header-wrapper\" class=\"\">        Login to \n" +
                                    "                        <span>dirbs</span>\n" +
                                    "                    </div>\n" +
                                    "                </div>\n" +
                                    "                <div id=\"kc-content\" class=\"\">\n" +
                                    "                    <div id=\"kc-content-wrapper\" class=\"\">\n" +
                                    "                        <div id=\"kc-form\" class=\"\">\n" +
                                    "                            <div id=\"kc-form-wrapper\" class=\"\">\n" +
                                    "                                <form id=\"kc-form-login\" class=\"\" onsubmit=\"login.disabled = true; return true;\" action=\"http://192.168.100.58:8080/auth/realms/dirbs/login-actions/authenticate?code=xZiTXTF5-imnM2_gLrtaBGQ6IGZiF7qLfyWUu4xfdXU&amp;execution=3a5d6b3c-5844-4588-b365-5a2bf8be7805&amp;client_id=sso&amp;tab_id=p92A5_AfBe8\" method=\"post\">\n" +
                                    "                                    <div class=\"form-group\">\n" +
                                    "                                        <div class=\"labelbox\">\n" +
                                    "                                            <label for=\"username\" class=\"control-label\">Username or email</label>\n" +
                                    "                                        </div>\n" +
                                    "                                        <div class=\"fieldbox\">\n" +
                                    "                                            <input tabindex=\"1\" id=\"username\" class=\"form-control\" name=\"username\" value=\"\" type=\"text\" autofocus autocomplete=\"off\" placeholder=\"Username\" />\n" +
                                    "                                        </div>\n" +
                                    "                                    </div>\n" +
                                    "                                    <div class=\"form-group\">\n" +
                                    "                                        <div class=\"labelbox\">\n" +
                                    "                                            <label for=\"password\" class=\"control-label\">Password</label>\n" +
                                    "                                        </div>\n" +
                                    "                                        <div class=\"fieldbox\">\n" +
                                    "                                            <input tabindex=\"2\" id=\"password\" class=\"form-control\" name=\"password\" type=\"password\" autocomplete=\"off\" placeholder=\"Password\" />\n" +
                                    "                                        </div>\n" +
                                    "                                    </div>\n" +
                                    "                                    <div class=\"form-group\">\n" +
                                    "                                        <div id=\"kc-form-options\" class=\"submit-before\">\n" +
                                    "                                            <div class=\"\">\n" +
                                    "                        </div>\n" +
                                    "                                        </div>\n" +
                                    "                                        <div id=\"kc-form-buttons\" class=\"submit\">\n" +
                                    "                                            <div class=\"\">\n" +
                                    "                                                <input tabindex=\"4\" class=\"btn btn-primary btn-lg\" name=\"login\" id=\"kc-login\" type=\"submit\" value=\"Log in\"/>\n" +
                                    "                                            </div>\n" +
                                    "                                        </div>\n" +
                                    "                                    </div>\n" +
                                    "                                </form>\n" +
                                    "                            </div>\n" +
                                    "                        </div>\n" +
                                    "                        <div id=\"kc-info\" class=\"details\">\n" +
                                    "                            <div id=\"kc-info-wrapper\" class=\"\">\n" +
                                    "                                <div id=\"kc-registration\">\n" +
                                    "                                    <span>New user? \n" +
                                    "                                        <a tabindex=\"6\" href=\"/auth/realms/dirbs/login-actions/registration?client_id=sso&amp;tab_id=p92A5_AfBe8\">Register</a>\n" +
                                    "                                    </span>\n" +
                                    "                                </div>\n" +
                                    "                            </div>\n" +
                                    "                        </div>\n" +
                                    "                    </div>\n" +
                                    "                </div>\n" +
                                    "            </div>\n" +
                                    "        </div>\n" +
                                    "    </body>\n" +
                                    "</html>");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Unit test for userResponse Model calss
    @Test
    public void userInfoResponseModel(){

        String userInfoResponseString = "{\n" +
                "    \"sub\": \"fe5f9832-be43-49e6-a1dc-5d5bec8f6aa7\",\n" +
                "    \"name\": \"Hamza Shahid\",\n" +
                "    \"preferred_username\": \"hamza\",\n" +
                "    \"given_name\": \"Hamza\",\n" +
                "    \"family_name\": \"Shahid\",\n" +
                "    \"email\": \"hamza@3gca.org\"\n" +
                "}";
        Gson gson = new Gson();
        UserInfoResponse userInfoResponse = gson.fromJson(userInfoResponseString, UserInfoResponse.class);

        assertEquals(userInfoResponse.toString(), "UserInfoResponse{" +
                "sub = '" + userInfoResponse.getSub() + '\'' +
                ",name = '" + userInfoResponse.getName() + '\'' +
                ",preferred_username = '" + userInfoResponse.getPreferredUsername() + '\'' +
                ",given_name = '" + userInfoResponse.getGivenName() + '\'' +
                ",family_name = '" + userInfoResponse.getFamilyName() + '\'' +
                ",email = '" + userInfoResponse.getEmail() + '\'' +
                "}");

        UserInfoResponse userInfoResp = new UserInfoResponse();
        userInfoResp.setSub("a");
        userInfoResp.setName("b");
        userInfoResp.setPreferredUsername("c");
        userInfoResp.setGivenName("d");
        userInfoResp.setFamilyName("e");
        userInfoResp.setEmail("f");
        assertEquals("a", userInfoResp.getSub());
        assertEquals("b", userInfoResp.getName());
        assertEquals("c", userInfoResp.getPreferredUsername());
        assertEquals("d", userInfoResp.getGivenName());
        assertEquals("e", userInfoResp.getFamilyName());
        assertEquals("f", userInfoResp.getEmail());
    }

}

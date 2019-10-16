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

package com.qualcom.dvsauthorized.mResultActivityTests;

import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.qualcom.dvsauthorized.mTestUtilities.ImeiStatusResponses;
import com.qualcom.dvsauthorized.mTestUtilities.MockServerRule;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ImeiStatusResponse;
import com.qualcomm.dvsauthorized.mMainScreens.MainResultActivity;
import com.qualcomm.dvsauthorized.mUtils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

// Unit tests for main result activity
@RunWith(AndroidJUnit4.class)
public class MainResultActivityTests {

    @Rule
    public final ActivityTestRule<MainResultActivity> mainResultActivityActivityTestRule = new ActivityTestRule<>(MainResultActivity.class,
            true, false);

    @Rule
    public final MockServerRule mMockServerRule = new MockServerRule();

    @Before
    public void setUp() {
        Constants.IMEI_STATUS_URL = "http://localhost:8000";
        Intents.init();
    }

    @After
    public void restLoginUrl() {
        Constants.IMEI_STATUS_URL = "http://192.168.100.74/";
        Intents.release();
        mMockServerRule.stopServer();
    }

    // For launching main result activity
    public void mainResultActivity(){

        Intent intent = new Intent();
        Gson gson = new Gson();
        ImeiStatusResponse imeiStatusResponse = gson.fromJson(ImeiStatusResponses.successResponse, ImeiStatusResponse.class);
        intent.putExtra("imeiStatusResponse", imeiStatusResponse);
        mainResultActivityActivityTestRule.launchActivity(intent);

    }

    // Unit test bottom navigation menu items
    @Test
    public void bottomNavigation(){
        mainResultActivity();
        onView(withText("12345678912345")).check(matches(isDisplayed()));

        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));

        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("Last Seen")).check(matches(isDisplayed()));

        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_device_status));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("12345678912345")).check(matches(isDisplayed()));
    }

    // Imei device status fragment test
    @Test
    public void imeiStatusFragment(){

        mainResultActivity();
        onView(withText("12345678912345")).check(matches(isDisplayed()));
    }

    // Subscribes fragment tests begin
    @Test
    public void subscribers(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("MSISDN")).check(matches(isDisplayed()));
        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));
    }

    // Unit test load more button in subscribers fragment (success response)
    @Test
    public void loadMoreSubSuccess(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("MSISDN")).check(matches(isDisplayed()));
        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreSubSuccessDispatcher());
        onView(withId(R.id.loadMoreSubBtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withText("112233445566778899")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(not(isDisplayed())));

    }

    // Unit test load more button in subscribers fragment (no more data response)
    @Test
    public void loadMoreSubNoData(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("MSISDN")).check(matches(isDisplayed()));
        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreSubNoDataDispatcher());
        onView(withId(R.id.loadMoreSubBtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withText("No more data available.")).inRoot(withDecorView(not(mainResultActivityActivityTestRule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(not(isDisplayed())));

    }

    // Unit test load more button in subscribers fragment (json error response)
    @Test
    public void loadMoreSubJsonError(){
        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("MSISDN")).check(matches(isDisplayed()));
        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreJsonDispatcher());
        onView(withId(R.id.loadMoreSubBtn)).perform(click());

        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));
    }

    // Unit test load more button in subscribers fragment (422 error response)
    @Test
    public void loadMoreSubError(){
        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("MSISDN")).check(matches(isDisplayed()));
        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreErrorDispatcher());
        onView(withId(R.id.loadMoreSubBtn)).perform(click());

        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));
    }

    // Unit test load more button in subscribers fragment (401 error response)
    @Test
    public void loadMoreSubError2(){
        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_seen_with));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("MSISDN")).check(matches(isDisplayed()));
        onView(withText(R.string.seen_with)).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreError2Dispatcher());
        onView(withId(R.id.loadMoreSubBtn)).perform(click());

        onView(withId(R.id.loadMoreSubBtn)).check(matches(isDisplayed()));
    }
    // Subscribers fragment tests end

    // Paired subscribers fragment tests start
    @Test
    public void pairedSubscribers(){
        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        onView(withText("Last Seen")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));
    }

    // Unit test load more button in paired subscribers fragment (success response)
    @Test
    public void loadMorePairedSuccess(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("Last Seen")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMorePairedSuccessDispatcher());
        onView(withId(R.id.loadMorebtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withText("9999-99-99")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(not(isDisplayed())));

    }

    // Unit test load more button in paired subscribers fragment (no more data response)
    @Test
    public void loadMorePairedNoData(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("Last Seen")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMorePairedNoDataDispatcher());
        onView(withId(R.id.loadMorebtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withText("No more data available.")).inRoot(withDecorView(not(mainResultActivityActivityTestRule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
        onView(withId(R.id.loadMoreSubBtn)).check(matches(not(isDisplayed())));

    }

    // Unit test load more button in paired subscribers fragment (json error response)
    @Test
    public void loadMorePairedJsonError(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("Last Seen")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreJsonDispatcher());
        onView(withId(R.id.loadMorebtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));
    }

    // Unit test load more button in paired subscribers fragment (422 error response)
    @Test
    public void loadMorePairedError(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("Last Seen")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreErrorDispatcher());
        onView(withId(R.id.loadMorebtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));
    }

    // Unit test load more button in paired subscribers fragment (401 error response)
    @Test
    public void loadMorePairedError2(){

        mainResultActivity();
        BottomNavigationView bottomNavigationView = mainResultActivityActivityTestRule.getActivity().findViewById(R.id.navigation);
        try {
            mainResultActivityActivityTestRule.runOnUiThread(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_subscribers));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        onView(withText("Last Seen")).check(matches(isDisplayed()));
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));

        mMockServerRule.server().setDispatcher(loadMoreError2Dispatcher());
        onView(withId(R.id.loadMorebtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.loadMorebtn)).check(matches(isDisplayed()));
    }

    // Dispatcher for success response for subscribers load more button
    private Dispatcher loadMoreSubSuccessDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(ImeiStatusResponses.loadMoreSubResponse);
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for no more data response for subscribers load more button
    private Dispatcher loadMoreSubNoDataDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(ImeiStatusResponses.loadMoreSubNoData);
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for json error response for subscribers load more button
    private Dispatcher loadMoreJsonDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody("\"error\": \"invalid_request\",\n" +
                                    "\"error_description\": \"User session not found or doesn't have client attached on it\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for error response for subscribers/paired subscribers load more button
    private Dispatcher loadMoreErrorDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(422)
                            .setBody("{\n" +
                                    "    \"messages\": {\n" +
                                    "        \"imei\": [\n" +
                                    "            \"Enter IMEI.\"\n" +
                                    "        ]\n" +
                                    "    }\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for success response for subscribers/paired subscribers load more button
    private Dispatcher loadMoreError2Dispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(401)
                            .setBody("{\n" +
                                    "    \"error\": \"invalid_request\",\n" +
                                    "    \"error_description\": \"error\"\n" +
                                    "}");
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for success response for paired subscribers load more button
    private Dispatcher loadMorePairedSuccessDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(ImeiStatusResponses.loadMorePairedResponse);
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Dispatcher for no more data response for paired subscribers load more button
    private Dispatcher loadMorePairedNoDataDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(ImeiStatusResponses.loadMorePairedNoData);
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }
}

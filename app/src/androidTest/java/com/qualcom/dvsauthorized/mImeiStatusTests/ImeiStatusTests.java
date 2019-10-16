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

package com.qualcom.dvsauthorized.mImeiStatusTests;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.qualcom.dvsauthorized.mTestUtilities.ImeiStatusResponses;
import com.qualcom.dvsauthorized.mTestUtilities.MockServerRule;
import com.qualcom.dvsauthorized.mTestUtilities.TestUtils;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mModels.imeiData.DeviceStatusData;
import com.qualcomm.dvsauthorized.mModels.imeiData.PairedImsiData;
import com.qualcomm.dvsauthorized.mModels.imeiData.SeenWithData;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.BlockingConditionsItem;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ClassificationState;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.Compliant;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.DataItem;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.Gsma;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ImeiStatusResponse;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.InformativeConditionsItem;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.Pairs;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.Subscribers;
import com.qualcomm.dvsauthorized.mMainScreens.MainTabActivity;
import com.qualcomm.dvsauthorized.mMainScreens.ScannerActivity;
import com.qualcomm.dvsauthorized.mUtils.Constants;
import com.qualcomm.dvsauthorized.mUtils.MyPreferences;
import com.qualcomm.dvsauthorized.mUtils.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.qualcom.dvsauthorized.mTestUtilities.TestUtils.withDrawable;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Unit tests for imei status/verify imei fragments /scanner activity
@RunWith(AndroidJUnit4.class)
public class ImeiStatusTests {

    @Rule
    public final ActivityTestRule<MainTabActivity> mainTabActivityActivityTestRule = new ActivityTestRule<>(MainTabActivity.class,
            true, false);

    @Rule
    public final ActivityTestRule<ScannerActivity> scannerActivityActivityTestRule = new ActivityTestRule<>(ScannerActivity.class,
            true, false);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

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

    // For launching mainTabActivity
    public void mainTabActivity() {

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

    // Unit test for empty text in imei edit text
    @Test
    public void emptyImei() {

        mainTabActivity();

        String imei = "";
        onView(withId(R.id.imei_et)).perform(clearText());
        onView(withId(R.id.imei_et)).perform(typeText(imei), closeSoftKeyboard());
        onView(withId(R.id.submit_btn)).perform(click());

        onView(withId(R.id.imeiTil)).check
                (matches(TestUtils.inputLayoutHasErrorText(mainTabActivityActivityTestRule.getActivity().getString(R.string.length_error))));

        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test for invalid text in imei edit text
    @Test
    public void invalidImei() {

        mainTabActivity();

        String imei = "abcefghijklbjf";
        onView(withId(R.id.imei_et)).perform(clearText());
        onView(withId(R.id.imei_et)).perform(typeText(imei), closeSoftKeyboard());
        onView(withId(R.id.submit_btn)).perform(click());

        onView(withId(R.id.imeiTil)).check
                (matches(TestUtils.inputLayoutHasErrorText(mainTabActivityActivityTestRule.getActivity().getString(R.string.hexa_error))));

        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test scanner button click
    @Test
    public void scanner(){

        mainTabActivity();

//        onView(withId(R.id.scannerBtn)).perform(click());
        intended(hasComponent(ScannerActivity.class.getName()));

        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test for flash button inside scanner activity
    @Test
    public void flashBtn(){

        mainTabActivity();
//        onView(withId(R.id.scannerBtn)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        intended(hasComponent(ScannerActivity.class.getName()));

        boolean hasFlash = mainTabActivityActivityTestRule.getActivity().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (hasFlash) {
            onView(withId(R.id.flashBtn)).perform(click());
            onView(withId(R.id.flashBtn)).check(matches(withDrawable(R.drawable.ic_action_flash_on)));

            InstrumentationRegistry.getInstrumentation().waitForIdleSync();

            onView(withId(R.id.flashBtn)).perform(click());
            onView(withId(R.id.flashBtn)).check(matches(withDrawable(R.drawable.ic_action_flash_off)));
        } else {
            onView(withId(R.id.flashBtn)).check(matches(not(isDisplayed())));
        }
        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());

    }

    // Unit test for nav up button in scanner activity
    @Test
    public void scannerNavUpButton(){

        mainTabActivity();
//        onView(withId(R.id.scannerBtn)).perform(click());
        intended(hasComponent(ScannerActivity.class.getName()));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        intended(hasComponent(MainTabActivity.class.getName()));

        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test scanner result callback method
    @Test
    public void scannerCallback(){

        Intent intent = new Intent();
        scannerActivityActivityTestRule.launchActivity(intent);

        intended(hasComponent(ScannerActivity.class.getName()));

        String imei = "123456789012345";

        Activity scannerActivity = scannerActivityActivityTestRule.getActivity();
        Result resultZxing = new Result(imei, null, null ,null);
        BarcodeResult result = new BarcodeResult(resultZxing, null);
        ((ScannerActivity)scannerActivity).callback.barcodeResult(result);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        intended(hasComponent(MainTabActivity.class.getName()));
        onView(withText("123456789012345")).check(matches(isDisplayed()));

        Utils.deleteToken(scannerActivityActivityTestRule.getActivity());
    }

    // Unit test for successful imei response from api
    @Test
    public void imeiStatusSuccess() {

        mainTabActivity();

        String imei = "12345678912345";
        onView(withId(R.id.imei_et)).perform(clearText());
        onView(withId(R.id.imei_et)).perform(typeText(imei), closeSoftKeyboard());
        mMockServerRule.server().setDispatcher(statusSuccessDispatcher());
        onView(withId(R.id.submit_btn)).perform(click());

        onView(withText("12345678912345")).check(matches(isDisplayed()));
        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test for json error in response from api
    @Test
    public void imeiStatusJsonError() {

        mainTabActivity();

        String imei = "12345678912345";
        onView(withId(R.id.imei_et)).perform(clearText());
        onView(withId(R.id.imei_et)).perform(typeText(imei), closeSoftKeyboard());
        mMockServerRule.server().setDispatcher(statusJsonDispatcher());
        onView(withId(R.id.submit_btn)).perform(click());

        intended(hasComponent(MainTabActivity.class.getName()));
        onView(withId(R.id.submit_btn)).check(matches(isDisplayed()));
        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test for 422 error response from api
    @Test
    public void imeiStatusError() {

        mainTabActivity();

        String imei = "12345678912345";
        onView(withId(R.id.imei_et)).perform(clearText());
        onView(withId(R.id.imei_et)).perform(typeText(imei), closeSoftKeyboard());
        mMockServerRule.server().setDispatcher(statusErrorDispatcher());
        onView(withId(R.id.submit_btn)).perform(click());

        intended(hasComponent(MainTabActivity.class.getName()));
        onView(withId(R.id.submit_btn)).check(matches(isDisplayed()));
        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Unit test for 401 error response from api
    @Test
    public void imeiStatusError2() {

        mainTabActivity();

        String imei = "12345678912345";
        onView(withId(R.id.imei_et)).perform(clearText());
        onView(withId(R.id.imei_et)).perform(typeText(imei), closeSoftKeyboard());
        mMockServerRule.server().setDispatcher(statusError2Dispatcher());
        onView(withId(R.id.submit_btn)).perform(click());

        intended(hasComponent(MainTabActivity.class.getName()));
        onView(withId(R.id.submit_btn)).check(matches(isDisplayed()));
        Utils.deleteToken(mainTabActivityActivityTestRule.getActivity());
    }

    // Mock server dispatcher for success imei status response
    private Dispatcher statusSuccessDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/api/v1/fullstatus")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(ImeiStatusResponses.successResponse);
                }
                throw new IllegalStateException("no mock set up for " + request.getPath());
            }
        };
    }

    // Mock server dispatcher for json error response
    private Dispatcher statusJsonDispatcher() {
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

    // Mock server dispatcher for 422 error response
    private Dispatcher statusErrorDispatcher() {
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

    // Mock server dispatcher for 401 error response
    private Dispatcher statusError2Dispatcher() {
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

    // Unit test of all model classes for imei status
    @Test
    public void imeiStatusModelsTest(){

        // Data model
        DeviceStatusData deviceStatusData = new DeviceStatusData("a", "b");
        deviceStatusData.setTitle("aa");
        deviceStatusData.setValue("bb");
        assertEquals("aa", deviceStatusData.getTitle());
        assertEquals("bb", deviceStatusData.getValue());

        PairedImsiData pairedImsiData = new PairedImsiData("a", "b");
        pairedImsiData.setImsi("aa");
        pairedImsiData.setLastSeen("bb");
        assertEquals("aa", pairedImsiData.getImsi());
        assertEquals("bb", pairedImsiData.getLastSeen());

        SeenWithData seenWithData = new SeenWithData("a", "b", "c");
        seenWithData.setImsi("aa");
        seenWithData.setLastSeen("bb");
        seenWithData.setMsisdn("cc");
        assertEquals("aa", seenWithData.getImsi());
        assertEquals("bb", seenWithData.getLastSeen());
        assertEquals("cc", seenWithData.getMsisdn());

        // Response Model
        Gson gson = new Gson();
        ImeiStatusResponse imeiStatusResp = gson.fromJson(ImeiStatusResponses.loadMoreSubResponse, ImeiStatusResponse.class);
        assertEquals(imeiStatusResp.toString(), "ImeiStatusResponse{" +
                "classification_state = '" + imeiStatusResp.getClassificationState() + '\'' +
                ",gsma = '" + imeiStatusResp.getGsma() + '\'' +
                ",stolen_status = '" + imeiStatusResp.getStolenStatus() + '\'' +
                ",registration_status = '" + imeiStatusResp.getRegistrationStatus() + '\'' +
                ",subscribers = '" + imeiStatusResp.getSubscribers() + '\'' +
                ",imei = '" + imeiStatusResp.getImei() + '\'' +
                ",compliant = '" + imeiStatusResp.getCompliant() + '\'' +
                ",pairs = '" + imeiStatusResp.getPairs() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getClassificationState().getBlockingConditions().get(0).toString(), "BlockingConditionsItem{" +
                "condition_met = '" + imeiStatusResp.getClassificationState().getBlockingConditions().get(0).isConditionMet() + '\'' +
                ",condition_name = '" + imeiStatusResp.getClassificationState().getBlockingConditions().get(0).getConditionName() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getClassificationState().toString(), "ClassificationState{" +
                "informative_conditions = '" + imeiStatusResp.getClassificationState().getInformativeConditions() + '\'' +
                ",blocking_conditions = '" + imeiStatusResp.getClassificationState().getBlockingConditions() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getCompliant().toString(), "Compliant{" +
                "block_date = '" + imeiStatusResp.getCompliant().getBlockDate() + '\'' +
                ",status = '" + imeiStatusResp.getCompliant().getStatus() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getPairs().getData().get(0).toString(), "DataItem{" +
                "last_seen = '" + imeiStatusResp.getPairs().getData().get(0).getLastSeen() + '\'' +
                ",imsi = '" + imeiStatusResp.getPairs().getData().get(0).getImsi() + '\'' +
                ",msisdn = '" + imeiStatusResp.getPairs().getData().get(0).getMsisdn() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getGsma().toString(), "Gsma{" +
                "model_name = '" + imeiStatusResp.getGsma().getModelName() + '\'' +
                ",radio_access_technology = '" + imeiStatusResp.getGsma().getRadioAccessTechnology() + '\'' +
                ",operating_system = '" + imeiStatusResp.getGsma().getOperatingSystem() + '\'' +
                ",device_type = '" + imeiStatusResp.getGsma().getDeviceType() + '\'' +
                ",model_number = '" + imeiStatusResp.getGsma().getModelNumber() + '\'' +
                ",brand = '" + imeiStatusResp.getGsma().getBrand() + '\'' +
                ",manufacturer = '" + imeiStatusResp.getGsma().getManufacturer() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getClassificationState().getInformativeConditions().get(0).toString(), "InformativeConditionsItem{" +
                "condition_met = '" + imeiStatusResp.getClassificationState().getInformativeConditions().get(0).isConditionMet() + '\'' +
                ",condition_name = '" + imeiStatusResp.getClassificationState().getInformativeConditions().get(0).getConditionName() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getPairs().toString(), "Pairs{" +
                "data = '" + imeiStatusResp.getPairs().getData() + '\'' +
                ",count = '" + imeiStatusResp.getPairs().getCount() + '\'' +
                ",limit = '" + imeiStatusResp.getPairs().getLimit() + '\'' +
                ",start = '" + imeiStatusResp.getPairs().getStart() + '\'' +
                "}");

        assertEquals(imeiStatusResp.getSubscribers().toString(), "Subscribers{" +
                "data = '" + imeiStatusResp.getSubscribers().getData() + '\'' +
                ",count = '" + imeiStatusResp.getSubscribers().getCount() + '\'' +
                ",limit = '" + imeiStatusResp.getSubscribers().getLimit() + '\'' +
                ",start = '" + imeiStatusResp.getSubscribers().getStart() + '\'' +
                "}");

        BlockingConditionsItem blockingConditionsItem = new BlockingConditionsItem();
        blockingConditionsItem.setConditionMet(true);
        blockingConditionsItem.setConditionName("a");
        assertTrue(blockingConditionsItem.isConditionMet());
        assertEquals("a", blockingConditionsItem.getConditionName());

        Compliant compliant = new Compliant();
        compliant.setBlockDate("a");
        compliant.setStatus("b");
        assertEquals("a", compliant.getBlockDate());
        assertEquals("b", compliant.getStatus());

        DataItem dataItem = new DataItem();
        dataItem.setImsi("a");
        dataItem.setLastSeen("b");
        dataItem.setMsisdn("c");
        assertEquals("a", dataItem.getImsi());
        assertEquals("b", dataItem.getLastSeen());
        assertEquals("c", dataItem.getMsisdn());

        Gsma gsma = new Gsma();
        gsma.setBrand("a");
        gsma.setDeviceType("b");
        gsma.setManufacturer("c");
        gsma.setModelName("d");
        gsma.setModelNumber("e");
        gsma.setOperatingSystem("f");
        gsma.setRadioAccessTechnology("g");
        assertEquals("a", gsma.getBrand());
        assertEquals("b", gsma.getDeviceType());
        assertEquals("c", gsma.getManufacturer());
        assertEquals("d", gsma.getModelName());
        assertEquals("e", gsma.getModelNumber());
        assertEquals("f", gsma.getOperatingSystem());
        assertEquals("g", gsma.getRadioAccessTechnology());

        InformativeConditionsItem informativeConditionsItem = new InformativeConditionsItem();
        informativeConditionsItem.setConditionMet(true);
        informativeConditionsItem.setConditionName("a");
        assertTrue(informativeConditionsItem.isConditionMet());
        assertEquals("a", informativeConditionsItem.getConditionName());

        ArrayList<DataItem> dataItemArrayList = new ArrayList<>();

        Pairs pairs = new Pairs();
        pairs.setCount(1);
        pairs.setLimit(2);
        pairs.setStart(3);
        pairs.setData(dataItemArrayList);
        assertEquals(1, pairs.getCount());
        assertEquals(2, pairs.getLimit());
        assertEquals(3, pairs.getStart());
        assertEquals(dataItemArrayList, pairs.getData());

        Subscribers subscribers = new Subscribers();
        subscribers.setCount(1);
        subscribers.setData(dataItemArrayList);
        subscribers.setLimit(2);
        subscribers.setStart(3);
        assertEquals(1, subscribers.getCount());
        assertEquals(dataItemArrayList, subscribers.getData());
        assertEquals(2, subscribers.getLimit());
        assertEquals(3, subscribers.getStart());

        ArrayList<InformativeConditionsItem> informativeConditionsItemArrayList = new ArrayList<>();
        ArrayList<BlockingConditionsItem> blockingConditionsItemArrayList = new ArrayList<>();

        ClassificationState classificationState = new ClassificationState();
        classificationState.setInformativeConditions(informativeConditionsItemArrayList);
        classificationState.setBlockingConditions(blockingConditionsItemArrayList);
        assertEquals(informativeConditionsItemArrayList, classificationState.getInformativeConditions());
        assertEquals(blockingConditionsItemArrayList, classificationState.getBlockingConditions());

        ImeiStatusResponse imeiStatusResponse = new ImeiStatusResponse();
        imeiStatusResponse.setClassificationState(classificationState);
        imeiStatusResponse.setGsma(gsma);
        imeiStatusResponse.setStolenStatus("a");
        imeiStatusResponse.setRegistrationStatus("b");
        imeiStatusResponse.setSubscribers(subscribers);
        imeiStatusResponse.setImei("c");
        imeiStatusResponse.setCompliant(compliant);
        imeiStatusResponse.setPairs(pairs);
        assertEquals(classificationState, imeiStatusResponse.getClassificationState());
        assertEquals(gsma, imeiStatusResponse.getGsma());
        assertEquals("a", imeiStatusResponse.getStolenStatus());
        assertEquals("b", imeiStatusResponse.getRegistrationStatus());
        assertEquals(subscribers, imeiStatusResponse.getSubscribers());
        assertEquals("c", imeiStatusResponse.getImei());
        assertEquals(compliant, imeiStatusResponse.getCompliant());
        assertEquals(pairs, imeiStatusResponse.getPairs());

    }
}

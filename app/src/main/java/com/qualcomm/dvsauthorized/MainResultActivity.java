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
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.view.MenuItem;
import android.widget.TextView;

import com.qualcomm.dvsauthorized.data.DeviceStatusData;
import com.qualcomm.dvsauthorized.data.PairedImsiData;
import com.qualcomm.dvsauthorized.data.SeenWithData;
import com.qualcomm.dvsauthorized.fragments.DeviceStatusFragment;
import com.qualcomm.dvsauthorized.fragments.PairedImsiFragment;
import com.qualcomm.dvsauthorized.fragments.SeenWithFragment;

public class MainResultActivity extends Activity implements DeviceStatusFragment.OnListFragmentInteractionListener, SeenWithFragment.OnListFragmentInteractionListener, PairedImsiFragment.OnListFragmentInteractionListener{

    private TextView mTextMessage;
    String response;
    private static final String TAG_FRAGMENT_DEVICE_STATUS = "fragment_device_status";
    private static final String TAG_FRAGMENT_SEEN_WITH = "fragment_seen_with";
    private static final String TAG_FRAGMENT_SUBSCRIBERS = "fragment_paired_subscribers";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_device_status:
                    fragmentManager.beginTransaction().hide(currentFragment).show(deviceStatusFragment).commit();
                    currentFragment = deviceStatusFragment;
                    return true;
                case R.id.navigation_seen_with:
                    fragmentManager.beginTransaction().hide(currentFragment).show(seenWithFragment).commit();
                    currentFragment = seenWithFragment;
                    return true;
                case R.id.navigation_subscribers:
                    fragmentManager.beginTransaction().hide(currentFragment).show(subscribersFragment).commit();
                    currentFragment = subscribersFragment;
                    return true;
            }
            return false;
        }
    };
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private Fragment deviceStatusFragment;
    private Fragment seenWithFragment;
    private Fragment subscribersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);

        response = getIntent().getStringExtra("response");

        fragmentManager = getFragmentManager();
        deviceStatusFragment = DeviceStatusFragment.newInstance(response);
        seenWithFragment = SeenWithFragment.newInstance(response);
        subscribersFragment = PairedImsiFragment.newInstance(response);


//        loadFragment(DeviceStatusFragment.newInstance(response));
//        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_DEVICE_STATUS);
//        if (fragment == null) {
//            fragment = DeviceStatusFragment.newInstance(response);
//        }
//        loadFragment(fragment, TAG_FRAGMENT_DEVICE_STATUS);
        fragmentManager.beginTransaction().add(R.id.frame_container, seenWithFragment, TAG_FRAGMENT_SEEN_WITH).hide(seenWithFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_container, subscribersFragment, TAG_FRAGMENT_SUBSCRIBERS).hide(subscribersFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_container, deviceStatusFragment, TAG_FRAGMENT_DEVICE_STATUS).commit();
        currentFragment = deviceStatusFragment;


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadFragment(@NonNull Fragment fragment, @NonNull String tag){
        // load fragment
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
        if (!fragment.equals(currentFragment)) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment, tag)
                    .commit();
            currentFragment = fragment;
        }
    }

    @Override
    public void onListFragmentInteraction(DeviceStatusData item) {

    }

    @Override
    public void onListFragmentInteraction(PairedImsiData item) {

    }

    @Override
    public void onListFragmentInteraction(SeenWithData item) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

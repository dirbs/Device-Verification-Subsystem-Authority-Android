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

package com.qualcomm.dvsauthorized.mMainScreens;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mModels.imeiData.DeviceStatusData;
import com.qualcomm.dvsauthorized.mModels.imeiData.PairedImsiData;
import com.qualcomm.dvsauthorized.mModels.imeiData.SeenWithData;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ImeiStatusResponse;
import com.qualcomm.dvsauthorized.mImeiStatus.view.fragments.DeviceStatusFragment;
import com.qualcomm.dvsauthorized.mImeiStatus.view.fragments.PairedImsiFragment;
import com.qualcomm.dvsauthorized.mImeiStatus.view.fragments.SeenWithFragment;

// View class for main result activity
public class MainResultActivity extends FragmentActivity implements DeviceStatusFragment.OnListFragmentInteractionListener, SeenWithFragment.OnListFragmentInteractionListener, PairedImsiFragment.OnListFragmentInteractionListener {

    // Global variables for storing api response, view access.
    ImeiStatusResponse mImeiStatusResponse;
    private static final String TAG_FRAGMENT_DEVICE_STATUS = "fragment_device_status";
    private static final String TAG_FRAGMENT_SEEN_WITH = "fragment_seen_with";
    private static final String TAG_FRAGMENT_SUBSCRIBERS = "fragment_paired_subscribers";

    // Variables for fragments inside this activity
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private Fragment mDeviceStatusFragment;
    private Fragment mSeenWithFragment;
    private Fragment mSubscribersFragment;

    private BottomNavigationView mBottomNavigationView;

    // For handling menu items click on bottom navigation listener to change fragments
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_device_status:
                    mFragmentManager.beginTransaction().hide(mCurrentFragment).show(mDeviceStatusFragment).commit();
                    mCurrentFragment = mDeviceStatusFragment;
                    return true;
                case R.id.navigation_seen_with:
                    mFragmentManager.beginTransaction().hide(mCurrentFragment).show(mSeenWithFragment).commit();
                    mCurrentFragment = mSeenWithFragment;
                    return true;
                case R.id.navigation_subscribers:
                    mFragmentManager.beginTransaction().hide(mCurrentFragment).show(mSubscribersFragment).commit();
                    mCurrentFragment = mSubscribersFragment;
                    return true;
            }
            return false;
        }
    };

    // Invoked upon opening of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);

        mImeiStatusResponse = (ImeiStatusResponse) getIntent().getSerializableExtra("imeiStatusResponse");

        mFragmentManager = this.getSupportFragmentManager();
        mDeviceStatusFragment = DeviceStatusFragment.newInstance(mImeiStatusResponse);
        mSeenWithFragment = SeenWithFragment.newInstance(mImeiStatusResponse);
        mSubscribersFragment = PairedImsiFragment.newInstance(mImeiStatusResponse);

        mFragmentManager.beginTransaction().add(R.id.frame_container, mSeenWithFragment, TAG_FRAGMENT_SEEN_WITH).hide(mSeenWithFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.frame_container, mSubscribersFragment, TAG_FRAGMENT_SUBSCRIBERS).hide(mSubscribersFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.frame_container, mDeviceStatusFragment, TAG_FRAGMENT_DEVICE_STATUS).commit();
        mCurrentFragment = mDeviceStatusFragment;

        mBottomNavigationView = findViewById(R.id.navigation);
        removePadding();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void removePadding() {
        // https://github.com/material-components/material-components-android/issues/139
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            View view = item.findViewById(R.id.largeLabel);
            if (view instanceof TextView) {
                view.setPadding(0, 0, 0, 0);
            }
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
}

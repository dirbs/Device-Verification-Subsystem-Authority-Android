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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mLogin.view.LoginActivity;
import com.qualcomm.dvsauthorized.mMainScreens.viewModel.LogoutViewModel;
import com.qualcomm.dvsauthorized.mUtils.ConnectionDetector;
import com.qualcomm.dvsauthorized.mUtils.MyPreferences;
import com.qualcomm.dvsauthorized.mUtils.Utils;
import com.tapadoo.alerter.Alerter;

// View class for profile fragment
public class ProfileFragment extends Fragment {

    private LogoutViewModel mLogoutViewModel;
    private ProgressBar mProgressBar;
    private MaterialButton mLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // Invoked when fragment is first opened, initialized view variables and click listeners
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mLogout = view.findViewById(R.id.logout_btn);
        TextView name = view.findViewById(R.id.name_txt);
        TextView username = view.findViewById(R.id.username_txt);
        TextView email = view.findViewById(R.id.email_txt);
        mProgressBar = view.findViewById(R.id.progressBar);

        mLogoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);
        observeModel(mLogoutViewModel);

        MyPreferences myPreferences = new MyPreferences(getActivity());

        name.setText(myPreferences.getString("name", "-"));
        email.setText(myPreferences.getString("email", "-"));
        username.setText(myPreferences.getString("username", "-"));

        // Logout button click listener
        mLogout.setOnClickListener(v -> {
            mLogout.setEnabled(false);
            mLogout.getBackground().setAlpha(128);
            mProgressBar.setVisibility(View.VISIBLE);
            ConnectionDetector cd = new ConnectionDetector(getActivity());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                logout();
            } else {
                mProgressBar.setVisibility(View.GONE);
                mLogout.setEnabled(true);
                mLogout.getBackground().setAlpha(255);
                Utils.deleteToken(getActivity());
                Intent mainIntent = new Intent(getActivity(), LoginActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    // Requesting view model to send request to api to remove user session
    private void logout() {
        mLogoutViewModel.logout(Utils.getToken(getActivity()));
    }

    // Observer to observer api response and remove user access token from storage and take back to login screen
    private void observeModel(LogoutViewModel logoutViewModel) {

        logoutViewModel.getLogoutLiveData().observe(this, apiResponse -> {

            mProgressBar.setVisibility(View.GONE);
            mLogout.setEnabled(true);
            mLogout.getBackground().setAlpha(255);

            Utils.deleteToken(getActivity());
            Intent mainIntent = new Intent(getActivity(), LoginActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            getActivity().finish();
        });
    }

    // Alert user if response is an error
    private void showAlerter(String message) {
        Alerter.create(getActivity())
                .setTitle("Oops...")
                .setText(message)
                .setIcon(R.drawable.ic_error)
                .setBackgroundColorRes(R.color.colorRed)
                .setIconColorFilter(0) // Optional - Removes white tint
                .show();
    }

}

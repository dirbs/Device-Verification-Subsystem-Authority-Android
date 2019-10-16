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

package com.qualcomm.dvsauthorized.mImeiStatus.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.databinding.FragmentPairedimsiListBinding;
import com.qualcomm.dvsauthorized.mImeiStatus.view.adapters.MyPairedImsiRecyclerViewAdapter;
import com.qualcomm.dvsauthorized.mImeiStatus.viewmodel.ImeiStatusViewModel;
import com.qualcomm.dvsauthorized.mLogin.view.LoginActivity;
import com.qualcomm.dvsauthorized.mMainScreens.viewModel.LogoutViewModel;
import com.qualcomm.dvsauthorized.mModels.imeiData.PairedImsiData;
import com.qualcomm.dvsauthorized.mModels.imeiRequestModel.ImeiStatusRequest;
import com.qualcomm.dvsauthorized.mModels.imeiRequestModel.Pairs;
import com.qualcomm.dvsauthorized.mModels.imeiRequestModel.Subscribers;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ImeiStatusResponse;
import com.qualcomm.dvsauthorized.mUtils.ConnectionDetector;
import com.qualcomm.dvsauthorized.mUtils.Status;
import com.qualcomm.dvsauthorized.mUtils.Utils;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

// View class for paired imei fragment
/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PairedImsiFragment extends Fragment {

    // Global variables for storing api response, view access and paired imei data list.
    private static final String ARG_RESPONSE = "response";

    private RecyclerView mRecyclerView;
    private ImageButton mCancelBtn;
    private TextView mNoRecord;
    private Button mLoadMoreBtn;
    private ProgressBar mProgressBar;
    private OnListFragmentInteractionListener mListener;

    private String mPairedListStart = "1";

    private ImeiStatusResponse mImeiStatusResponse;
    private List<PairedImsiData> mPairedImsiDataList;

    private MyPairedImsiRecyclerViewAdapter mMyPairedImsiRecyclerViewAdapter;
    private ImeiStatusViewModel mImeiStatusViewModel;

    private LogoutViewModel mLogoutViewModel;
    private SweetAlertDialog mProgressDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PairedImsiFragment() {
    }

    // To return instance of paired imei fragment
    @SuppressWarnings("unused")
    public static PairedImsiFragment newInstance(ImeiStatusResponse response) {
        PairedImsiFragment fragment = new PairedImsiFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, response);
        fragment.setArguments(args);
        return fragment;
    }

    // Invoked when fragment is first opened
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mImeiStatusResponse = (ImeiStatusResponse) getArguments().getSerializable(ARG_RESPONSE);
        }
        mImeiStatusViewModel = ViewModelProviders.of(this).get(ImeiStatusViewModel.class);
        observeViewModel(mImeiStatusViewModel);
    }

    // Invoked when fragment is ready to be visible to user, views are initialized and button click listeners added
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPairedimsiListBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pairedimsi_list, container, false);

        // Set the adapter
        mRecyclerView = mBinding.list;
        mCancelBtn = mBinding.cancelBtn;
        mNoRecord = mBinding.noRecordText;
        mProgressBar = mBinding.progressBar;
        mLoadMoreBtn = mBinding.loadMorebtn;

        mPairedImsiDataList = new ArrayList<>();
        mPairedImsiDataList.add(new PairedImsiData(getString(R.string.imsi), getString(R.string.last_seen)));
        setListData(false);

        // Pass the data to custom adapter to set in list
        mMyPairedImsiRecyclerViewAdapter = new MyPairedImsiRecyclerViewAdapter(getActivity(), mPairedImsiDataList);
        mBinding.list.setAdapter(mMyPairedImsiRecyclerViewAdapter);
        Context context = mBinding.getRoot().getContext();
        mBinding.list.setLayoutManager(new LinearLayoutManager(context));
        mBinding.list.setNestedScrollingEnabled(false);

        // Invoked when load more data button clicked (if visible)
        mLoadMoreBtn.setOnClickListener(v -> {
            mCancelBtn.setEnabled(false);
            mLoadMoreBtn.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mPairedListStart = String.valueOf(Integer.parseInt(mPairedListStart) + 10);
            ConnectionDetector cd = new ConnectionDetector(getActivity());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                getImeiStatus();
            } else {
                mCancelBtn.setEnabled(true);
                mLoadMoreBtn.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mPairedListStart = String.valueOf(Integer.parseInt(mPairedListStart) - 10);
            }
        });

        // Cross button click listener for closing fragment/activity
        mCancelBtn.setOnClickListener(v -> getActivity().finish());

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // For fetching more data for imei
    private void getImeiStatus() {

        Subscribers subscribers = new Subscribers("10", "1");
        Pairs pairs = new Pairs("10", mPairedListStart);
        ImeiStatusRequest imeiStatusRequest = new ImeiStatusRequest(mImeiStatusResponse.getImei(), subscribers, pairs);
        mImeiStatusViewModel.getImeiStatus("Bearer " + Utils.getToken(getActivity()), imeiStatusRequest);
    }

    // Observer for imei status view model to observe for any api response after request is sent
    private void observeViewModel(final ImeiStatusViewModel viewModel) {
        // Observe project data

        viewModel.getStatusLiveSata().observe(this, apiResponse -> {
            mCancelBtn.setEnabled(true);
            mLoadMoreBtn.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    mImeiStatusResponse = (ImeiStatusResponse) apiResponse.data;
                    if (mImeiStatusResponse.getPairs().getData().size() != 0) {
                        setListData(true);
                    } else {
                        Toast.makeText(getActivity(), "No more data available.", Toast.LENGTH_SHORT).show();
                        mLoadMoreBtn.setVisibility(View.INVISIBLE);
                    }
                }
                if (apiResponse.status == Status.ERROR) {
                    String error = Utils.errorType(apiResponse.error);
                    if (error == null || error.equals("")) {
                        if (apiResponse.error.fillInStackTrace().toString().contains("JsonSyntaxException")) {
                            error = String.valueOf(R.string.error_json_parsing);
                            showAlerter(error);
                        }
                    } else if (error.equals("Token is not active.")) {
                        showSessionExpireDialog(getActivity());
                    } else {
                        showAlerter(error);
                    }
                }
            }
        });
    }

    // For alerting user with any error response for sent request
    private void showAlerter(String message) {
        int resourceId;
        try {
            resourceId = Integer.parseInt(message);
        } catch (Exception e) {
            resourceId = -1;
            e.printStackTrace();
        }

        if (resourceId != -1) {
            Alerter.create(getActivity())
                    .setTitle("Oops...")
                    .setText(getString(resourceId))
                    .setIcon(R.drawable.ic_error)
                    .setBackgroundColorRes(R.color.colorRed)
                    .setIconColorFilter(0) // Optional - Removes white tint
                    .show();

        } else {
            Alerter.create(getActivity())
                    .setTitle("Oops...")
                    .setText(message)
                    .setIcon(R.drawable.ic_error)
                    .setBackgroundColorRes(R.color.colorRed)
                    .setIconColorFilter(0) // Optional - Removes white tint
                    .show();
        }
    }

    // For showing session expiry dialog
    private void showSessionExpireDialog(Activity context) {

        mLogoutViewModel = ViewModelProviders.of(getActivity()).get(LogoutViewModel.class);
        observeModel(mLogoutViewModel);
        androidx.appcompat.app.AlertDialog.Builder logoutDialog = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.status_dialog, null);
        logoutDialog.setView(dialogView).setPositiveButton(context.getResources().getString(android.R.string.ok), (dialog, whichButton) -> {
            dialog.dismiss();
            mProgressDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.loading));
            mProgressDialog.setTitleText(getResources().getString(R.string.wait));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mLoadMoreBtn.setEnabled(false);
            mLoadMoreBtn.getBackground().setAlpha(128);
            mLogoutViewModel.logout(Utils.getToken(context));
        });

        ImageView icon = dialogView.findViewById(R.id.icon);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);

        icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_warn));
        title.setText(R.string.session_expired);
        message.setText(R.string.session_expired_detail);

        androidx.appcompat.app.AlertDialog alertDialog = logoutDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();
    }

    // Observer to observer api response and remove user access token from storage and take back to login screen
    private void observeModel(LogoutViewModel logoutViewModel) {

        logoutViewModel.getLogoutLiveData().observe(this, apiResponse -> {

            Utils.deleteToken(getActivity());
            Intent mainIntent = new Intent(getActivity(), LoginActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            getActivity().finish();

        });
    }

    // Extract device status data from response to show in paired imei fragment list
    private void setListData(boolean isRefresh) {
        try {

            if (mImeiStatusResponse.getPairs() != null) {
                for (int i = 0; i < mImeiStatusResponse.getPairs().getData().size(); i++) {
                    mPairedImsiDataList.add(new PairedImsiData(mImeiStatusResponse.getPairs().getData().get(i).getImsi()
                            , mImeiStatusResponse.getPairs().getData().get(i).getLastSeen()));
                }
            }
            if (mPairedImsiDataList.size() < 2) {
                mRecyclerView.setVisibility(View.GONE);
                mNoRecord.setVisibility(View.VISIBLE);
            }
            if (mImeiStatusResponse.getPairs() != null && mImeiStatusResponse.getPairs().getData().size() < 10) {
                mLoadMoreBtn.setVisibility(View.GONE);
            }
            if (isRefresh) {
                mMyPairedImsiRecyclerViewAdapter.notifyDataSetChanged();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PairedImsiData item);
    }

}
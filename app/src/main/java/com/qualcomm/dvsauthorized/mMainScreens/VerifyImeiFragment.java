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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.mImeiStatus.viewmodel.ImeiStatusViewModel;
import com.qualcomm.dvsauthorized.mLogin.view.LoginActivity;
import com.qualcomm.dvsauthorized.mMainScreens.viewModel.LogoutViewModel;
import com.qualcomm.dvsauthorized.mModels.imeiRequestModel.ImeiStatusRequest;
import com.qualcomm.dvsauthorized.mModels.imeiRequestModel.Pairs;
import com.qualcomm.dvsauthorized.mModels.imeiRequestModel.Subscribers;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ImeiStatusResponse;
import com.qualcomm.dvsauthorized.mUtils.ConnectionDetector;
import com.qualcomm.dvsauthorized.mUtils.Status;
import com.qualcomm.dvsauthorized.mUtils.Utils;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;

// View class for verify imei fragment

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VerifyImeiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class VerifyImeiFragment extends Fragment {

    // Global variables for storing api response, view access.
    private OnFragmentInteractionListener mListener;
    private OnCompleteListener mCompleteListener;
    private TextInputLayout mErorText;
    private TextInputEditText mImeiEt;
    private MaterialButton mSubmitBtn;
    private ProgressBar mProgressBar;
//    private ImageButton mScannerBtn;
    private LogoutViewModel mLogoutViewModel;
    private SweetAlertDialog mProgressDialog;

    private ImeiStatusViewModel mImeiStatusViewModel;

    public interface OnCompleteListener {
        void onComplete();
    }

    public VerifyImeiFragment() {
        // Required empty public constructor
    }

    // Invoked when fragment is first opened
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImeiStatusViewModel = ViewModelProviders.of(this).get(ImeiStatusViewModel.class);

        observeViewModel(mImeiStatusViewModel);
    }

    // Invoked when fragment is ready to be visible to user, views are initialized and button click listeners added
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_imei, container, false);
        mImeiEt = view.findViewById(R.id.imei_et);
        mErorText = view.findViewById(R.id.imeiTil);
        mSubmitBtn = view.findViewById(R.id.submit_btn);
        mProgressBar = view.findViewById(R.id.progressBar);
//        mScannerBtn = view.findViewById(R.id.scannerBtn);

        mSubmitBtn.setOnClickListener(v -> {
            // for hiding soft keyboard if visible
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != getActivity().getCurrentFocus())
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getApplicationWindowToken(), 0);

            validate();
        });

        // Invoked on clicking scan imei button inside edit text for imei to launch scanner activity
//        mScannerBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), ScannerActivity.class);
//            startActivity(intent);
//        });

        mImeiEt.setOnTouchListener((v, event) -> {

            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (mImeiEt.getRight() - mImeiEt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    Intent intent = new Intent(VerifyImeiFragment.this.getActivity(), ScannerActivity.class);
                    VerifyImeiFragment.this.startActivity(intent);
                    return true;
                }
            }
            return false;
        });

        // Any changes in edit text for imei are listened here.
        mImeiEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            // Validating if entered characters are valid for imei
            @Override
            public void afterTextChanged(Editable s) {
                final String imei = mImeiEt.getText().toString();
                boolean isValid;
                if (imei.length() < 14 || imei.length() > 16) {
                    isValid = false;
                    if (!imei.matches("^(?=.[a-fA-F]*)(?=.[0-9]*)[a-fA-F0-9]+$")) {
                        mErorText.setError(getString(R.string.hexa_error));
                    } else {
                        mErorText.setError(getString(R.string.length_error));
                    }
                } else {
                    if (!imei.matches("^(?=.[a-fA-F]*)(?=.[0-9]*)[a-fA-F0-9]+$")) {
                        mErorText.setError(getString(R.string.hexa_error));
                        isValid = false;
                    } else {
                        isValid = true;
                    }
                }

                if (isValid) {
                    mErorText.setError("");
                }
            }
        });
        mCompleteListener.onComplete();
        return view;
    }

    // For validating iemi string entered in edit text, if correct request is sent to api for details of entered imei
    private void validate() {
        final String imei = mImeiEt.getText().toString();
        if (imei.length() >= 14 && imei.length() <= 16) {
            if (imei.matches("^(?=.[a-fA-F]*)(?=.[0-9]*)[a-fA-F0-9]+$")) {
                mErorText.setError("");
                mProgressBar.setVisibility(View.VISIBLE);
//                mScannerBtn.setEnabled(false);
                mSubmitBtn.setEnabled(false);
                mSubmitBtn.getBackground().setAlpha(128);
                ConnectionDetector cd = new ConnectionDetector(getActivity());
                boolean isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    getImeiStatus();
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSubmitBtn.getBackground().setAlpha(255);
                    mSubmitBtn.setEnabled(true);
//                    mScannerBtn.setEnabled(true);
                    Utils.showNoInternetDialog(getActivity());
                }

            } else {
                mErorText.setError(getString(R.string.hexa_error));
            }
        } else {
            mErorText.setError(getString(R.string.length_error));
        }
    }

    // Method for calling view model to request api for fetching imei data
    private void getImeiStatus() {

        Subscribers subscribers = new Subscribers("10", "1");
        Pairs pairs = new Pairs("10", "1");
        ImeiStatusRequest imeiStatusRequest = new ImeiStatusRequest(mImeiEt.getText().toString(), subscribers, pairs);

        mImeiStatusViewModel.getImeiStatus("Bearer " + Utils.getToken(getActivity()), imeiStatusRequest);
    }

    // Observer to observe view model for any response from server
    private void observeViewModel(final ImeiStatusViewModel viewModel) {
        // Observe project data

        viewModel.getStatusLiveSata().observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    ImeiStatusResponse imeiStatusResponse = (ImeiStatusResponse) apiResponse.data;

                    Intent intent = new Intent(getActivity(), MainResultActivity.class);
                    intent.putExtra("imeiStatusResponse", imeiStatusResponse);
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                }
                if (apiResponse.status == Status.ERROR) {
                    String error = Utils.errorType(apiResponse.error);
                    if (error == null || error.equals("")) {
                        if (apiResponse.error.fillInStackTrace().toString().contains("JsonSyntaxException") ||
                                apiResponse.error.fillInStackTrace().toString().contains("JSONException") ||
                                apiResponse.error.fillInStackTrace().toString().contains("org.json")) {
                            error = String.valueOf(R.string.error_json_parsing);
                            showAlerter(error);
                        }
                    } else if (error.equals("-0")) {
                        showSessionExpireDialog(getActivity());
                    } else {
                        showAlerter(error);
                    }
                }
            }
            mProgressBar.setVisibility(View.INVISIBLE);
            mSubmitBtn.setEnabled(true);
//            mScannerBtn.setEnabled(true);
            mSubmitBtn.getBackground().setAlpha(255);
        });
    }

    // Alert user if reponse is an error
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
            mSubmitBtn.setEnabled(false);
//            mScannerBtn.setEnabled(true);
            mSubmitBtn.getBackground().setAlpha(128);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        try {
            this.mCompleteListener = (OnCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setImeiText(String imei) {
        mImeiEt.setText(imei);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
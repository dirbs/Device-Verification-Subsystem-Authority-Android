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

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.databinding.FragmentDevicestatusListBinding;
import com.qualcomm.dvsauthorized.mImeiStatus.view.adapters.MyDeviceStatusRecyclerViewAdapter;
import com.qualcomm.dvsauthorized.mModels.imeiData.DeviceStatusData;
import com.qualcomm.dvsauthorized.mModels.imeiResponseModel.ImeiStatusResponse;

import java.util.ArrayList;
import java.util.List;

// View class for device status fragment
/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceStatusFragment extends Fragment {

    private static final String ARG_RESPONSE = "response";

    // Global variables for storing api response and device status data list.
    private ImeiStatusResponse mImeiStatusResponse;
    private List<DeviceStatusData> mDeviceStatusData;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceStatusFragment() {
    }

    // To return instance of device status fragment
    @SuppressWarnings("unused")
    public static DeviceStatusFragment newInstance(ImeiStatusResponse response) {
        DeviceStatusFragment fragment = new DeviceStatusFragment();
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
    }

    // Invoked when fragment is ready to be visible to user, views are initialized and button click listeners added
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDevicestatusListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_devicestatus_list, container, false);

        //populate data
        mDeviceStatusData = new ArrayList<>();
        setListData();

        // Pass the data to custom adapter to set in list
        MyDeviceStatusRecyclerViewAdapter mMyDeviceStatusRecyclerViewAdapter = new MyDeviceStatusRecyclerViewAdapter(getActivity(), mDeviceStatusData);
        binding.list.setAdapter(mMyDeviceStatusRecyclerViewAdapter);
        // Set the adapter
        Context context = binding.getRoot().getContext();
        ImageButton cancelBtn = binding.cancelBtn;

        binding.list.setLayoutManager(new LinearLayoutManager(context));

        // Cross button click listener for closing fragment/activity
        cancelBtn.setOnClickListener(v -> getActivity().finish());

        return binding.getRoot();
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

    // Extract device status data from response to show in device status fragment list
    private void setListData() {

        String imeiNumber = getString(R.string.not_available);
        String stolenStatus = getString(R.string.not_available);
        String regStatus = getString(R.string.not_available);
        String brand = getString(R.string.not_available);
        String model = getString(R.string.not_available);
        String model_num = getString(R.string.not_available);
        String manufacturer = getString(R.string.not_available);
        String deviceType = getString(R.string.not_available);
        String operatingSystem = getString(R.string.not_available);
        String radioAccessTech = getString(R.string.not_available);
        String status = getString(R.string.not_available);
        String date = getString(R.string.not_available);

        if (mImeiStatusResponse.getImei() != null) {
            imeiNumber = mImeiStatusResponse.getImei();
        }

        if (mImeiStatusResponse.getGsma() != null) {
            if (mImeiStatusResponse.getGsma().getBrand() != null) {
                brand = mImeiStatusResponse.getGsma().getBrand();
            }
            if (mImeiStatusResponse.getGsma().getModelName() != null) {
                model = mImeiStatusResponse.getGsma().getModelName();
            }
            if (mImeiStatusResponse.getGsma().getModelNumber() != null) {
                model_num = mImeiStatusResponse.getGsma().getModelNumber();
            }
            if (mImeiStatusResponse.getGsma().getManufacturer() != null) {
                manufacturer = mImeiStatusResponse.getGsma().getManufacturer();
            }
            if (mImeiStatusResponse.getGsma().getDeviceType() != null) {
                deviceType = mImeiStatusResponse.getGsma().getDeviceType();
            }
            if (mImeiStatusResponse.getGsma().getOperatingSystem() != null) {
                operatingSystem = mImeiStatusResponse.getGsma().getOperatingSystem().toString();
            }
            if (mImeiStatusResponse.getGsma().getRadioAccessTechnology() != null) {
                radioAccessTech = mImeiStatusResponse.getGsma().getRadioAccessTechnology();
            }
        }

        if (mImeiStatusResponse.getCompliant() != null) {
            if (mImeiStatusResponse.getCompliant().getStatus() != null) {
                status = mImeiStatusResponse.getCompliant().getStatus();
            }
            if (mImeiStatusResponse.getCompliant().getBlockDate() != null) {
                date = mImeiStatusResponse.getCompliant().getBlockDate();
            }
        }
        if (mImeiStatusResponse.getRegistrationStatus() != null) {
            regStatus = mImeiStatusResponse.getRegistrationStatus();
        }

        if (mImeiStatusResponse.getStolenStatus() != null){
            stolenStatus = mImeiStatusResponse.getStolenStatus();
        }

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.imei), imeiNumber));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.imei_compliance_status), status));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.brand), brand));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.model_name), model));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.model_number), model_num));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.manufacturer), manufacturer));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.device_type), deviceType));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.operating_system), operatingSystem));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.radio_access_tech), radioAccessTech));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.reg_status), regStatus));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.stolen_status), stolenStatus));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.block_as_of_date), date));

        mDeviceStatusData.add(new DeviceStatusData(getResources().getString(R.string.per_condition_classification_state), ""));

        if (mImeiStatusResponse.getClassificationState().getBlockingConditions().size() > 0) {

            for (int i = 0; i < mImeiStatusResponse.getClassificationState().getBlockingConditions().size(); i++) {
                mDeviceStatusData.add(new DeviceStatusData(mImeiStatusResponse.getClassificationState().getBlockingConditions().get(i).getConditionName()
                        , String.valueOf(mImeiStatusResponse.getClassificationState().getBlockingConditions().get(i).isConditionMet())));
            }
        } else {
            mDeviceStatusData.add(new DeviceStatusData("", "N/A"));
        }

        if (mImeiStatusResponse.getClassificationState().getInformativeConditions().size() > 0) {
            for (int i = 0; i < mImeiStatusResponse.getClassificationState().getInformativeConditions().size(); i++) {
                mDeviceStatusData.add(new DeviceStatusData(mImeiStatusResponse.getClassificationState().getInformativeConditions().get(i).getConditionName(),
                        String.valueOf(mImeiStatusResponse.getClassificationState().getInformativeConditions().get(i).isConditionMet())));
            }
        } else {
            mDeviceStatusData.add(new DeviceStatusData("", "N/A"));
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
        void onListFragmentInteraction(DeviceStatusData item);
    }
}

/**
 * Copyright (c) 2018 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.dvsauthorized.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.adapters.MyDeviceStatusRecyclerViewAdapter;
import com.qualcomm.dvsauthorized.data.DeviceStatusData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceStatusFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_RESPONSE = "response";
    String response = "";
    List<DeviceStatusData> list;
    ImageButton cancelBtn;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceStatusFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DeviceStatusFragment newInstance(String response) {
        DeviceStatusFragment fragment = new DeviceStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESPONSE, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            response = getArguments().getString(ARG_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devicestatus_list, container, false);

        //populate data
        list = new ArrayList<>();
        parseJson();


        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        cancelBtn = (ImageButton) view.findViewById(R.id.cancelBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyDeviceStatusRecyclerViewAdapter(getActivity(), list, mListener));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
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

    public void parseJson() {
        try {
            JSONObject mJsonObject = new JSONObject(response);
            Log.e("response", response.toString());

            String imeiNumber = "N/A";
            if (mJsonObject.has("imei"))
                if(isEmptyOrNull( mJsonObject.getString("imei")))
                    imeiNumber = mJsonObject.getString("imei");

            list.add(new DeviceStatusData(getResources().getString(R.string.imei), imeiNumber));

            String status = "N/A";
            if (mJsonObject.getJSONObject("compliant") != null && !mJsonObject.isNull("compliant"))
                if (mJsonObject.getJSONObject("compliant").has("status"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("compliant").getString("status")))
                        status = mJsonObject.getJSONObject("compliant").getString("status");

            list.add(new DeviceStatusData(getResources().getString(R.string.imei_compliance_status), status));

            String brand = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("brand"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("brand")))
                        brand = mJsonObject.getJSONObject("gsma").getString("brand");

            list.add(new DeviceStatusData(getResources().getString(R.string.brand), brand));

            String model = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("model_name") && !mJsonObject.get("gsma").equals("null"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("model_name")))
                        model = mJsonObject.getJSONObject("gsma").getString("model_name");

            list.add(new DeviceStatusData(getResources().getString(R.string.model_name), model));

            String model_num = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("model_number") && !mJsonObject.get("gsma").equals("null"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("model_number")))
                        model_num = mJsonObject.getJSONObject("gsma").getString("model_number");

            list.add(new DeviceStatusData(getResources().getString(R.string.model_number), model_num));

            String manufacturer = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("manufacturer") && !mJsonObject.get("gsma").equals("null"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("manufacturer")))
                        manufacturer = mJsonObject.getJSONObject("gsma").getString("manufacturer");

            list.add(new DeviceStatusData(getResources().getString(R.string.manufacturer), manufacturer));

            String deviceType = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("device_type") && !mJsonObject.get("gsma").equals("null"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("device_type")))
                        deviceType = mJsonObject.getJSONObject("gsma").getString("device_type");

            list.add(new DeviceStatusData(getResources().getString(R.string.device_type), deviceType));

            String operatingSystem = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("operating_system") && !mJsonObject.get("gsma").equals("null"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("operating_system")))
                        operatingSystem = mJsonObject.getJSONObject("gsma").getString("operating_system");

            list.add(new DeviceStatusData(getResources().getString(R.string.operating_system), operatingSystem));

            String radioAccessTech = "N/A";
            if (mJsonObject.get("gsma") != null && !mJsonObject.isNull("gsma"))
                if (mJsonObject.getJSONObject("gsma").has("radio_access_technology") && !mJsonObject.get("gsma").equals("null"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("gsma").getString("radio_access_technology")))
                        radioAccessTech = mJsonObject.getJSONObject("gsma").getString("radio_access_technology");

            list.add(new DeviceStatusData(getResources().getString(R.string.radio_access_tech), radioAccessTech));



            String regStatus = "N/A";
            if (mJsonObject.has("registration_status"))
                if(isEmptyOrNull(mJsonObject.getString("registration_status")))
                    regStatus = mJsonObject.getString("registration_status");

            list.add(new DeviceStatusData(getResources().getString(R.string.reg_status), regStatus));

            String stolenStatus = "N/A";
            if (mJsonObject.has("stolen_status"))
                if(isEmptyOrNull(mJsonObject.getString("stolen_status")))
                    stolenStatus = mJsonObject.getString("stolen_status");

            list.add(new DeviceStatusData(getResources().getString(R.string.stolen_status), stolenStatus));


            String date = "N/A";
            if (mJsonObject.getJSONObject("compliant") != null && !mJsonObject.isNull("compliant"))
                if (mJsonObject.getJSONObject("compliant").has("block_date"))
                    if(isEmptyOrNull(mJsonObject.getJSONObject("compliant").getString("block_date")))
                        date = mJsonObject.getJSONObject("compliant").getString("block_date");

            list.add(new DeviceStatusData(getResources().getString(R.string.block_as_of_date), date));

            JSONArray blockingConditions = mJsonObject.getJSONObject("classification_state").getJSONArray("blocking_conditions");
            list.add(new DeviceStatusData(getResources().getString(R.string.per_condition_classification_state), ""));
            if(blockingConditions.length()>0) {


                for (int i = 0; i < blockingConditions.length(); i++) {
                    list.add(new DeviceStatusData(blockingConditions.getJSONObject(i).getString("condition_name"), String.valueOf(blockingConditions.getJSONObject(i).getBoolean("condition_met"))));
                }
            }
            else
            {
                list.add(new DeviceStatusData("", "N/A"));
            }

            JSONArray informativeConditions = mJsonObject.getJSONObject("classification_state").getJSONArray("informative_conditions");
//            list.add(new DeviceStatusData(getResources().getString(R.string.imei_per_informational_condition), ""));
            if(informativeConditions.length()>0) {


                for (int i = 0; i < informativeConditions.length(); i++) {
                    list.add(new DeviceStatusData(informativeConditions.getJSONObject(i).getString("condition_name"), String.valueOf(informativeConditions.getJSONObject(i).getBoolean("condition_met"))));
                }
            }
            else
            {
                list.add(new DeviceStatusData("", "N/A"));
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(DeviceStatusData item);
    }
    public static boolean isEmptyOrNull(String text)
    {
        return text != null && !text.isEmpty() && !text.equals("null");
    }
}

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.adapters.MyPairedImsiRecyclerViewAdapter;
import com.qualcomm.dvsauthorized.data.PairedImsiData;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PairedImsiFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_RSPONSE = "response";
    List<PairedImsiData> list;
    RecyclerView recyclerView;
    ImageButton cancelBtn;
    TextView noRecord;
    // TODO: Customize parameters
    private String response = "";
    private OnListFragmentInteractionListener mListener;
    private MyPairedImsiRecyclerViewAdapter adapter;
    private Button loadMoreBtn;
    private ProgressBar progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PairedImsiFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PairedImsiFragment newInstance(String response) {
        PairedImsiFragment fragment = new PairedImsiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RSPONSE, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            response = getArguments().getString(ARG_RSPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pairedimsi_list, container, false);



        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        cancelBtn = (ImageButton) view.findViewById(R.id.cancelBtn);
        noRecord = (TextView) view.findViewById(R.id.no_record_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        loadMoreBtn = (Button) view.findViewById(R.id.loadMorebtn);

        //parse data
        list = new ArrayList<>();
        list.add(new PairedImsiData("IMSI", "Last Seen"));
        parseJson(false);

        //populate data
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyPairedImsiRecyclerViewAdapter(getActivity(), list, mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);




        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBtn.setEnabled(false);
                loadMoreBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                getMoreItems();
            }
        });



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

    public void parseJson(boolean isRefreah) {
        try {
            JSONObject mJsonObject = new JSONObject(response);
            Log.e("response", response.toString());
            JSONArray pairs = mJsonObject.getJSONObject("pairs").getJSONArray("data");

            Log.e("parid","pairs length "+pairs.length());
                for (int i = 0; i < pairs.length(); i++) {
                    Log.e("parid",pairs.getJSONObject(i).getString("imsi") + pairs.getJSONObject(i).getString("last_seen"));
                    list.add(new PairedImsiData(pairs.getJSONObject(i).getString("imsi"), pairs.getJSONObject(i).getString("last_seen")));
                }

            if (list.size() < 2) {
                Log.e("pairedFrag", "pairs length 0");
                recyclerView.setVisibility(View.GONE);
                noRecord.setVisibility(View.VISIBLE);
            }
            if(pairs.length() < 10){
                    loadMoreBtn.setVisibility(View.GONE);
            }
            if (isRefreah) {
                    adapter.notifyDataSetChanged();

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
        void onListFragmentInteraction(PairedImsiData item);
    }
    public void getMoreItems() {
        String url = "http://192.168.100.74/api/v1/fullstatus";
        Log.e(TAG, url);
        try {
            JSONObject responseJsonObject = new JSONObject(response);
            JSONObject requestObj = new JSONObject();
            requestObj.put("imei", responseJsonObject.getString("imei"));

            JSONObject subscribersObj = new JSONObject();
            subscribersObj.put("limit", 10);
            subscribersObj.put("start", list.size() + 1);
            requestObj.put("subscribers", subscribersObj);


            JSONObject pairsObj = new JSONObject();
            pairsObj.put("limit", 10);
            pairsObj.put("start", list.size() + 1);
            requestObj.put("pairs", pairsObj);


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, requestObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            cancelBtn.setEnabled(true);
                            loadMoreBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            PairedImsiFragment.this.response = response.toString();
                            parseJson(true);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            cancelBtn.setEnabled(true);
                            loadMoreBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            try {
                                if (error != null) {
                                    error.printStackTrace();
                                    if (error.networkResponse != null) {
                                        if (error.networkResponse.statusCode >= 400 && error.networkResponse.statusCode < 500) {
                                            if (error.networkResponse.statusCode == 403) {
                                                String body = new String(error.networkResponse.data, "UTF-8");
                                                Log.e("422", body + "");
                                                JSONObject mJsonObject = new JSONObject(body);
                                                JSONObject errorObj = mJsonObject.getJSONObject("error");
                                                Alerter.create(getActivity())
                                                        .setTitle("Oops...")
                                                        .setText(errorObj.getString("message"))
                                                        .setIcon(R.drawable.ic_error)
                                                        .setBackgroundColorRes(R.color.colorRed)
                                                        .setIconColorFilter(0) // Optional - Removes white tint
                                                        .show();

                                            } else if (error.networkResponse.statusCode == 422) {
                                                String body = new String(error.networkResponse.data, "UTF-8");
                                                Log.e("422", body + "");
                                                JSONObject mJsonObject = new JSONObject(body);
                                                JSONObject errorObj = mJsonObject.getJSONObject("error");
                                                if (mJsonObject.has("error")) {
                                                    if (errorObj.optJSONObject("errors") != null) {

                                                        Iterator<String> iter = errorObj.optJSONObject("errors").keys();
                                                        String errors = "";
                                                        while (iter.hasNext()) {
                                                            String key = iter.next();
                                                            for (int i = 0; i < errorObj.optJSONObject("errors").getJSONArray(key).length(); i++) {

                                                                errors = errors + "- " + errorObj.optJSONObject("errors").getJSONArray(key).getString(0) + "\n";
                                                            }


                                                        }

                                                        Alerter.create(getActivity())
                                                                .setTitle("Oops...")
                                                                .setText(errors)
                                                                .setIcon(R.drawable.ic_error)
                                                                .setBackgroundColorRes(R.color.colorRed)
                                                                .setIconColorFilter(0) // Optional - Removes white tint
                                                                .show();
                                                    }
                                                }

                                            }

                                        } else {
                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                                Alerter.create(getActivity())
                                                        .setTitle("Oops...")
                                                        .setText(getActivity().getString(R.string.error_network_timeout))
                                                        .setIcon(R.drawable.ic_error)
                                                        .setBackgroundColorRes(R.color.colorRed)
                                                        .setIconColorFilter(0) // Optional - Removes white tint
                                                        .show();
                                            } else if (error instanceof AuthFailureError) {


                                                Alerter.create(getActivity())
                                                        .setTitle("Oops...")
                                                        .setText(getActivity().getString(R.string.error_auth_failure))
                                                        .setIcon(R.drawable.ic_error)
                                                        .setBackgroundColorRes(R.color.colorRed)
                                                        .setIconColorFilter(0) // Optional - Removes white tint
                                                        .show();
                                            } else if (error instanceof ServerError) {

                                                Alerter.create(getActivity())
                                                        .setTitle("Oops...")
                                                        .setText(getActivity().getString(R.string.error_server_error))
                                                        .setIcon(R.drawable.ic_error)
                                                        .setBackgroundColorRes(R.color.colorRed)
                                                        .setIconColorFilter(0) // Optional - Removes white tint
                                                        .show();
                                            } else if (error instanceof NetworkError) {

                                                Alerter.create(getActivity())
                                                        .setTitle("Oops...")
                                                        .setText(getActivity().getString(R.string.error_network_error))
                                                        .setIcon(R.drawable.ic_error)
                                                        .setBackgroundColorRes(R.color.colorRed)
                                                        .setIconColorFilter(0) // Optional - Removes white tint
                                                        .show();
                                            } else if (error instanceof ParseError) {

                                                Alerter.create(getActivity())
                                                        .setTitle("Oops...")
                                                        .setText(getActivity().getString(R.string.error_parse_error))
                                                        .setIcon(R.drawable.ic_error)
                                                        .setBackgroundColorRes(R.color.colorRed)
                                                        .setIconColorFilter(0) // Optional - Removes white tint
                                                        .show();
                                            }
                                        }
                                    } else {
                                        Alerter.create(getActivity())
                                                .setTitle("Oops...")
                                                .setText(getActivity().getString(R.string.error_server_error))
                                                .setIcon(R.drawable.ic_error)
                                                .setBackgroundColorRes(R.color.colorRed)
                                                .setIconColorFilter(0) // Optional - Removes white tint
                                                .show();
                                    }
                                } else {
                                    Alerter.create(getActivity())
                                            .setTitle("Oops...")
                                            .setText(getActivity().getString(R.string.error_server_error))
                                            .setIcon(R.drawable.ic_error)
                                            .setBackgroundColorRes(R.color.colorRed)
                                            .setIconColorFilter(0) // Optional - Removes white tint
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//                MyPrefrences myPrefrences = new MyPrefrences(getActivity());
//                params.put("Authorization", "Bearer " + myPrefrences.getString("token",""));
                    return params;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

//                params.put("imei_number", imei);


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

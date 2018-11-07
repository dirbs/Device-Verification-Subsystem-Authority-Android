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
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ResultActivity extends Activity {
//    private List<Cell> mRowHeaderList;
//    private List<Cell> mColumnHeaderList;
//    private List<List<Cell>> mCellList;

    TextView imei, brand, model, model_num, manufacturer, deviceType, operatingSystem, radioAccessTech, regStatus, stolenStatus, status, reason, link, date;
    ImageButton cancelBtn;
    TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        imei = (TextView) findViewById(R.id.imei_txt);
        brand = (TextView) findViewById(R.id.brand_txt);
        model = (TextView) findViewById(R.id.model_name_txt);
        status = (TextView) findViewById(R.id.imei_compliance_status_txt);
        date = (TextView) findViewById(R.id.block_as_of_date_txt);

        model_num = (TextView) findViewById(R.id.model_num_txt);
        manufacturer = (TextView) findViewById(R.id.manufacturer_txt);
        deviceType = (TextView) findViewById(R.id.device_type_txt);
        radioAccessTech = (TextView) findViewById(R.id.radio_access_tech_txt);
        operatingSystem = (TextView) findViewById(R.id.operating_system_txt);

        regStatus = (TextView) findViewById(R.id.reg_status_txt);
        stolenStatus = (TextView) findViewById(R.id.stolen_status_txt);


        cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);


        imei.setText(getIntent().getStringExtra("imei"));
        brand.setText(getIntent().getStringExtra("brand"));
        model.setText(getIntent().getStringExtra("model_name"));

        model_num.setText(getIntent().getStringExtra("model_num"));
        manufacturer.setText(getIntent().getStringExtra("manufacturer"));
        deviceType.setText(getIntent().getStringExtra("device_type"));
        operatingSystem.setText(getIntent().getStringExtra("operating_system"));
        radioAccessTech.setText(getIntent().getStringExtra("radio_access_technology"));

        status.setText(getIntent().getStringExtra("comp_status"));
        regStatus.setText(getIntent().getStringExtra("reg_status"));
        stolenStatus.setText(getIntent().getStringExtra("stolen_status"));
        date.setText(getIntent().getStringExtra("date"));

        String response = getIntent().getStringExtra("response");

        try {
            JSONObject resObj = new JSONObject(response);
            JSONArray blockingConditions = resObj.getJSONObject("classification_state").getJSONArray("blocking_conditions");


            for (int i = 0; i < blockingConditions.length() ; i++) {
                //create row
                TableRow tableRow = new TableRow(this);
                tableRow.setId(1+i);
                tableRow.setBackgroundColor(Color.BLACK);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));


                LinearLayout tableRowLayout = new LinearLayout(this);
                tableRowLayout.setId(100+i);
                tableRowLayout.setBackgroundColor(Color.BLACK);
                tableRowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                TextView label = new TextView(this);
                label.setId(200+i);
                label.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setText(blockingConditions.getJSONObject(i).getString("condition_name"));
                label.setBackgroundColor(getResources().getColor(R.color.colorCellHead));
                label.setTextColor(Color.BLACK);
                TableRow.LayoutParams labelParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT);

                labelParams.setMargins(dpToPixel(this, 1),0,dpToPixel(this, 1),dpToPixel(this, 1));

                label.setLayoutParams(labelParams);

                label.setPadding(dpToPixel(this, 4), dpToPixel(this, 4), dpToPixel(this, 4), dpToPixel(this, 4));
                // add the column to the table row here

                TextView text = new TextView(this);
                text.setId(300+i);
                text.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                text.setText(String.valueOf(blockingConditions.getJSONObject(i).getBoolean("condition_met")));
                text.setBackgroundColor(getResources().getColor(R.color.colorCellValue));
                text.setPadding(dpToPixel(this, 4), dpToPixel(this, 4), dpToPixel(this, 4), dpToPixel(this, 4));
                TableRow.LayoutParams textParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT);

                textParams.setMargins(0, 0,dpToPixel(this, 1),dpToPixel(this, 1));
                label.setLayoutParams(textParams);

                // add the column to the table row here

                tableRow.addView(label);
                tableRow.addView(text);

                //tableRow.addView(tableRowLayout);
                tableLayout.addView(tableRow);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivity.this.finish();
            }
        });
    }

        /*mRowHeaderList = new ArrayList<>();

        mColumnHeaderList = new ArrayList<>();

        mCellList = new ArrayList<>();

//        mRowHeaderList.add(new Cell("IMEI"));
//        mRowHeaderList.add(new Cell("Brand"));
//        mRowHeaderList.add(new Cell("Model Name"));
//        mRowHeaderList.add(new Cell("Model Number"));
//        mRowHeaderList.add(new Cell("Manufacturer"));
//        mRowHeaderList.add(new Cell("Device Type"));
//        mRowHeaderList.add(new Cell("Operating System"));
//
//        mRowHeaderList.add(new Cell("Radio Access Technology"));
//        mRowHeaderList.add(new Cell("IMEI Compliance Status"));
//        mRowHeaderList.add(new Cell("Registration Status"));
//        mRowHeaderList.add(new Cell("Lost/Stolen Status"));
//        mRowHeaderList.add(new Cell("Block as of Date"));

//        List<Cell> imeiT = new ArrayList<Cell>();
//        imeiT.add(new Cell(getResources().getString(R.string.imei)));
//        mCellList.add(imeiT);
//
//        List<Cell> brandT = new ArrayList<Cell>();
//        brandT.add(new Cell(getResources().getString(R.string.brand)));
//        mCellList.add(brandT);
//
//        List<Cell> modelNameT = new ArrayList<Cell>();
//        modelNameT.add(new Cell(getResources().getString(R.string.model_name)));
//        mCellList.add(modelNameT);
//
//        List<Cell> modelNumT = new ArrayList<Cell>();
//        modelNumT.add(new Cell(getResources().getString(R.string.model_number)));
//        mCellList.add(modelNumT);
//
//        List<Cell> manufacturerT = new ArrayList<Cell>();
//        manufacturerT.add(new Cell(getResources().getString(R.string.manufacturer)));
//        mCellList.add(manufacturerT);
//
//        List<Cell> deviceTypeT = new ArrayList<Cell>();
//        deviceTypeT.add(new Cell(getResources().getString(R.string.device_type)));
//        mCellList.add(deviceTypeT);
//
//        List<Cell> operatingSystemT = new ArrayList<Cell>();
//        operatingSystemT.add(new Cell(getResources().getString(R.string.operating_system)));
//        mCellList.add(operatingSystemT);
//
//        List<Cell> radioAccessTechnologyT = new ArrayList<Cell>();
//        radioAccessTechnologyT.add(new Cell(getResources().getString(R.string.radio_access_tech)));
//        mCellList.add(radioAccessTechnologyT);
//
//        List<Cell> compStatusT = new ArrayList<Cell>();
//        compStatusT.add(new Cell(getResources().getString(R.string.imei_compliance_status)));
//        mCellList.add(compStatusT);
//
//        List<Cell> stolenStatusT = new ArrayList<Cell>();
//        stolenStatusT.add(new Cell(getResources().getString(R.string.stolen_status)));
//        mCellList.add(stolenStatusT);
//
//        List<Cell> regStatusT = new ArrayList<Cell>();
//        regStatusT.add(new Cell(getResources().getString(R.string.reg_status)));
//        mCellList.add(regStatusT);
//
//        List<Cell> dateT = new ArrayList<Cell>();
//        dateT.add(new Cell(getResources().getString(R.string.block_as_of_date)));
//        mCellList.add(dateT);


        List<Cell> imei = new ArrayList<Cell>();
        imei.add(new Cell(getResources().getString(R.string.imei)));
        imei.add(new Cell(getIntent().getStringExtra("imei")));
        mCellList.add(imei);

        List<Cell> brand = new ArrayList<Cell>();
        brand.add(new Cell(getResources().getString(R.string.brand)));
        brand.add(new Cell(getIntent().getStringExtra("brand")));
        mCellList.add(brand);

        List<Cell> modelName = new ArrayList<Cell>();
        modelName.add(new Cell(getResources().getString(R.string.model_name)));
        modelName.add(new Cell(getIntent().getStringExtra("model_name")));
        mCellList.add(modelName);

        List<Cell> modelNum = new ArrayList<Cell>();
        modelNum.add(new Cell(getResources().getString(R.string.model_number)));
        modelNum.add(new Cell(getIntent().getStringExtra("model_num")));
        mCellList.add(modelNum);

        List<Cell> manufacturer = new ArrayList<Cell>();
        manufacturer.add(new Cell(getResources().getString(R.string.manufacturer)));
        manufacturer.add(new Cell(getIntent().getStringExtra("manufacturer")));
        mCellList.add(manufacturer);

        List<Cell> deviceType = new ArrayList<Cell>();
        deviceType.add(new Cell(getResources().getString(R.string.device_type)));
        deviceType.add(new Cell(getIntent().getStringExtra("device_type")));
        mCellList.add(deviceType);

        List<Cell> operatingSystem = new ArrayList<Cell>();
        operatingSystem.add(new Cell(getResources().getString(R.string.operating_system)));
        operatingSystem.add(new Cell(getIntent().getStringExtra("operating_system")));
        mCellList.add(operatingSystem);

        List<Cell> radioAccessTechnology = new ArrayList<Cell>();
        radioAccessTechnology.add(new Cell(getResources().getString(R.string.radio_access_tech)));
        radioAccessTechnology.add(new Cell(getIntent().getStringExtra("radio_access_technology")));
        mCellList.add(radioAccessTechnology);

        List<Cell> compStatus = new ArrayList<Cell>();
        compStatus.add(new Cell(getResources().getString(R.string.imei_compliance_status)));
        compStatus.add(new Cell(getIntent().getStringExtra("comp_status")));
        mCellList.add(compStatus);

        List<Cell> stolenStatus = new ArrayList<Cell>();
        stolenStatus.add(new Cell(getResources().getString(R.string.stolen_status)));
        stolenStatus.add(new Cell(getIntent().getStringExtra("stolen_status")));
        mCellList.add(stolenStatus);

        List<Cell> regStatus = new ArrayList<Cell>();
        regStatus.add(new Cell(getResources().getString(R.string.reg_status)));
        regStatus.add(new Cell(getIntent().getStringExtra("reg_status")));
        mCellList.add(regStatus);

        List<Cell> date = new ArrayList<Cell>();
        date.add(new Cell(getResources().getString(R.string.block_as_of_date)));
        date.add(new Cell(getIntent().getStringExtra("date")));
        mCellList.add(date);




        TableView tableView =(TableView) findViewById(R.id.content_container);

//        get screen width
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columnWidth = (int) (dpWidth-32)/2;


        // Create our custom TableView Adapter
        DeviceStatusAdapter adapter = new DeviceStatusAdapter(this);

        // Set this adapter to the our TableView
        tableView.setAdapter(adapter);

        // Let's set datas of the TableView on the Adapter
        adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);

        tableView.hasFixedWidth();
        tableView.setColumnWidth(0, columnWidth);
        tableView.setColumnWidth(1, columnWidth);


    }*/

        public int dpToPixel(Context mContext,int dp){
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    r.getDisplayMetrics()
            );
            return  px;
        }
}

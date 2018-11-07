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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.qualcomm.dvsauthorized.utils.NetworkUtils;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;


public class MainActivity extends Activity {

    TextFieldBoxes imeiTil;
    ExtendedEditText imeiEt;
    Button submitBtn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imeiTil = (TextFieldBoxes) findViewById(R.id.imei_til);
        imeiEt = (ExtendedEditText) findViewById(R.id.imei_et);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(getIntent().hasExtra("imei"))
            imeiEt.setText(getIntent().getStringExtra("imei"));


        if(getIntent().hasExtra("barcode"))
        {
            imeiEt.setText(getIntent().getStringExtra("barcode"));
        }


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        imeiTil.getEndIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);

            }
        });
        imeiEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                final String imei = imeiEt.getText().toString();
                boolean isValid = true;
                if ( imei.length() < 14 || imei.length() > 16 ) {
                    isValid = false;
                    imeiTil.setError(getString(R.string.length_error),true);
                }
                if ( !imei.matches("^(?=.[a-fA-F]*)(?=.[0-9]*)[a-fA-F0-9]+$")) {
                    isValid = false;
                    imeiTil.setError(getString(R.string.hexa_error), true);
                }

                if(isValid)
                    imeiTil.removeError();

            }
        });
    }

    private void validate() {
        final String imei = imeiEt.getText().toString();
        if ( imei.length() >= 14 && imei.length() <= 16 ) {
            if ( imei.matches("^(?=.[a-fA-F]*)(?=.[0-9]*)[a-fA-F0-9]+$")) {
                imeiTil.removeError();
                NetworkUtils.getImeiStatus(imei,null, progressBar, submitBtn, null,MainActivity.this);
            } else {
                imeiTil.setError(getString(R.string.hexa_error),true);
            }
        } else {
            imeiTil.setError(getString(R.string.length_error), true);
        }

    }



}

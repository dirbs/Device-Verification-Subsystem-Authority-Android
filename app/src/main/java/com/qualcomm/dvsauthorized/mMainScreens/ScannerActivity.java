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

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.qualcomm.dvsauthorized.R;

import java.util.List;

// View class for scanner activity
public class ScannerActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private ImageButton mFlashBtn;
    private boolean mIsFlash = false;
    private DecoratedBarcodeView mBarcodeScannerView;
    private CaptureManager mCapture;

    // Invoked upon opening of scanner class
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Programmatically initialize the scanner view
        setContentView(R.layout.activity_scanner);                // Set the scanner view as the content view
        mFlashBtn = findViewById(R.id.flashBtn);
        ImageButton autoFocusBtn = findViewById(R.id.auto_focus_btn);
        mBarcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        mBarcodeScannerView.getBarcodeView().getCameraSettings().setAutoFocusEnabled(true);
        mCapture = new CaptureManager(this, mBarcodeScannerView);
        mCapture.initializeFromIntent(getIntent(), state);
        mCapture.decode();

        mBarcodeScannerView.decodeContinuous(callback);
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            mFlashBtn.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.scan_barcode));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));

        mFlashBtn.setOnClickListener(v -> {
            mIsFlash = !mIsFlash;
            if (mIsFlash) {
                mBarcodeScannerView.setTorchOn();
                mFlashBtn.setImageResource(R.drawable.ic_action_flash_on);
            } else {
                mBarcodeScannerView.setTorchOff();
                mFlashBtn.setImageResource(R.drawable.ic_action_flash_off);
            }
        });

        checkCameraPermission();

    }

    private void checkCameraPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(ScannerActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                }
            } else {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(ScannerActivity.this, Manifest.permission.CAMERA)) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScannerActivity.this, R.style.AlertDialogTheme);
                    alertDialogBuilder.setTitle(ScannerActivity.this.getString(R.string.permission_access));
                    alertDialogBuilder.setMessage(ScannerActivity.this.getString(R.string.camera_permission_needed));

                    alertDialogBuilder.setPositiveButton(ScannerActivity.this.getString(R.string.ok), (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", ScannerActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        ScannerActivity.this.startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);
                    });

                    alertDialogBuilder.setNegativeButton(ScannerActivity.this.getString(R.string.cancel), (dialog, which) -> {
                        Toast.makeText(ScannerActivity.this, ScannerActivity.this.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlack));

                } else {
                    Toast.makeText(ScannerActivity.this, ScannerActivity.this.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result codes
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            recreate();
        }

        // call super method to ensure unhandled result codes are handled
        super.onActivityResult(requestCode, resultCode, data);

    }

    //call back to capture scan result
    public BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            String imei = result.getText();
            Intent intent = new Intent(ScannerActivity.this, MainTabActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("imei", imei);
            startActivity(intent);
            finish();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    // Invoked upon clicking nav up button in app bar for scanner activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCapture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCapture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCapture.onDestroy();
    }
}
/**
 * Copyright (c) 2018 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.dvsauthorized.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyPrefrences {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public MyPrefrences(Context context)
    {
        Log.e("package","package name = "+context.getPackageName());
        preferences=context.getSharedPreferences(context.getPackageName(),0);
        editor=preferences.edit();
    }
    /**
     * put int preference
     *
     * @param prefName
     * @param val
     *
     */
    public void setInt(String prefName,int val) {
        editor.putInt(prefName,val).commit();
    }
    /**
     * put String preference
     *
     * @param prefName
     * @param val
     *
     */
    public void setString(String prefName,String val) {

        Log.e("setstring","Str name = "+prefName+" Str val= "+val);
        editor.putString(prefName, val).commit();
    }
    /**
     * put float preference
     *
     * @param prefName
     * @param val
     *
     */
    public void setFloat(String prefName,float val) {
        editor.putFloat(prefName, val).commit();
    }
    /**
     * put boolean preference
     *
     * @param prefName
     * @param val
     *
     */
    public void setBoolean(String prefName,boolean val) {
        editor.putBoolean(prefName, val).commit();
    }
    /**
     * put long preference
     *
     * @param prefName
     * @param val
     *
     */
    public void setLong(String prefName,long val) {
        editor.putLong(prefName, val).commit();
    }

    /**
     * get int preference
     *
     * @param prefName
     * @param valDef
     *
     */
    public int getInt(String prefName,int valDef) {
       return preferences.getInt(prefName,valDef);
    }
    /**
     * put String preference
     *  @param prefName
     * @param valDef
     *
     */
    public String getString(String prefName, String valDef) {
        return preferences.getString(prefName, valDef);
    }
    /**
     * put float preference
     *  @param prefName
     * @param valDef
     *
     */
    public float getFloat(String prefName, float valDef) {
        return preferences.getFloat(prefName, valDef);
    }
    /**
     * put boolean preference
     *  @param prefName
     * @param valDef
     *
     */
    public boolean getBoolean(String prefName, boolean valDef) {
        return preferences.getBoolean(prefName, valDef);
    }
    /**
     * put long preference
     *  @param prefName
     * @param valDef
     *
     */
    public long getLong(String prefName, long valDef) {
        return  preferences.getLong(prefName, valDef);
    }


}

/**
 * Copyright (c) 2018 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.dvsauthorized.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qualcomm.dvsauthorized.fragments.PairedImsiFragment.OnListFragmentInteractionListener;
import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.data.PairedImsiData;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PairedImsiData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPairedImsiRecyclerViewAdapter extends RecyclerView.Adapter<MyPairedImsiRecyclerViewAdapter.ViewHolder> {

    private final List<PairedImsiData> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public MyPairedImsiRecyclerViewAdapter(Context context,List<PairedImsiData> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pairedimsi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.setIsRecyclable(false);
        holder.mLabelView.setText(mValues.get(position).getImsi());
        holder.mTextView.setText(mValues.get(position).getLastSeen());

        //change heading bg color
        if(position == 0){

            float px1 = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1.5f,
                    mContext.getResources().getDisplayMetrics()
            );
            Math.ceil(px1);

            holder.mLabelView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mLabelView.setTextColor(Color.parseColor("#000000"));

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.mLabelView.getLayoutParams();

            params.topMargin = (int)px1;

            holder.mTextView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mTextView.setTextColor(Color.parseColor("#000000"));

            ViewGroup.MarginLayoutParams paramsText = (ViewGroup.MarginLayoutParams) holder.mTextView.getLayoutParams();

            paramsText.topMargin = (int)px1;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLabelView;
        public final TextView mTextView;
        public PairedImsiData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLabelView = (TextView) view.findViewById(R.id.label);
            mTextView = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }
}

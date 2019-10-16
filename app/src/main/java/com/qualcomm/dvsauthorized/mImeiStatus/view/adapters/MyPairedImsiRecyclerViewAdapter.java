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

package com.qualcomm.dvsauthorized.mImeiStatus.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualcomm.dvsauthorized.R;
import com.qualcomm.dvsauthorized.databinding.FragmentPairedimsiBinding;
import com.qualcomm.dvsauthorized.mImeiStatus.view.fragments.PairedImsiFragment.OnListFragmentInteractionListener;
import com.qualcomm.dvsauthorized.mModels.imeiData.PairedImsiData;

import java.util.List;

// Custom adapter to set data into paired subscribers fragment list.
/**
 * {@link RecyclerView.Adapter} that can display a {@link PairedImsiData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPairedImsiRecyclerViewAdapter extends RecyclerView.Adapter<MyPairedImsiRecyclerViewAdapter.ViewHolder> {

    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private List<PairedImsiData> mPairedImsiDataList;

    // Constructor for passing activity/fragment context and list which needs to be populated.
    public MyPairedImsiRecyclerViewAdapter(Context context, List<PairedImsiData> list) {
        this.mContext = context;
        this.mPairedImsiDataList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FragmentPairedimsiBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_pairedimsi,
                        parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mPairedImsiDataList.get(position);
        holder.setIsRecyclable(false);
        holder.mBinding.setPaired(mPairedImsiDataList.get(position));

        //change heading bg color
        if (position == 0) {

            float px1 = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1.5f,
                    mContext.getResources().getDisplayMetrics()
            );
            px1 = (float) Math.ceil(px1);

            holder.mLabelView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mLabelView.setTextColor(Color.parseColor("#000000"));

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.mLabelView.getLayoutParams();
            params.topMargin = (int) px1;

            holder.mTextView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mTextView.setTextColor(Color.parseColor("#000000"));

            ViewGroup.MarginLayoutParams paramsText = (ViewGroup.MarginLayoutParams) holder.mTextView.getLayoutParams();
            paramsText.topMargin = (int) px1;
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPairedImsiDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final FragmentPairedimsiBinding mBinding;
        final TextView mLabelView;
        final TextView mTextView;
        PairedImsiData mItem;
        final View mView;

        ViewHolder(FragmentPairedimsiBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            mView = binding.getRoot();
            mLabelView = mBinding.label;
            mTextView = mBinding.text;
        }
    }
}

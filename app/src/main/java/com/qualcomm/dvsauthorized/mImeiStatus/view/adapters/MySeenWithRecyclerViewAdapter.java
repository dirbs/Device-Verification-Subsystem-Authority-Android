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
import com.qualcomm.dvsauthorized.databinding.FragmentSeenwithBinding;
import com.qualcomm.dvsauthorized.mImeiStatus.view.fragments.SeenWithFragment.OnListFragmentInteractionListener;
import com.qualcomm.dvsauthorized.mModels.imeiData.SeenWithData;

import java.util.List;

// Custom adapter to set data into subscribers seen with fragment list.
/**
 * {@link RecyclerView.Adapter} that can display a {@link SeenWithData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySeenWithRecyclerViewAdapter extends RecyclerView.Adapter<MySeenWithRecyclerViewAdapter.ViewHolder> {

    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private List<SeenWithData> mSeenWithDataList;

    // Constructor for passing activity/fragment context and list which needs to be populated.
    public MySeenWithRecyclerViewAdapter(Context context, List<SeenWithData> list) {
        this.mSeenWithDataList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FragmentSeenwithBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_seenwith,
                        parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mSeenWithDataList.get(position);
        holder.setIsRecyclable(false);
        holder.binding.setSeen(mSeenWithDataList.get(position));

        //change heading bg color
        if (position == 0) {

            float px1 = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1.5f,
                    mContext.getResources().getDisplayMetrics()
            );
            px1 = (float) Math.ceil(px1);

            holder.mImsiView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mImsiView.setTextColor(Color.parseColor("#000000"));

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.mImsiView.getLayoutParams();
            params.topMargin = (int) px1;

            holder.mMsisdnView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mMsisdnView.setTextColor(Color.parseColor("#000000"));

            ViewGroup.MarginLayoutParams paramsMsisdn = (ViewGroup.MarginLayoutParams) holder.mMsisdnView.getLayoutParams();
            paramsMsisdn.topMargin = (int) px1;
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
        return mSeenWithDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final FragmentSeenwithBinding binding;
        final TextView mImsiView;
        final TextView mMsisdnView;
        SeenWithData mItem;
        final View mView;

        ViewHolder(FragmentSeenwithBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            mImsiView = binding.imsi;
            mView = binding.getRoot();
            mMsisdnView = binding.msisdn;
        }
    }
}

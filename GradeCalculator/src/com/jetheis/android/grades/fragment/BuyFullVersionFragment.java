/*
 * Copyright (C) 2012 Jimmy Theis. Licensed under the MIT License:
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jetheis.android.grades.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.jetheis.android.grades.Constants;
import com.jetheis.android.grades.Constants.LicenseType;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.billing.BillingWrapper;
import com.jetheis.android.grades.billing.BillingWrapper.OnBillingReadyListener;
import com.jetheis.android.grades.billing.FreeBillingWrapper;

public class BuyFullVersionFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.buy_full_version_fragment, container, false);

        ((Button) result.findViewById(R.id.buy_full_version_fragment_buy_button))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final BillingWrapper billingWrapper;
                        final String itemId;

                        if (Constants.LICENSE_TYPE == LicenseType.FREE) {
                            billingWrapper = FreeBillingWrapper.isInstanceInitialized() ? FreeBillingWrapper
                                    .getInstance() : FreeBillingWrapper
                                    .initializeIntance(getActivity());
                            itemId = Constants.FREE_ITEM_ID;
                        } else if (Constants.LICENSE_TYPE == LicenseType.GOOGLE_PLAY) {
                            billingWrapper = FreeBillingWrapper.isInstanceInitialized() ? FreeBillingWrapper
                                    .getInstance() : FreeBillingWrapper
                                    .initializeIntance(getActivity());

                            itemId = Constants.FREE_ITEM_ID;
                        } else { // Amazon
                            billingWrapper = FreeBillingWrapper.isInstanceInitialized() ? FreeBillingWrapper
                                    .getInstance() : FreeBillingWrapper
                                    .initializeIntance(getActivity());

                            itemId = Constants.FREE_ITEM_ID;
                        }

                        billingWrapper.registerOnBillingReadyListener(new OnBillingReadyListener() {

                            @Override
                            public void onBillingReady() {
                                billingWrapper.requestPurchase(itemId);
                            }

                            @Override
                            public void onBillingNotSupported() {
                                // Don't care
                            }
                        });
                    }
                });

        return result;
    }

}

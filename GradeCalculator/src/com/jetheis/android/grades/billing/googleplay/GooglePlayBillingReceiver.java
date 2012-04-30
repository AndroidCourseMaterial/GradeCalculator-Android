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

package com.jetheis.android.grades.billing.googleplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.jetheis.android.grades.Constants;
import com.jetheis.android.grades.billing.googleplay.GooglePlayBillingConstants.GooglePlayResponseCode;

public class GooglePlayBillingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (!GooglePlayBillingWrapper.isInstanceInitialized()) {
            GooglePlayBillingWrapper.initializeInstance(context);
        }

        Log.v(Constants.TAG, "Received Google Play intent: " + action);

        // RESPONSE_CODE
        if (action.equals(GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_ACTION_RESPONSE_CODE)) {
            int responseCode = intent.getIntExtra(
                    GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_KEY_RESPONSE_CODE, -1);
            long requestId = intent.getLongExtra(
                    GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_KEY_REQUEST_ID, -1);

            if (responseCode == GooglePlayResponseCode.RESULT_OK.ordinal()) {
                Log.v(Constants.TAG, "Google Play RESPONSE_CODE (request " + requestId + "): "
                        + GooglePlayResponseCode.fromInt(responseCode));
            } else {
                Log.e(Constants.TAG, "Google Play RESPONSE_CODE (request " + requestId + "): "
                        + GooglePlayResponseCode.fromInt(responseCode));

                if (responseCode == GooglePlayResponseCode.RESULT_SERVICE_UNAVAILABLE.ordinal()) {
                    Toast.makeText(
                            context,
                            "Google Play failed to provide transaction data. This may cause a VIP status order not to be loaded.",
                            Toast.LENGTH_LONG).show();
                }

                return;
            }

            // IN_APP_NOTIFY
        } else if (action
                .equals(GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_ACTION_IN_APP_NOTIFY)) {
            String notifyId = intent
                    .getStringExtra(GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_KEY_NOTIFICATION_ID);

            Log.v(Constants.TAG, "Requesting purchase state for IN_APP_NOTIFY notification: "
                    + notifyId);

            GooglePlayBillingWrapper.getInstance().requestPurchaseInfo(new String[] { notifyId });

            // PURCHASE_STATE_CHANGED
        } else if (action
                .equals(GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_ACTION_PURCHASE_STATE_CHANGED)) {
            String signedData = intent
                    .getStringExtra(GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_KEY_SIGNED_DATA);
            String signature = intent
                    .getStringExtra(GooglePlayBillingConstants.GOOGLE_PLAY_INTENT_KEY_SIGNATURE);

            GooglePlayBillingWrapper.getInstance().handleJsonResponse(signedData, signature);

            // Unknown
        } else {
            Log.e(Constants.TAG, "Unexpected response from Google Play: " + action);
        }
    }

}

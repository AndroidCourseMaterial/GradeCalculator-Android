package com.jetheis.android.grades.billing.googleplay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;

import com.jetheis.android.grades.Constants;
import com.jetheis.android.grades.billing.BillingWrapper;
import com.jetheis.android.grades.billing.Security;
import com.jetheis.android.grades.billing.googleplay.GooglePlayBillingConstants.GooglePlayPurchaseState;
import com.jetheis.android.grades.billing.googleplay.GooglePlayBillingConstants.GooglePlayResponseCode;
import com.jetheis.android.grades.billing.googleplay.GooglePlayBillingService.OnGooglePlayBillingSupportResultListener;

public class GooglePlayBillingWrapper implements BillingWrapper {

    private static GooglePlayBillingWrapper sInstance;

    private Context mContext;
    private ServiceConnection mConnection;
    private Collection<OnBillingReadyListener> mOnReadyListeners;
    private Collection<OnPurchaseStateChangedListener> mOnPurchaseStateChangedListeners;
    private GooglePlayBillingService mBoundService;

    public static GooglePlayBillingWrapper initializeIntance(Context context) {
        sInstance = new GooglePlayBillingWrapper(context);

        return sInstance;
    }

    public static GooglePlayBillingWrapper getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Billing wrapper has not been initialized");
        }

        return sInstance;
    }

    public static boolean isInstanceInitialized() {
        return sInstance != null;
    }

    public GooglePlayBillingWrapper(Context context) {
        mContext = context;
        mOnReadyListeners = new HashSet<OnBillingReadyListener>();
        mOnPurchaseStateChangedListeners = new HashSet<OnPurchaseStateChangedListener>();

        mContext.startService(new Intent(mContext, GooglePlayBillingService.class));

        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBoundService = ((GooglePlayBillingService.GooglePlayBillingBinder) service)
                        .getService();
                mBoundService
                        .checkIsBillingSupported(new OnGooglePlayBillingSupportResultListener() {

                            @Override
                            public void onGooglePlayBillingSupportResultFound(
                                    boolean billingSupported) {
                                if (billingSupported) {
                                    Log.i(Constants.TAG, "Google Play billing ready");

                                    for (OnBillingReadyListener listener : mOnReadyListeners) {
                                        listener.onBillingReady();
                                    }

                                } else {
                                    Log.i(Constants.TAG, "Google Play billing is not supported");

                                    for (OnBillingReadyListener listener : mOnReadyListeners) {
                                        listener.onBillingNotSupported();
                                    }

                                }
                            }

                        });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBoundService = null;
            }
        };

        mContext.bindService(new Intent(context, GooglePlayBillingService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void requestPurchase(String itemId) {
        Bundle response;

        try {
            response = mBoundService.makeGooglePlayPurchaseRequest(itemId);
        } catch (RemoteException e) {
            Log.e(Constants.TAG, "RemoteException: " + e.getLocalizedMessage());
            return;
        }

        if (response.getInt(GooglePlayBillingConstants.GOOGLE_PLAY_BUNDLE_KEY_RESPONSE_CODE) != GooglePlayResponseCode.RESULT_OK
                .ordinal()) {
            return;
        }

        PendingIntent pendingIntent = response
                .getParcelable(GooglePlayBillingConstants.GOOGLE_PLAY_BUNDLE_KEY_PURCHASE_INTENT);

        try {
            mContext.startIntentSender(pendingIntent.getIntentSender(), new Intent(), 0, 0, 0);
        } catch (SendIntentException e) {
            Log.e(Constants.TAG, "SendIntentException: " + e.getLocalizedMessage());
        }

    }

    @Override
    public void restorePurchases() {
        try {
            mBoundService.makeGooglePlayRestoreTransactionsRequest();
        } catch (RemoteException e) {
            Log.e(Constants.TAG, "RemoteException: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void registerOnBillingReadyListener(OnBillingReadyListener onBillingReadyListener) {
        mOnReadyListeners.add(onBillingReadyListener);
    }

    @Override
    public int clearOnBillingReadyListeners() {
        int result = mOnReadyListeners.size();
        mOnReadyListeners.clear();

        return result;
    }

    @Override
    public void registerOnPurchaseStateChangedListener(
            OnPurchaseStateChangedListener onPurchaseStateChangedListener) {
        mOnPurchaseStateChangedListeners.add(onPurchaseStateChangedListener);
    }

    @Override
    public int clearOnPurchaseStateChangedListeners() {
        int result = mOnPurchaseStateChangedListeners.size();
        mOnPurchaseStateChangedListeners.clear();

        return result;
    }

    public void requestPurchaseInfo(String[] notifyIds) {
        try {
            mBoundService.makeGooglePlayPurchaseInformationRequest(notifyIds);
        } catch (RemoteException e) {
            Log.e(Constants.TAG, "RemoteException: " + e.getLocalizedMessage());
            return;
        }
    }

    public void sendNotificationConformation(String[] notificationIds) {
        Log.v(Constants.TAG, "Confirming " + notificationIds.length + " notification(s)");

        try {
            mBoundService.makeGooglePlayConfirmNotificationsRequest(notificationIds);
        } catch (RemoteException e) {
            Log.e(Constants.TAG, "RemoteException: " + e.getLocalizedMessage());
        }
    }

    public void unbind() {
        if (mConnection != null) {
            mContext.unbindService(mConnection);
            mConnection = null;
        }
    }

    public void handleJsonResponse(String response, String signature) {
        Log.v(Constants.TAG, "Handling JSON response: " + response);

        if (!Security.isCorrectSignature(response, signature)) {
            Log.e(Constants.TAG, "Bad Google Play signature! Possible security breach!");
            return;
        }

        JSONObject responseJson;

        try {
            responseJson = new JSONObject(response);

            long nonce = responseJson
                    .getLong(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_NONCE);

            if (!Security.isNonceKnown(nonce)) {
                Log.e(Constants.TAG, "Bad Google Play nonce! Possible security breach!");
                return;
            }

            Log.v(Constants.TAG, "Signature and nonce OK");

            JSONArray orders = responseJson
                    .getJSONArray(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_ORDERS);

            if (orders.length() == 0) {
                Log.v(Constants.TAG, "No orders present in response");
                return;
            }

            List<String> notificationIds = new ArrayList<String>(orders.length());

            for (int i = 0; i < orders.length(); i++) {
                JSONObject order = orders.getJSONObject(i);

                String packageName = order
                        .getString(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_PACKAGE_NAME);
                if (!packageName.equals(mContext.getPackageName())) {
                    Log.e(Constants.TAG, "Bad Google Play package name! Possible security breach!");
                    return;
                }

                Log.v(Constants.TAG, "Package name OK");

                if (order.has(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_NOTIFICATION_ID)) {
                    notificationIds
                            .add(order
                                    .getString(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_NOTIFICATION_ID));

                }

                String productId = order
                        .getString(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_PRODUCT_ID);

                Date purchaseDate = new Date(
                        order.getLong(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_PURCHASE_TIME));
                GooglePlayPurchaseState purchaseState = GooglePlayPurchaseState.fromInt(order
                        .getInt(GooglePlayBillingConstants.GOOGLE_PLAY_JSON_KEY_PURCHASE_STATE));

                if (purchaseState == GooglePlayPurchaseState.PURCHASED) {

                    Log.i(Constants.TAG, "Found record of purchase of " + productId + " from "
                            + DateFormat.getLongDateFormat(mContext).format(purchaseDate));

                    for (OnPurchaseStateChangedListener listener : mOnPurchaseStateChangedListeners) {
                        listener.onPurchaseSuccessful(productId);
                    }

                } else if (purchaseState == GooglePlayPurchaseState.CANCELLED) {
                    Log.i(Constants.TAG, "User cancelled purchase");
                    
                    for (OnPurchaseStateChangedListener listener : mOnPurchaseStateChangedListeners) {
                        listener.onPurchaseCancelled(productId);
                    }
                    
                } else {
                    Log.e(Constants.TAG, "Google Play purchase refunded");
                    
                    for (OnPurchaseStateChangedListener listener : mOnPurchaseStateChangedListeners) {
                        listener.onPurchaseReturend(productId);
                    }
                }
            }

            if (notificationIds.size() > 0) {
                sendNotificationConformation(notificationIds.toArray(new String[notificationIds
                        .size()]));
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "JSONException: " + e.getLocalizedMessage());
        }
    }

}

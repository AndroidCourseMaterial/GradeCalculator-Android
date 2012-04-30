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

package com.jetheis.android.grades.billing;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;

public class FreeBillingWrapper implements BillingWrapper {
    
    private static FreeBillingWrapper sInstance;
    
    private Context mContext;
    private Collection<OnBillingReadyListener> mOnReadyListeners;
    private Collection<OnPurchaseStateChangedListener> mOnPurchaseStateChangedListeners;
    
    public static FreeBillingWrapper initializeInstance(Context context) {
        sInstance = new FreeBillingWrapper(context);
        
        return sInstance;
    }
    
    public static FreeBillingWrapper getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Billing wrapper has not been initialized");
        }
        
        return sInstance;
    }
    
    public static boolean isInstanceInitialized() {
        return sInstance != null;
    }
    
    public FreeBillingWrapper(Context context) {
        mContext = context;
        mOnReadyListeners = new HashSet<OnBillingReadyListener>();
        mOnPurchaseStateChangedListeners = new HashSet<OnPurchaseStateChangedListener>();
    }

    @Override
    public void requestPurchase(String itemId) {
        Security.setFullVersionUnlocked(true, mContext);
        
        for (OnPurchaseStateChangedListener listener : mOnPurchaseStateChangedListeners) {
            listener.onPurchaseSuccessful(itemId);
        }
    }

    @Override
    public void restorePurchases() {
//        for (OnPurchaseStateChangedListener listener : mOnPurchaseStateChangedListeners) {
//            Log.d(Constants.TAG, "Restoring purchases");
//            listener.onPurchaseReturend(Constants.FREE_ITEM_ID);
//        }
    }

    @Override
    public void registerOnBillingReadyListener(OnBillingReadyListener onBillingReadyListener) {
        mOnReadyListeners.add(onBillingReadyListener);
        
        onBillingReadyListener.onBillingReady();
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

}

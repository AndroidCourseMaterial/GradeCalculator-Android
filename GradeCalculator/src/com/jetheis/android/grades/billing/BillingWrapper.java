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

/**
 * An interface for "billing wrappers" to conform to. This functionality aims to
 * be generic enough for most billing services to conform to, while still being
 * generic enough to be called from activity code.
 */
public interface BillingWrapper {

    /**
     * A listener for the state of a particular billing service. Implementing
     * this listener allows an activity (or other component) to be notified when
     * the state of billing availability is determined.
     */
    public interface OnBillingReadyListener {

        /**
         * The billing service associated with this listener has been connected
         * to and is ready to receive communication.
         */
        public void onBillingReady();

        /**
         * For one reason or another, the billing service associated with this
         * listener is not supported.
         */
        public void onBillingNotSupported();
    }

    /**
     * A listener for changes in "purchase state" of bought items. Implementing
     * this listener allows an activity (or other component) to be notified when
     * an item is purchased, the purchase of an item is cancelled, or an item is
     * returned or refunded.
     */
    public interface OnPurchaseStateChangedListener {

        /**
         * An item has been successfully purchased and should be made available
         * in the app.
         * 
         * @param item
         *            The billing service's {@link String} identifier for the
         *            item purchased.
         */
        public void onPurchaseSuccessful(String itemId);

        /**
         * The purchase of an item has been cancelled. This is a minor event and
         * likely does not require any notification of the user, but it can be
         * useful to restore an app to an "unpurchased" state.
         * 
         * @param itemId
         *            The billing service's {@link String} identifier for the
         *            item purchased.
         */
        public void onPurchaseCancelled(String itemId);

        /**
         * An item has been refunded or returned. The app should remove this
         * item from the user's possession and make it available for purchase
         * again, if applicable.
         * 
         * @param itemId
         *            The billing service's {@link String} identifier for the
         *            item purchased.
         */
        public void onPurchaseReturend(String itemId);
    }

    /**
     * Begin the process of purchasing an item. This will likely cause the user
     * to be presented with an activity prompting him or her to confirm or
     * cancel the purchase.
     * 
     * @param itemId
     *            The billing service's {@link String} identifier for the item
     *            purchased.
     */
    public void requestPurchase(String itemId);

    /**
     * Restore any previous purchases of items made for this user's account. Any
     * purchases found will be sent to this billing wrapper's
     * {@link OnPurchaseStateChangedListener}s.
     */
    public void restorePurchases();

    /**
     * Register a listener for when this billing service is determined to be
     * available or unsupported. If the result of this has already been
     * determined, the listener's relevant method
     * {@link OnBillingReadyListener#onBillingReady()} or
     * {@link OnBillingReadyListener#onBillingNotSupported()}) will be called
     * immediately.
     * 
     * @param onBillingReadyListener
     *            The listener to be added to this {@link BillingWrapper}'s set
     *            of billing readiness listeners.
     */
    public void registerOnBillingReadyListener(OnBillingReadyListener onBillingReadyListener);

    /**
     * Remove all listeners for billing readiness status from this billing
     * wrapper. This is the only way to remove listeners other than destroying
     * the billing wrapper's instance.
     * 
     * @return The number of {@link OnBillingReadyListener}s removed.
     */
    public int clearOnBillingReadyListeners();

    /**
     * Register a listener for changes or updates in purchase state of in-app
     * purchaseable items.
     * 
     * @param onPurchaseStateChangedListener
     *            The {@link OnPurchaseStateChangedListener} to be added to this
     *            billing wrapper's listener set.
     */
    public void registerOnPurchaseStateChangedListener(
            OnPurchaseStateChangedListener onPurchaseStateChangedListener);

    /**
     * Remove all listeners for item purchase state changes from this billing
     * wrapper. This is the only way to remove listeners other than destroying
     * the billing wrapper's instance.
     * 
     * @return The number of {@link OnPurchaseStateChangedListener}s removed.
     */
    public int clearOnPurchaseStateChangedListeners();
}

package org.cafemember.messenger.mytg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.cafemember.messenger.mytg.listeners.OnJoinSuccess;
import org.cafemember.messenger.mytg.util.paytool.IabHelper;
import org.cafemember.messenger.mytg.util.paytool.IabResult;
import org.cafemember.messenger.mytg.util.paytool.Inventory;
import org.cafemember.messenger.mytg.util.paytool.Purchase;

/**
 * Created by Masoud on 3/9/2015.
 */
public class PaymentActivity extends Activity {

    // Debug tag, for logging
    static final String TAG = "PAYMETN";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_PREMIUM = "sign_locker";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 1234;

    private Button payBtn;
    // The helper object
    IabHelper mHelper;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.i(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.i(TAG, "Failed to query inventory: " + result);
                return;
            } else {
                Log.i(TAG, "Query inventory was successful.");
                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_PREMIUM);

                // update UI accordingly

                Log.i(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            }

            Log.i(TAG, "Initial inventory query finished; enabling main UI.");

            payBtn.setEnabled(true);
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Log.i(TAG, "Error purchasing: " + result);
                Toast.makeText(PaymentActivity.this,"عملیات پرداخت با مشکل مواجه شد",Toast.LENGTH_SHORT).show();
                //paymentState.setText("پرداخت انجام نشد");
                finish();
                return;
            } else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // give user access to premium content and update the UI
                //paymentState.setText("پرداخت با موفقیت انجام شد");
                //paymentState.setTextColor(Color.GREEN);
                Toast.makeText(PaymentActivity.this,"پرداخت با موفقیت انجام شد",Toast.LENGTH_SHORT).show();
                grantUser(purchase.getSku(),purchase);
            }
        }
    };
    private String MARKET_KEY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MARKET_KEY = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDAD+mkd/W+9SymV0KtE7tayd5oGcLZRwylQJLXI0EuwJnvfChT58XJJmOitquaUFIXnmO84spLRsVvbeuxM4nUsTihEWxNOFDD/h1MW4lT5wEkXR7dNb6Z1WrA1uRImNoqtD817L4wwaDC5F8JixczBsIplalFqWX3oq0fCAo1q9GJ+1FQfmO39GkJbKH9T/ZzsI3kFPNVt+VacLwSsa1iqO3ppdPik4PjOQmGZMcCAwEAAQ==";

            mHelper = new IabHelper(this, MARKET_KEY);
        try {
            Log.i(TAG, "Starting setup.");
            IabHelper.OnIabSetupFinishedListener lis =null;
            /*try {
                lis = new IabHelper.OnIabSetupFinishedListener() {
                    public void onIabSetupFinished(IabResult result) {
                        try {
                            Log.i(TAG, "Setup finished.");

                            if (!result.isSuccess()) {
                                // Oh noes, there was a problem.
                                Log.i(TAG, "Problem setting up In-app Billing: " + result);
                            }
                            if(result == null){
                                Log.i(TAG,"Failed");
                            }
                            // Hooray, IAB is fully set up!
                            mHelper.queryInventoryAsync(mGotInventoryListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            }catch (Exception e){
                e.printStackTrace();
            }*/
//            mHelper.enableDebugLogging(true);
//           mHelper.startSetup(lis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHelper.launchPurchaseFlow(PaymentActivity.this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "payload-string");



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            finish();
        } else {
            Log.i(TAG, "onActivityResult handled by IABUtil.");
        }
    }


    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            if (mHelper != null) mHelper.dispose();
            mHelper = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void grantUser(String sku, final Purchase purchase) {

        Commands.checkBoughtItem(sku,purchase.getOriginalJson(), new OnJoinSuccess() {
            @Override
            public void OnResponse(boolean ok) {
                if (ok) {
                    mHelper.consumeAsync(purchase
                    , new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            if(result.isSuccess()){
                                Toast.makeText(PaymentActivity.this,"خرید انجام شد",Toast.LENGTH_SHORT).show();                            }
                            else {
                                Toast.makeText(PaymentActivity.this,"خرید انجام شد ولی بازار نفهمید!",Toast.LENGTH_SHORT).show();
                            }
                            finish();

                        }
                    });

                }
                else {
                    {
                        Toast.makeText(PaymentActivity.this,"ارتباط با سرور برقرار نشد, دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                        finish();}
                }

            }
        });

    }
}
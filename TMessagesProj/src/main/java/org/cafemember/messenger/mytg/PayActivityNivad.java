package org.cafemember.messenger.mytg;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.cafemember.messenger.mytg.listeners.OnJoinSuccess;
import org.cafemember.messenger.mytg.util.paytool.IabHelper;
import org.cafemember.messenger.mytg.util.paytool.IabResult;
import org.cafemember.messenger.mytg.util.paytool.Purchase;

import io.nivad.iab.BillingProcessor;
import io.nivad.iab.TransactionDetails;

public class PayActivityNivad extends Activity {


	static final int RC_REQUEST = 10001;

	private static final String NIVAD_APP_ID = "896b2db5-c89e-4ec9-a70a-72282d474e21";
    private static final String NIVAD_APP_SECRET = "04t87AOgv66YHW6C5814DXdN3zOplbn8npqXIw972fsu92ZWZpEUj2TkijwAgHGs";
//    final String BAZAAR_KEY = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDwTZhig/xOyPNvi1UWMqpXftPV4t8zfgnMwfiWfzFUDGIdTjbKOvGZPV6mHpPVkLXYiTNq7mLg2r6IZFBr8DEMj4LXUpkgjW5IFW3VWWcCxE9eTSiFLPBR5iHFaVkLQdOMW8ALsdARbeRJ5AsjFQV6cIbqk6AXWUxtwFrwYHqwPJBiUBoSRin54giYmkAsX91X4dzCLzrWTqCCDDdPs9KYDYr5tmFd0j5SP5bNrWkCAwEAAQ==";
    final String BAZAAR_KEY = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDho82R4VhTDIMaOoi2zUKUomrhcuSElL9yvGvUjySWKnepiAfh4wBUcz+KVeMpI0snuVrsPRbcBILhg7eQFbxa9pbKknweG9s/er8ap7su28FSgoNrGTBZC+hQvgXtEkg7jMZAIn8rL/HGgYTlY0NG21D42Qf/sx3kQmrE2dInj8C2lGVkfK4CPv/OVxUvW6EidGOnKIVtZzOZoEVclZ1SmwVeUt/T+0yMIgJkSXUCAwEAAQ==";

	IabHelper mHelper;

	ListView listView;
	Dialog progressDialog;
	private String sku;
	private BillingProcessor mNivadBilling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sku = getIntent().getStringExtra("sku");
		mNivadBilling = new BillingProcessor(this, BAZAAR_KEY, null, null, mBillingMethods);


	}


	private BillingProcessor.IBillingHandler mBillingMethods = new BillingProcessor.IBillingHandler() {

		@Override
		public void onBillingInitialized() {
			// این متد زمانی که سرویس پرداخت درون برنامه‌ای آماده‌ی کار می‌شود فراخوانی می‌شود
            mNivadBilling.purchase(PayActivityNivad.this, sku);

        }


		@Override
		public void onProductPurchased(String sku, final TransactionDetails details) {
			// این متد پس از خرید موفق فراخوانی می‌شود
            Commands.checkBoughtItem(sku, details.purchaseInfo.responseData, new OnJoinSuccess() {
                @Override
                public void OnResponse(boolean ok) {
                    if (ok) {
                        if (mNivadBilling.consumePurchase(details.productId)) {
                            Toast.makeText(PayActivityNivad.this,"خرید انجام شد",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PayActivityNivad.this,"خرید انجام شد ولی بازار نفهمید!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    finish();
                }
            });
		}

		@Override
		public void onBillingError(int code, Throwable error) {
			// این متد زمانی که اشکالی در فرایند پرداخت به وجود بیاید فراخوانی می‌شود
            Toast.makeText(PayActivityNivad.this,"خطا "+code,Toast.LENGTH_SHORT).show();
        finish();
        }


		@Override
		public void onPurchaseHistoryRestored() {
			// این متد زمانی فراخوانی می‌شود که لیست محصولاتی که کاربر خریده اما هنوز مصرف نشده‌اند از بازار دریافت شده اند
		}
	};

	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (!mNivadBilling.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onDestroy() {
        if (mNivadBilling != null)
            mNivadBilling.release();
    	super.onDestroy();

    }
	
    

    
    
     
}

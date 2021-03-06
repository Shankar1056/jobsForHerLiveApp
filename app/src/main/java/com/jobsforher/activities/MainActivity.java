package com.jobsforher.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jobsforher.R;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Extras.PayUSdkDetails;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

/**
 * This activity prepares PaymentParams, fetches hashes from server and send it to PayuBaseActivity.java.
 * <p>
 * Implement this activity with OneClickPaymentListener only if you are integrating One Tap payments.
 */

public class MainActivity extends AppCompatActivity {

    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    private String mAction = ""; // For Final URL
    private String mTXNId; // This will create below randomly
    private String mHash; // This will create below randomly
    private String mProductInfo = "Food Items"; //Passing String only
    private String mFirstName; // From Previous Activity
    private String mEmailId; // From Previous Activity
    private double mAmount; // From Previous Activity
    private String mPhone; // From Previous Activity
    private String mServiceProvider = "payu";//"payu_paisa";
    private String mSuccessUrl = "https://www.payumoney.com/mobileapp/payumoney/success.php";  //"your success URL";
    private String mFailedUrl = "https://www.payumoney.com/mobileapp/payumoney/failure.php"; //"Your Failure URL";

    // This sets the configuration
    private PayuConfig payuConfig;

    private Spinner environmentSpinner;

    // Used when generating hash from SDK
    private PayUChecksum checksum;
    private EditText etSalt;
    private String salt = null;

    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            mFirstName = bundle.getString("name");
            mEmailId = bundle.getString("email");
            mAmount = bundle.getDouble("amount");
            mPhone = bundle.getString("phone");
            mHash = bundle.getString("hash");
            mTXNId = mHash;//"m_"+bundle.getString("txnid");
            mProductInfo = bundle.getString("productinfo");

            Log.d("TAGG", "PAY G atatus"+mFirstName+mEmailId+mAmount+mPhone+mHash+mTXNId+mProductInfo);
        }

        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);

        // lets set up the tool bar;
        Toolbar toolBar = (Toolbar) findViewById(R.id.app_bar);
        toolBar.setTitle("PayU Demo App");
        toolBar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolBar);

        // lets tell the people what version of sdk we are using
        PayUSdkDetails payUSdkDetails = new PayUSdkDetails();

        //   Toast.makeText(this, "Build No: " + payUSdkDetails.getSdkBuildNumber() + "\n Build Type: " + payUSdkDetails.getSdkBuildType() + " \n Build Flavor: " + payUSdkDetails.getSdkFlavor() + "\n Application Id: " + payUSdkDetails.getSdkApplicationId() + "\n Version Code: " + payUSdkDetails.getSdkVersionCode() + "\n Version Name: " + payUSdkDetails.getSdkVersionName(), Toast.LENGTH_LONG).show();

        //Lets setup the environment spinner
        environmentSpinner = (Spinner) findViewById(R.id.spinner_environment);
        //  List<String> list = new ArrayList<String>();
        String[] environmentArray = getResources().getStringArray(R.array.environment_array);
/*        list.add("Test");
        list.add("Production");*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, environmentArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        environmentSpinner.setAdapter(dataAdapter);
        environmentSpinner.setSelection(0);

        environmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (environmentSpinner.getSelectedItem().equals("Production")) {
                    Toast.makeText(MainActivity.this, getString(R.string.use_live_key_in_production_environment), Toast.LENGTH_SHORT).show();

                    /* For test keys, please contact mobile.integration@payu.in with your app name and registered email id
                     */
                    // ((EditText) findViewById(R.id.editTextMerchantKey)).setText("0MQaQP");
                    ((EditText) findViewById(R.id.editTextMerchantKey)).setText("smsplus");
                    ((EditText) findViewById(R.id.editTextMerchantSalt)).setText("1b1b0");
                }
                else{
                    //set the test key in test environment
                    ((EditText) findViewById(R.id.editTextMerchantKey)).setText("gtKFFX");
                    ((EditText) findViewById(R.id.editTextMerchantSalt)).setText("eCwWELxi");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        navigateToBaseActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
                getIntent().putExtra("result", data.getStringExtra("result"));
                getIntent().putExtra("txnid", mTXNId);
                getIntent().putExtra("hash",mHash);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(1, getIntent());
                finish();

//                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();

            } else {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(2, getIntent());
                finish();
            }
        }
    }

    /**
     * This method prepares all the payments params to be sent to PayuBaseActivity.java
     */
    public void navigateToBaseActivity() {


        // merchantKey="";
        merchantKey = "gtKFFx"; //((EditText) findViewById(R.id.editTextMerchantKey)).getText().toString();
        etSalt = ((EditText) findViewById(R.id.editTextMerchantSalt));
        String amount = String.valueOf(mAmount);//((EditText) findViewById(R.id.editTextAmount)).getText().toString();
        String email = mEmailId;//((EditText) findViewById(R.id.editTextEmail)).getText().toString();

        String value = environmentSpinner.getSelectedItem().toString();
        int environment;
        String TEST_ENVIRONMENT = getResources().getString(R.string.test);
        if (value.equals(TEST_ENVIRONMENT))
            environment = PayuConstants.STAGING_ENV;
        else
            environment = PayuConstants.PRODUCTION_ENV;

        userCredentials = merchantKey + ":" + email;

        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();
        /**
         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id

         */
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(String.valueOf(mAmount));
        mPaymentParams.setProductInfo(mProductInfo);
        mPaymentParams.setFirstName(mFirstName);
        mPaymentParams.setEmail(mEmailId);
        mPaymentParams.setPhone(mPhone);


        /*
         * Transaction Id should be kept unique for each transaction.
         * */
        mPaymentParams.setTxnId(mTXNId);

        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */
        mPaymentParams.setSurl(" https://payuresponse.firebaseapp.com/success");
        mPaymentParams.setFurl("https://payuresponse.firebaseapp.com/failure");
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        /*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */
        mPaymentParams.setUdf1("");
        mPaymentParams.setUdf2("");
        mPaymentParams.setUdf3("");
        mPaymentParams.setUdf4("");
        mPaymentParams.setUdf5("");

        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        // generateHashFromServer(mPaymentParams);

        /**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * */
        if(environment== PayuConstants.STAGING_ENV){
            salt = "eCwWELxi";
        }else {
            //Production Env
            salt = "1b1b0";
        }
        etSalt.setText(salt);
//        String salt = "eCwWELxi";
        // String salt = "13p0PXZk";
        // String salt = "1b1b0";
        //
        generateHashFromSDK(mPaymentParams, salt);

    }

    /******************************
     * Client hash generation
     ***********************************/
    // Do not use this, you may use this only for testing.
    // lets generate hashes.
    // This should be done from server side..
    // Do not keep salt anywhere in app.
    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();

        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }

        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }

        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }

        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }

    // deprecated, should be used only for testing.
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }
    /**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     */
    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.SALT,salt);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        onPressingBack();
    }

    private void onPressingBack() {

        final Intent intent;

//        if(isFromOrder)
//            intent = new Intent(PayUMoneyActivity.this, ProductInCartList.class);
//        else
//            intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Warning");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to cancel this transaction?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(2, getIntent());
                finish();
                // startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}

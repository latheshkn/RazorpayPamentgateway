package com.example.paymentgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
EditText enterAmount;
Button pay;
TextView transactionId;
String aamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instance of checkout
        Checkout.preload(getApplicationContext());

        enterAmount = findViewById(R.id.enterAmount);
        pay = findViewById(R.id.pay);
        transactionId = findViewById(R.id.transactionId);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }

    private void startPayment() {

        aamount = enterAmount.getText().toString();
        double totalAmount = Double.parseDouble(aamount);
        totalAmount = totalAmount*100;

        //Initiate Checkout
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_eZRHdqSKKo9Vu9");

        //Set your logo here
        checkout.setImage(R.drawable.ic_baseline_payment_24);

        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Invoice Pvt");
            options.put("description", "Invoice Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", totalAmount);//pass amount in currency subunits Eg 100 => INR 1.00 Rs

//            Email and contact number of Customer who is going to pay
            JSONObject preFill = new JSONObject();
            preFill.put("email", "user@gmail.com");
            preFill.put("contact", "0123456789");

            options.put("prefill", preFill);

            checkout.open(activity, options);

        } catch(Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            transactionId.setText("Payment Successful: " + razorpayPaymentID);
        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentError", e);e
        }
    }
}
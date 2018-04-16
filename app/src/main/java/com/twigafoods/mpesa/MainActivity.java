package com.twigafoods.mpesa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twigafoods.daraja.Daraja;
import com.twigafoods.daraja.DarajaListener;
import com.twigafoods.daraja.model.AccessToken;
import com.twigafoods.daraja.model.LNMExpress;
import com.twigafoods.daraja.model.LNMResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.sendButton)
    Button sendButton;

    //Declare Daraja :: Global Variable
    Daraja daraja;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Init Daraja
        //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
        daraja = Daraja.with("EG6TJgOCyuVGHKUeJs0uLXKsLCANRjbo", "4F3tAlRZ4OzNZt0Y", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MainActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(MainActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(MainActivity.this.getClass().getSimpleName(), error);
            }
        });

        //TODO :: THIS IS A SIMPLE WAY TO DO ALL THINGS AT ONCE!!! DON'T DO THIS :)
        sendButton.setOnClickListener(v -> {

            //Get Phone Number from User Input
            phoneNumber = editTextPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                editTextPhoneNumber.setError("Please Provide a Phone Number");
                return;
            }

            //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
            LNMExpress lnmExpress = new LNMExpress(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                    "100",
                    "254708374149",
                    "174379",
                    phoneNumber,
                    "http://mycallbackurl.com/checkout.php",
                    "001ABC",
                    "Goods Payment"
            );

            daraja.sendSTKPush(lnmExpress,
                    new DarajaListener<LNMResult>() {
                        @Override
                        public void onResult(@NonNull LNMResult lnmResult) {
                            Log.i(MainActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i(MainActivity.this.getClass().getSimpleName(), error);
                        }
                    }
            );
        });
    }
}

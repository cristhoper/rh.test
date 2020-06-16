package com.example.interviewapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class FullscreenActivity extends AppCompatActivity {
    private LinearLayout mMenuPaymentLay;
    private LinearLayout mMenuStationsLay;

    private final LinearLayout.OnClickListener mMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.menu_payment:
                    openPopup(view.getContext());
                    break;
                case R.id.menu_route:
                    validateUser();
                    break;
                default:
                    break;
            }

        }
    };

    private void validateUser() {
        // TODO :(
    }

    private void openPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.dialog_string));

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.dialog_rut_hint);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String plainRut = input.getText().toString();
                plainRut = plainRut.replace(".", "");
                plainRut = plainRut.replace("-", "");
                String encryptRut = encryptId(plainRut);
                searchRut(encryptRut);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void searchRut(String encryptedRut) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sandbox.ionix.cl/test-tecnico/search?rut="+encryptedRut;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(":(");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private String encryptId(String plainRut) {
        String encryptedRut = null;
        try {
            DESKeySpec keySpec = new DESKeySpec("ionix123456".getBytes( "UTF8" ));
            SecretKeyFactory keyFactory = SecretKeyFactory. getInstance ( "DES" );
            byte [] cleartext = plainRut.getBytes( "UTF8" );
            Cipher cipher = Cipher.getInstance( "DES");
            encryptedRut = Base64.encodeToString (cipher.doFinal(cleartext), Base64. DEFAULT );
        } catch (Exception e) {
            encryptedRut = null;
            e.printStackTrace();
        }
        return encryptedRut;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.menu_conf_fa), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.menu_envelope_fa), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.menu_payment_fa), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.menu_route_fa), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.menu_stations_fa), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.menu_tag_fa), iconFont);

        mMenuPaymentLay = findViewById(R.id.menu_payment);
        mMenuStationsLay = findViewById(R.id.menu_route);

        mMenuPaymentLay.setOnClickListener(mMenuListener);
        mMenuStationsLay.setOnClickListener(mMenuListener);
    }

}
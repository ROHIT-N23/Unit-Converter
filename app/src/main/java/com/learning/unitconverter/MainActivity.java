package com.learning.unitconverter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CountryCodePicker from_picker, to_picker;
     EditText from_text;
     TextView to_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from_picker = findViewById(R.id.from_picker);
        to_picker = findViewById(R.id.to_picker);
        from_text = findViewById(R.id.from_textView);
        to_text = findViewById(R.id.to_textView);

        // Setting Default Local CountyCode //
        Locale fromLocal = new Locale("ENGLISH", from_picker.getDefaultCountryNameCode());
        Locale toLocal = new Locale("ENGLISH", to_picker.getDefaultCountryNameCode());

        String from_unit = Currency.getInstance(fromLocal).getCurrencyCode();
        String to_unit = Currency.getInstance(toLocal).getCurrencyCode();
        requestCurrency(from_unit, to_unit);


        // Value Changes Whenever Country Changes //
        from_picker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                // Converting Country Name Code Into Currency Code With Local //
                Locale fromLocal = new Locale("ENGLISH", from_picker.getSelectedCountryNameCode());
                Locale toLocal = new Locale("ENGLISH", to_picker.getSelectedCountryNameCode());

                String from_unit = Currency.getInstance(fromLocal).getCurrencyCode();
                String to_unit = Currency.getInstance(toLocal).getCurrencyCode();
                requestCurrency(from_unit, to_unit);
            }
        });

        // Value Changes Whenever Country Changes //
        to_picker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                // Converting Country Name Code Into Currency Code With Local //
                Locale fromLocal = new Locale("ENGLISH", from_picker.getSelectedCountryNameCode());
                Locale toLocal = new Locale("ENGLISH", to_picker.getSelectedCountryNameCode());

                String from_unit = Currency.getInstance(fromLocal).getCurrencyCode();
                String to_unit = Currency.getInstance(toLocal).getCurrencyCode();
                requestCurrency(from_unit, to_unit);
            }
        });

    }


    public void requestCurrency(String base_currency, String to_currency){

        // Creating New requestQueue //
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Creating New Object Request //                                                                                                                  // Base Currency Coming From Country Picker //
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,"https://v6.exchangerate-api.com/v6/7604b81ed02c2f2f03cdc388/latest/" + base_currency, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    // Getting result From JSONresponce //
                    JSONObject result = response.getJSONObject("conversion_rates");
                    from_text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                           
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            // Every Time The text Changes //
                            double currency = 0;
                            try {

                                // Setting Result With Calculating With BaseCurrency With ToCurrency
                                currency = result.getDouble(to_currency);
                                String text = Double.parseDouble(s.toString()) * currency + "";
                                to_text.setText(text);
                            } catch (JSONException | NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Turn On Tour Internet First", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request To Request queue //
        requestQueue.add(jsonObjectRequest);

    }


}
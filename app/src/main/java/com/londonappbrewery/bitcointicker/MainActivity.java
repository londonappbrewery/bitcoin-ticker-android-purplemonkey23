package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    // private final String BASE_URL = "https://apiv2.bitcoinaverage.com/constants/exchangerates/local";
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/short";
    // ?crypto=BTC&fiat=USD,EUR

    // Member Variables:
    TextView mPriceTextView;
    String currancyShortCode;
    String mBitCoinPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        final Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);





        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currencyCode = spinner.getSelectedItem().toString();

                Log.d("BitCoin", "tracker:" + currencyCode);

                RequestParams params = new RequestParams();
                params.put("crypto", "BTC");
                params.put("fiat", currencyCode);
                currancyShortCode = "BTC" + currencyCode;

                letsDoSomeNetworking(BASE_URL, params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("BitCoin", "nothing selected");
            }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url, RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(BASE_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                BitCoinDataModel bitCoinData = BitCoinDataModel.fromJson(currancyShortCode, response);
                updateUI(bitCoinData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("BitCoin", "JSON Failed:"  + statusCode);
                Log.e("ERROR", throwable.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("BitCoin", "JSON Failed:"  + statusCode);
                Log.e("ERROR", throwable.toString());
            }
        });
    }

    private void updateUI(BitCoinDataModel bitCoinData) {
        mBitCoinPrice = bitCoinData.getPrice();
        mPriceTextView.setText(mBitCoinPrice);
    }

}

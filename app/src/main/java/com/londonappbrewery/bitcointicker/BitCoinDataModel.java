package com.londonappbrewery.bitcointicker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 17/02/2018.
 */

public class BitCoinDataModel {

    private String mPrice;

    {
        mPrice = "0";
    }


    public static BitCoinDataModel fromJson(String shortCode, JSONObject jsonObject) {
        // JSON parsing is risky business. Need to surround the parsing code with a try-catch block.
        try {


            BitCoinDataModel bitCoinData = new BitCoinDataModel();

            double coinResult = jsonObject.getJSONObject(shortCode).getDouble("last");
            int roundedValue = (int) Math.rint(coinResult);

            bitCoinData.mPrice = Integer.toString(roundedValue);

            return bitCoinData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPrice() {
        return mPrice;
    }
}

package com.yiling.address;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yiling.addresslib.utils.ChooseCityInterface;
import com.yiling.addresslib.utils.ChooseCityUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view) {
        ChooseCityUtil util = new ChooseCityUtil();
        util.createDialog(this, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                Log.e("syw", newCityArray[0] + newCityArray[1] + newCityArray[2]);
            }

            @Override
            public void cancel() {

            }
        });
    }
}

package com.amst.g8.heroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class DetailActivity extends AppCompatActivity {

    public static final String SUPER_ID = "HeroId";
    private TextView tvHeroName, tvRealName;
    private int heroId;
    private Context mContext = this;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        heroId = getIntent().getIntExtra(SUPER_ID, 0);
        tvHeroName = findViewById(R.id.tvHeroName);
        tvRealName = findViewById(R.id.tvRealName);
        barChart = findViewById(R.id.bcPowerStats);
        getHeroDetails();
    }

    private void getHeroDetails() {
        if (heroId != 0) {
            String url = String.format("%s%s/%d", Api.BASE_URL, Api.API_TOKEN, heroId);
           JsonObjectRequest objectRequest = new JsonObjectRequest(url, null,
                   new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   try {
                       Log.v("RESPONSE", response.toString());

                       tvHeroName.setText(response.getString("name"));
                       tvRealName.setText(response.getJSONObject("biography")
                               .getString("full-name"));


                       JSONObject powerStats = response.getJSONObject("powerstats");

                       ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                       ArrayList<BarEntry> arrayList = new ArrayList<>();

                       Iterator<String> iter = powerStats.keys();
                       int c = 0;
                       while (iter.hasNext()) {
                           String key = iter.next();
                           try {
                               BarEntry barEntry = new BarEntry(0+c, powerStats.getInt(key));
                               arrayList.add(barEntry);
                               c += 1;
                           } catch (Exception e) {
                               // Something went wrong!
                           }
                       }

                       BarDataSet powersDataSet = new BarDataSet(arrayList, "Powers");
                       powersDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                       powersDataSet.setDrawValues(true);
                       dataSets.add(powersDataSet);

                       BarData data = new BarData(dataSets);
                       barChart.setData(data);
                       barChart.setFitBars(true);
                       barChart.invalidate();
                       barChart.notifyDataSetChanged();
                   } catch (Exception e) {

                   }
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Log.v("VolleyError", error.toString());
               }
           });
            Volley.newRequestQueue(mContext).add(objectRequest);
        }
    }

}

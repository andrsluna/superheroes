package com.amst.g8.heroproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResultsActivity extends AppCompatActivity {

    private String superHeroName;
    private TextView txtResultsCount;
    private Context mContext = this;
    public static final String SUPER_NAME = "SUPER_NAME";
    private LinearLayout llHeroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        txtResultsCount = findViewById(R.id.tvResultsCount);
        llHeroes = findViewById(R.id.llResultsContainer);
        superHeroName = getIntent().getStringExtra(SUPER_NAME);
        searchHeroe();
    }

    private void searchHeroe() {
        String url = String.format("%s%s/search/%s", Api.BASE_URL, Api.API_TOKEN, superHeroName);

        JsonObjectRequest heroesRequest = new JsonObjectRequest(url, null ,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    txtResultsCount.append(String.format(" %d", results.length()));
                    for (int i=0; i<results.length(); i++) {
                        final JSONObject hero = results.getJSONObject(i);
                        TextView tvHeroName = new TextView(mContext);
                        tvHeroName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        tvHeroName.setText(hero.getString("name"));
                        tvHeroName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = new Intent(mContext, DetailActivity.class);
                                    intent.putExtra(DetailActivity.SUPER_ID, hero.getInt("id"));
                                    startActivity(intent);
                                } catch (Exception ignored) {}
                            }
                        });
                        llHeroes.addView(tvHeroName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());
            }
        });
        Volley.newRequestQueue(mContext).add(heroesRequest);
    }


}

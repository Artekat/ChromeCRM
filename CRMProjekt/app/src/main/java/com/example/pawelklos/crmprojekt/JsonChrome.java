package com.example.pawelklos.crmprojekt;

import android.content.res.Resources;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonChrome extends AppCompatActivity {
    String data = "";
    ArrayList<ArrayList> tableMain = new ArrayList<ArrayList>();
    ArrayList secondaryMain = new ArrayList();
    TextView textViewResults = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonchrome);
        parsingJson();
    }

    public void parsingJson() {
        String strJson = loadJSONFromAsset();
        data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonRootObject.optJSONArray("Company");
            for (int i = 0; i < jsonArray.length(); i++) {
                String record = "";
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                secondaryMain.add("Id Firmy= " + Integer.parseInt(jsonObject.optString("id").toString()));
                String name = jsonObject.optString("name").toString();
                secondaryMain.add("Nazwa= " + name);
                JSONArray jsonArraysal = jsonObject.getJSONArray("salary");
                buttonCreation(name, i);
                for (int j = 0; j < jsonArraysal.length(); j++) {
                    JSONObject youValue = jsonArraysal.getJSONObject(j);
                    secondaryMain.add("Numer pracownika= " + Integer.parseInt(youValue.optString("idPrac").toString()));
                    secondaryMain.add("Stawka= " + youValue.optString("zarobek").toString());
                    secondaryMain.add("" + Boolean.parseBoolean(youValue.optString("kierownik")));
                }
                tableMain.add(secondaryMain);
                secondaryMain = new ArrayList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelay);
            textViewResults.setWidth(getScreenWidth());
            textViewResults.setY(tableMain.size() * getScreenHeight() / 7);
            relativeLayout.addView(textViewResults);
        }
    }

    public String loadJSONFromAsset() {
        textViewResults = new TextView(this);
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public View.OnClickListener getOnClickDoSomething(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < tableMain.get(button.getId()).size(); i++) {
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelay);
                    relativeLayout.removeView(relativeLayout.findViewWithTag(1));
                }
                String results = "";
                for (int i = 0; i < tableMain.get(button.getId()).size(); i++) {
                    if ((tableMain.get(button.getId()).get(i)).equals("true") || (tableMain.get(button.getId()).get(i)).equals("false")) {
                        checkboxCreation(tableMain.get(button.getId()).get(i) + "", ((tableMain.size() * getScreenHeight() / 7) + i * 23));
                        results += "\n";
                    } else
                        results += tableMain.get(button.getId()).get(i) + "\n";
                }
                textViewResults.setText(results);
            }
        };
    }

    public void buttonCreation(String name, int a) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelay);
        Button button = new Button(this);
        button.setText(name);
        button.setWidth(getScreenWidth());
        button.setHeight(getScreenHeight() / 7);
        button.setY(a * (getScreenHeight() / 7));
        button.setId(a);
        button.setOnClickListener(getOnClickDoSomething(button));
        relativeLayout.addView(button);

    }

    public void checkboxCreation(String checked, int i) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelay);
        CheckBox checkbox = new CheckBox(this);
        checkbox.setY(i - 8);
        checkbox.setEnabled(false);
        checkbox.setTag(1);
        checkbox.setChecked(Boolean.valueOf(checked));
        checkbox.setText("Kierownik");
        relativeLayout.addView(checkbox);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}

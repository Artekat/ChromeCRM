package com.example.pawelklos.crmprojekt;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    ArrayList tableMain = new ArrayList();
    TextView textViewResults = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonchrome);
        parsingJson();
    }
    public void parsingJson() {
    //    String record="";
        String strJson = loadJSONFromAsset();
        data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonRootObject.optJSONArray("Company");
            for (int i = 0; i < jsonArray.length(); i++) {
                String record="";
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = Integer.parseInt(jsonObject.optString("id").toString());
                String name = jsonObject.optString("name").toString();
                JSONArray jsonArraysal = jsonObject.getJSONArray("salary");
                buttonCreation(name, i);
                int idPrac = 0;
                String zarobek = "";
                record+="Id Firmy= " + id + " \nNazwa = " + name+"\n";
                for (int j = 0; j < jsonArraysal.length(); j++) {
                    JSONObject youValue = jsonArraysal.getJSONObject(j);
                    idPrac = Integer.parseInt(youValue.optString("idPrac").toString());
                    zarobek = youValue.optString("zarobek").toString();
                    record+="\nNumer pracownika= " + idPrac + "\nStawka= " + zarobek + "\n";
                }
                tableMain.add( record);

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
                textViewResults.setText(tableMain.get(button.getId()) + "");
            }
        };
    }
    public void buttonCreation(String name, int a) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelay);
        Button button = new Button(this);
        button.setText(name);
        button.setWidth(getScreenWidth());
        button.setHeight(getScreenHeight()/7);
        button.setY(a * (getScreenHeight()/7));
        button.setId(a);
        button.setOnClickListener(getOnClickDoSomething(button));
        relativeLayout.addView(button);
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}

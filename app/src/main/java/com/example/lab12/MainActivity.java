package com.example.lab12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_search).setOnClickListener(v -> {
            String URL = "https://tools-api.italkutalk.com/java/lab12";
            Request request = new Request.Builder().url(URL).build();
            //OkHttpClient okHttpClient = new OkHttpClient();
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback(){
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException{
                    if(response.code() == 200){
                        Data data = new Gson().fromJson(response.body().string(), Data.class);
                        final String [] items = new String[data.result.results.length];
                        for(int i = 0; i < items.length; i++){
                            items[i] = "\n列車即將進入:" + data.result.results[i].Station +
                                    "\n列車行駛目的地:" + data.result.results[i].Destination;
                        }
                        runOnUiThread(() -> {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("台北捷運列車到站站名")
                                    .setItems(items, null)
                                    .show();
                        });
                    } else if(!response.isSuccessful()){
                        Log.e("伺服器錯誤", response.code() + " " + response.message());
                    }
                    else {
                        Log.e("其他錯誤", response.code() + " " + response.message());
                    }
                }
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e){
                    if(e.getMessage() != null){
                        Log.e("查詢失敗", e.getMessage());
                    }
                }
            });
        });
    }
}
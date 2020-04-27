package com.example.td3;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://pokeapi.co/";

    private RecyclerView recyclerView;
    private ListAdapteur mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        showlist();
        makeApiCall();

    }

    private void showlist() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_VIEW);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
            // define an adapter
            mAdapter = new ListAdapteur(input);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void makeApiCall() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PokeApi pokeApi = retrofit.create(PokeApi.class);

        Call<RestPokemonResponses> call = pokeApi.getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponses>() {
            @Override
            public void onResponse(Call<RestPokemonResponses> call, Response<RestPokemonResponses> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Pokemon> pokemonList = response.body().getResults();
                    Toast.makeText(getApplicationContext(), "API Succes", Toast.LENGTH_SHORT).show();
                } else {
                    showError();
                }

            }

            @Override
            public void onFailure(Call<RestPokemonResponses> call, Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.atharvakale.facerecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LogPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
     private DB_Helpers dbHelper;
    String EnYeniyeGoreSirala = DB_constants.C_DATE + " DESC ";
    String EnEskiyeGoreSirala = DB_constants.C_DATE + " ASC ";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_page);

        recyclerView = findViewById(R.id.rvid);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DB_Helpers(this);

        // verileri en yeniye göre yükle
        loadData(EnYeniyeGoreSirala);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.YenidenEskiye:
                loadData(EnYeniyeGoreSirala);
                return true;
            case R.id.EskidenYeniye:
                loadData(EnEskiyeGoreSirala);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData(String siralama) {
        adapter Adapter = new adapter(LogPage.this, dbHelper.fetchRecord(siralama));
        recyclerView.setAdapter(Adapter);
    }
}
package com.kalpvaig.k_raspstream;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelectConfiguration extends AppCompatActivity {

    private Button saveButton;
    private EditText username, password, rtspurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_configuration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rtspurl = (EditText) findViewById(R.id.rtspurl);

        saveButton = (Button) findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data
                Prefs.saveToPreferences(SelectConfiguration.this,"username",username.getText().toString());
                Prefs.saveToPreferences(SelectConfiguration.this,"password",password.getText().toString());
                Prefs.saveToPreferences(SelectConfiguration.this,"rtsp_url",rtspurl.getText().toString());
                Toast.makeText(SelectConfiguration.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

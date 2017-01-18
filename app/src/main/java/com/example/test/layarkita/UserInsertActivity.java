package com.example.test.layarkita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class UserInsertActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG = "UserInsertActivity";

    private EditText etName, etDeskripsi, etYoutubeUrl, etYoutubeCode;
    private Button btnInsert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (EditText)findViewById(R.id.etName);
        etDeskripsi = (EditText)findViewById(R.id.etDeskripsi);
        etYoutubeUrl = (EditText)findViewById(R.id.etYoutubeUrl);
        etYoutubeCode = (EditText)findViewById(R.id.etYoutubeCode);
        btnInsert = (Button)findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final String cekKode = etYoutubeCode.getText().toString();
        final String cekJudul = etName.getText().toString();
        final String cekDesk = etDeskripsi.getText().toString();
        if (cekJudul.length() < 5) {
            etName.setError("Judul terlalu pendek");
        } else if (cekDesk.length() < 5){
            etDeskripsi.setError("Deskripsi terlalu pendek");
        } else if (cekKode.length() != 11) {
            etYoutubeCode.setError("Kode URL Tidak Sesuai");
        } else {
            HashMap postData = new HashMap();
            postData.put("txtName", etName.getText().toString());
            postData.put("txtDeskripsi", etDeskripsi.getText().toString());
            postData.put("txtYoutubeUrl", etYoutubeUrl.getText().toString());
            postData.put("txtYoutubeCode", etYoutubeCode.getText().toString());
            postData.put("mobile", "android");

            PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(UserInsertActivity.this, postData,
                    new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(LOG, s);
                            if (s.contains("success")) {
                                Toast.makeText(UserInsertActivity.this, "Terimakasih telah merekomendasikan video pada kami!", Toast.LENGTH_LONG).show();
                                Intent in = new Intent(UserInsertActivity.this, UserListActivity.class);
                                startActivity(in);
                            } else {
                                Toast.makeText(UserInsertActivity.this, "Form Harus diisi!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            taskInsert.execute("http://192.168.201.1/layarkita/insert_rekomen.php");
        }
    }
}

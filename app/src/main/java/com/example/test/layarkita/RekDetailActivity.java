package com.example.test.layarkita;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class RekDetailActivity extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener {
    final String LOG = "RekDetailActivity";

    EditText etName, etDeskripsi, etYoutubeUrl, etYoutubeCode;
    YouTubePlayerView ivImage;
    Button btnPost, btnHapus;
    Switch mySwitch;
    TextView switchStatus, status;


    private Product product;
    public static final String API_KEY = " AIzaSyCjTZUgaPKKWiXBfgRghEzYpOBBinTnbNc";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rek_detail);

        product = (Product) getIntent().getSerializableExtra("product");

        switchStatus = (TextView) findViewById(R.id.switchStatus);
        status = (TextView) findViewById(R.id.status);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        mySwitch.setChecked(false);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchStatus.setText("1");
                    status.setText("ON");
                }else{
                    switchStatus.setText("0");
                    status.setText("OFF");
                }
            }
        });


        etName = (EditText)findViewById(R.id.etName);
        etDeskripsi = (EditText)findViewById(R.id.etDeskripsi);
        etYoutubeUrl = (EditText)findViewById(R.id.etYoutubeUrl);
        etYoutubeCode = (EditText)findViewById(R.id.etYoutubeCode);
        ivImage = (YouTubePlayerView)findViewById(R.id.ivImage);

        ivImage.initialize(API_KEY, this);

        if(product != null){
            etName.setText(product.name);
            etDeskripsi.setText( product.deskripsi);
            etYoutubeUrl.setText(product.youtube_url);
            etYoutubeCode.setText(product.youtube_code);

        }


        btnHapus = (Button)findViewById(R.id.btnHapus);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap postData = new HashMap();
                postData.put("pid", "" + product.pid);
                postData.put("mobile", "android");

                PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(RekDetailActivity.this, postData,
                        new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                Log.d(LOG, s);
                                if (s.contains("success")){
                                    Toast.makeText(RekDetailActivity.this, "Remove Successfully", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(RekDetailActivity.this, RekListActivity.class);
                                    startActivity(in);
                                }
                            }
                        });
                taskUpdate.execute("http://192.168.201.1/layarkita/remove.php");
            }
        });


        btnPost = (Button)findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cekKode = etYoutubeCode.getText().toString();
                final String cekJudul = etName.getText().toString();
                final String cekDesk = etDeskripsi.getText().toString();
                final String EMPTY_STRING = "";
                if (cekJudul.length() < 5) {
                    etName.setError("Judul terlalu pendek");
                } else if (cekDesk.length() < 5){
                    etDeskripsi.setError("Deskripsi terlalu pendek");
                } else if (cekKode.length() != 11) {
                    etYoutubeCode.setError("Kode URL Tidak Sesuai");
                } else {
                    HashMap postData = new HashMap();
                    postData.put("txtPid", "" + product.pid);
                    postData.put("txtName", etName.getText().toString());
                    postData.put("txtDeskripsi", etDeskripsi.getText().toString());
                    postData.put("txtYoutubeUrl", etYoutubeUrl.getText().toString());
                    postData.put("txtYoutubeCode", etYoutubeCode.getText().toString());
                    postData.put("txtPublish", switchStatus.getText().toString());
                    postData.put("mobile", "android");

                    PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(RekDetailActivity.this, postData,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(LOG, s);
                                    if (s.contains("success")) {
                                        Toast.makeText(RekDetailActivity.this, "Update Successfully", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(RekDetailActivity.this, RekListActivity.class);
                                        startActivity(in);
                                    } else {
                                        Toast.makeText(RekDetailActivity.this, "Form Harus diisi!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                    taskInsert.execute("http://192.168.201.1/layarkita/update_rekomen.php");

                }
            }
        });


    }



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        if (!restored){
            youTubePlayer.cueVideo(product.youtube_code);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Inisialisasi Gagal! Koneksi Internet Tidak Tersedia.",Toast.LENGTH_LONG).show();

    }




}
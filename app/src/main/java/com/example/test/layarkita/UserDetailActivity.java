package com.example.test.layarkita;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {
    final String LOG = "UserDetailActivity";

    TextView etName, etDeskripsi;
    YouTubePlayerView ivImage;
    Button btnShare, btnKomentar;
    EditText etKomenName, etKomenIsi;


    private Product product;
    public static final String API_KEY = " AIzaSyCjTZUgaPKKWiXBfgRghEzYpOBBinTnbNc";

    //    KOMENTARRRRRRRRRRRRRRR
    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_CID = "cid";
    private static final String TAG_NAME = "commentname";
    private static final String TAG_POS ="comment";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;
    ListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

//        KOMENTAR
        list = (ListView) findViewById(R.id.lvComment);
        personList = new ArrayList<HashMap<String,String>>();
        getData();

//          KOMENTAR


        product = (Product) getIntent().getSerializableExtra("product");


        etName = (TextView)findViewById(R.id.etName);
        etDeskripsi = (TextView)findViewById(R.id.etDeskripsi);
        ivImage = (YouTubePlayerView)findViewById(R.id.ivImage);
        etKomenName = (EditText)findViewById(R.id.etKomenName);
        etKomenIsi = (EditText)findViewById(R.id.etKomenIsi);

        ivImage.initialize(API_KEY, this);

        if(product != null){
            etName.setText(product.name);
            etDeskripsi.setText( product.deskripsi);

        }

        btnShare = (Button)findViewById(R.id.btnPost);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        btnKomentar = (Button)findViewById(R.id.btnKomentar);
        btnKomentar.setOnClickListener(this);


    }



    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Video");
//        harus bentuk link
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, product.youtube_url);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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


    // KOMENTARRRRRRRRRRRRRRRRRRRRR
    private void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String cid = c.getString(TAG_CID);
                String commentname = c.getString(TAG_NAME);
                String comment = c.getString(TAG_POS);

                HashMap<String,String> persons = new HashMap<String,String>();


                int ab = Integer.valueOf(cid.toString());
                if (cid.equals(product.pid)){

                    persons.put(TAG_NAME, commentname);
                    persons.put(TAG_POS, comment);

                    personList.add(persons);
                }

            }

            adapter = new SimpleAdapter(
                    UserDetailActivity.this, personList, R.layout.layout_comment,
                    new String[]{TAG_NAME,TAG_POS},
                    new int[]{R.id.name, R.id.position}
            );

            list.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.201.1/layarkita/komentar.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    @Override
    public void onClick(View v) {
        final String cekUser = etKomenName.getText().toString();
        final String cekIsi = etKomenIsi.getText().toString();
        if (cekUser.length() < 3) {
            etKomenName.setError("Nama terlalu pendek");
        } else if (cekIsi.length() < 5){
            etKomenIsi.setError("Komentar terlalu pendek");
        } else {
            HashMap postData = new HashMap();
            postData.put("cid", "" + product.pid);
            postData.put("txtKomenName", etKomenName.getText().toString());
            postData.put("txtKomenIsi", etKomenIsi.getText().toString());
            postData.put("mobile", "android");


            PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(UserDetailActivity.this, postData,
                    new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(LOG, s);
                            if (s.contains("success")) {
                                Toast.makeText(UserDetailActivity.this, "Berhasil!", Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(UserDetailActivity.this, "Form Harus diisi!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            taskInsert.execute("http://192.168.201.1/layarkita/inputkomen.php");
        }
    }
}

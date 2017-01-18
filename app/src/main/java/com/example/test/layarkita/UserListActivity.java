package com.example.test.layarkita;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity implements AsyncResponse, AdapterView.OnItemClickListener {

    final String LOG = "UserListActivity";
    private ArrayList<Product> productList;
    private ListView lvProduct;
    private FunDapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        ImageLoader.getInstance().init(UILConfig.config(UserListActivity.this));

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(UserListActivity.this, this);
        taskRead.execute("http://192.168.201.1/layarkita/product.php");

        lvProduct = (ListView)findViewById(R.id.lvProduct);


    }



    @Override
    public void processFinish(String s) {
        productList = new JsonConverter<Product>().toArrayList(s, Product.class);

        BindDictionary<Product> dict = new BindDictionary<Product>();
        dict.addStringField(R.id.tvName, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return product.name;
            }
        });

        dict.addStringField(R.id.tvDeskripsi, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return product.deskripsi;
            }
        });

        dict.addDynamicImageField(R.id.ivImage, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return product.youtube_code;
            }
        }, new DynamicImageLoader() {
            @Override
            public void loadImage(String url, ImageView imageView) {

                ImageLoader.getInstance().displayImage("http://img.youtube.com/vi/"+url+"/0.jpg", imageView);
            }
        });

        adapter = new FunDapter<>(UserListActivity.this, productList, R.layout.layout_list, dict);


        lvProduct.setAdapter(adapter);
        lvProduct.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product selectedProduct = productList.get(position);
        Intent in = new Intent(UserListActivity.this, UserDetailActivity.class);
        in.putExtra("product", (Serializable) selectedProduct);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda ingin keluar?");
        builder.setNegativeButton("Tidak", null);
        builder.setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_userlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rekomen_menu:
                Intent i = new Intent(UserListActivity.this, UserInsertActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

}

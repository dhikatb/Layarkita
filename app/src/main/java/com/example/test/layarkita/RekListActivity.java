package com.example.test.layarkita;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.HashMap;

public class RekListActivity extends AppCompatActivity implements AsyncResponse, AdapterView.OnItemClickListener {

    final String LOG = "RekListActivity";
    private ArrayList<Product> productList;
    private ListView lvProduct;
    private FunDapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rek_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ImageLoader.getInstance().init(UILConfig.config(RekListActivity.this));

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(RekListActivity.this, this);
        taskRead.execute("http://192.168.201.1/layarkita/product_rekomen.php");

        lvProduct = (ListView)findViewById(R.id.lvProduct);


        registerForContextMenu(lvProduct);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Product selectedProduct = adapter.getItem(info.position);
        Log.d(LOG, selectedProduct.name);

        if (item.getItemId() == R.id.menuUpdate){
            Intent in = new Intent(RekListActivity.this, RekDetailActivity.class);
            in.putExtra("product", selectedProduct);
            startActivity(in);
        }
        else if (item.getItemId() == R.id.menuRemove){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Apa kamu mau menghapus "+ selectedProduct.name +"?");
            alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    productList.remove(selectedProduct);
                    adapter.notifyDataSetChanged();

                    HashMap postData = new HashMap();
                    postData.put("pid", "" + selectedProduct.pid);
                    postData.put("mobile", "android");

                    PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(RekListActivity.this, postData,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(LOG, s);
                                    if (s.contains("success")){
                                        Toast.makeText(RekListActivity.this, "Remove Successfully", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    taskUpdate.execute("http://192.168.201.1/layarkita/remove.php");
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        }

        return super.onContextItemSelected(item);
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

                ImageLoader.getInstance().displayImage("http://img.youtube.com/vi/" + url + "/0.jpg", imageView);
            }
        });

        adapter = new FunDapter<>(RekListActivity.this, productList, R.layout.layout_list, dict);


        lvProduct.setAdapter(adapter);
        lvProduct.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product selectedProduct = productList.get(position);
        Intent in = new Intent(RekListActivity.this, RekDetailActivity.class);
        in.putExtra("product", (Serializable) selectedProduct);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(RekListActivity.this, ListActivity.class);
        startActivity(in);
    }
}

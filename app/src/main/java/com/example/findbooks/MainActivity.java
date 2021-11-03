package com.example.findbooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookDetails>> {

    private String url="https://www.googleapis.com/books/v1/volumes?q=";
    private String newUrl;
    private int LOADER_ID=1;
    private BooksArrayAdapter mAdaptor;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        emptyTextView=(TextView) findViewById(R.id.empty_text_state);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar_indicator);
        boolean isConnected = checkInternetConnectivity();
        if(!isConnected){
            // Handle no internet connectivity
            emptyTextView.setText("Check Your Intenet Connection!");
        }else{
            emptyTextView.setText("Search books on any topic!");
        }

        ListView listView=(ListView) findViewById(R.id.list_view);
        mAdaptor=new BooksArrayAdapter(MainActivity.this,new ArrayList<BookDetails>());




        listView.setAdapter(mAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDetails currentBookDetails=mAdaptor.getItem(position);
                Intent websiteIntent=new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(currentBookDetails.getmUrl()));
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_resource,menu);

        MenuItem searchItem=menu.findItem(R.id.search_bar_menu_item);
        SearchView searchView=(SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                emptyTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                newUrl=url+s;
                getLoaderManager().restartLoader(LOADER_ID,null,MainActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private Boolean checkInternetConnectivity(){
        // Code to check the network connectivity status
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<BookDetails>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this,newUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<BookDetails>> loader, List<BookDetails> data) {
        progressBar.setVisibility(View.GONE);

        mAdaptor.clear();
        if(data!=null && !data.isEmpty()){
            mAdaptor.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<BookDetails>> loader) {
        mAdaptor.clear();

    }
}

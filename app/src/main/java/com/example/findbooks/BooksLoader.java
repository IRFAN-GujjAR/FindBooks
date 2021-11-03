package com.example.findbooks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import androidx.annotation.Nullable;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<BookDetails>> {

    private String mUrl;

    public BooksLoader(Context context,String url){
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<BookDetails> loadInBackground() {
        if(mUrl==null){
            return null;
        }

        List<BookDetails> books=QueryUtils.fetchBooksData(mUrl);
        return books;
    }
}

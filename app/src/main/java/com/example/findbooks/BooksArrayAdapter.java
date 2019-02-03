package com.example.findbooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class BooksArrayAdapter extends ArrayAdapter<BookDetails> {

    public BooksArrayAdapter(Context context, List<BookDetails> book) {
        super(context, 0, book);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_design, parent, false);
        }
        Bitmap bitmap = null;
        BookDetails currentBookDetails = getItem(position);

        String requiredUrl;
        if (!currentBookDetails.getmBookThumbnail().isEmpty()) {
            ImageView bookImage = (ImageView) listView.findViewById(R.id.book_image_view);
            String[] parts = currentBookDetails.getmBookThumbnail().split(":");
            requiredUrl = parts[0] + "s:" + parts[1];
            Picasso.get().load(requiredUrl).placeholder(R.drawable.placeholder).into(bookImage);
        }
        TextView titleOfBookView = (TextView) listView.findViewById(R.id.title_of_book_text_view);
        titleOfBookView.setText(currentBookDetails.getmBookTitle());

        TextView authorOfBookView = (TextView) listView.findViewById(R.id.author_of_book_text_view);
        authorOfBookView.setText(currentBookDetails.getmBookAuthor());

        TextView descriptionOfBookView = (TextView) listView.findViewById(R.id.description_of_book_text_view);
        descriptionOfBookView.setText(currentBookDetails.getmBookDescription());

        TextView publishedDateView = (TextView) listView.findViewById(R.id.published_date_text_view);
        publishedDateView.setText(currentBookDetails.getmBookPublishedDate());


        return listView;

    }


}

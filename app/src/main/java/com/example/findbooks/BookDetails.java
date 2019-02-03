package com.example.findbooks;


public class BookDetails {

    private String mUrl;
    private String mBookThumbnail;
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookDescription;
    private String mBookPublishedDate;


    public BookDetails(String bookThumbnail, String bookTitle, String bookAuthor, String bookDescription, String bookPublishedDate, String url) {
        mBookThumbnail = bookThumbnail;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookDescription = bookDescription;
        mBookPublishedDate = bookPublishedDate;
        mUrl = url;
    }


    public String getmBookThumbnail() {
        return mBookThumbnail;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    public String getmBookDescription() {
        return mBookDescription;
    }

    public String getmBookPublishedDate() {
        return mBookPublishedDate;
    }

    public String getmUrl() {
        return mUrl;
    }
}

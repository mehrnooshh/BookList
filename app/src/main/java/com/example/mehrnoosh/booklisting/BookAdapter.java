package com.example.mehrnoosh.booklisting;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Mehrnoosh on 12/14/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private class MyAsyncTask extends AsyncTask<Void, Void, Drawable> {

        private String thumbnailUrl;
        private TextView rowTextView;

        public MyAsyncTask(String thumbnailUrl, TextView rowTextView) {
            this.thumbnailUrl = thumbnailUrl;
            this.rowTextView = rowTextView;
        }

        @Override
        protected Drawable doInBackground(Void... v) {
            try {
                    /* Open a new URL and get the InputStream to load data from it. */
                URL aURL = new URL(thumbnailUrl);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                    /* Buffered is always good for a performance plus. */
                BufferedInputStream bis = new BufferedInputStream(is);
                    /* Decode url-data to a bitmap. */
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();

                return new BitmapDrawable(bm);
                //d.setId("1");
                //textview.setCompoundDrawablesWithIntrinsicBounds(0,0,1,0);// wherever u want the image relative to textview
            } catch (IOException e) {
                Log.e("DEBUGTAG", "Remote Image Exception", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            rowTextView.setBackground(drawable);
        }
    }

    public BookAdapter(Context context, List<Book> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        // Find the book at the given position in the list of books
        final Book currentBook = getItem(position);

        // Find the TextView with view ID row
        TextView rowView = (TextView) listItemView.findViewById(R.id.row);
        // Display the row of the current book in that TextView
        rowView.setText(String.valueOf(position+1));

        fetchThumbnail(currentBook.getThumbnailUrl(), rowView);

        // Find the TextView with view ID publisher
        TextView publisherView = (TextView) listItemView.findViewById(R.id.publisher);
        // Display the publisher of the current book in that TextView
        publisherView.setText(currentBook.getPublisher());

        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        // Display the title of the current book in that TextView
        titleView.setText(currentBook.getTitle());

        // Find the TextView with view ID ISBN
        TextView googleIDView = (TextView) listItemView.findViewById(R.id.google_id);
        // Display the ISBN of the current book in that TextView
        googleIDView.setText(currentBook.getGoogleId());

        // Create a new Date object from the time in milliseconds of the book
        //Date dateObject = new Date(currentBook.getDate());
        // Find the TextView with view ID date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        // Display the date of the current book in that TextView
        //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        //dateTextView.setText(dateFormat.format(dateObject));
        dateTextView.setText(currentBook.getDate());
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    private void fetchThumbnail(String thumbnailUrl, TextView rowTextView) {
        MyAsyncTask asyncTask = new MyAsyncTask(thumbnailUrl, rowTextView);
        asyncTask.execute();
    }
}
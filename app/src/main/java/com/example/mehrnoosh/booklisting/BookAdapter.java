package com.example.mehrnoosh.booklisting;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mehrnoosh on 12/14/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {
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
}
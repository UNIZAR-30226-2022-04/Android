package es.unizar.eina.frankenstory.story;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.story.AsyncTaskGetParagraphs;
import es.unizar.eina.frankenstory.story.VoteStoryActivity;

public class ListParagraphsAdapter extends BaseAdapter {


    private VoteStoryActivity context; //context
    private List<AsyncTaskGetParagraphs.Paragraph> items; //data source of the list adapter

    //public constructor
    public ListParagraphsAdapter(VoteStoryActivity context, List<AsyncTaskGetParagraphs.Paragraph> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_paragraphs_vote, parent, false);
        }

        // get current item to be displayed
        String currentItem = (String) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.body);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem);

        // returns the view for the current row
        return convertView;
    }
}

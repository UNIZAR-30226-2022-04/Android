package es.unizar.eina.frankenstory.story;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;

public class ListPreviousContentAdapter extends BaseAdapter{

    private StoryNotFirstWriteActivity context; //context
    private List<AsyncTaskResumeStory.Paragraph> items; //data source of the list adapter

    //public constructor
    public ListPreviousContentAdapter(StoryNotFirstWriteActivity context, List<AsyncTaskResumeStory.Paragraph> items) {
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
                    inflate(R.layout.row_previous_paragraphs, parent, false);
        }

        // get current item to be displayed
        AsyncTaskResumeStory.Paragraph currentItem = (AsyncTaskResumeStory.Paragraph) getItem(position);

        // get VIEWS
        TextView textContent = (TextView) convertView.findViewById(R.id.body);
        TextView textAutor = (TextView) convertView.findViewById(R.id.autor);

        // set DATA
        textContent.setText(currentItem.text);
        textAutor.setText(currentItem.username);

        // returns the view for the current row
        return convertView;
    }

}

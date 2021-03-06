package es.unizar.eina.frankenstory.general;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.general.AsyncTaskGetStories;
import es.unizar.eina.frankenstory.general.LibraryActivity;

public class ListLibraryTaleGamesAdapter extends BaseAdapter{

    private LibraryActivity context; //context
    private List<AsyncTaskGetStories.Story> items; //data source of the list adapter

    //public constructor
    public ListLibraryTaleGamesAdapter(LibraryActivity context, List<AsyncTaskGetStories.Story> items) {
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
        // get current item to be displayed
        AsyncTaskGetStories.Story currentItem = (AsyncTaskGetStories.Story) getItem(position);

        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_story_library, parent, false);
        }

        // get VIEWS
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.titleStory);
        TextView textViewDate = (TextView) convertView.findViewById(R.id.date);
        // set DATA
        textViewItemName.setText(currentItem.title);
        textViewDate.setText(currentItem.date);

        ImageButton read_story = (ImageButton) convertView.findViewById(R.id.read_story);
        read_story.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, LibraryStoryActivity.class);
                i.putExtra("title", currentItem.title);
                i.putExtra("id", currentItem.id.toString());
                i.putExtra("type","tale");
                context.startActivity(i);
            }
        });

        // returns the view for the current row
        return convertView;
    }

}

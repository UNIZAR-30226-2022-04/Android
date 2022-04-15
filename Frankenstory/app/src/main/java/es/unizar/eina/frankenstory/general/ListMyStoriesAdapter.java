package es.unizar.eina.frankenstory.general;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.frankenstory.R;

public class ListMyStoriesAdapter extends BaseAdapter{

    private StoryActivity context; //context
    private List<AsyncTaskStories.Story> items; //data source of the list adapter

    //public constructor
    public ListMyStoriesAdapter(StoryActivity context, List<AsyncTaskStories.Story> items) {
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
                    inflate(R.layout.row_story_games, parent, false);
        }

        // get current item to be displayed
        AsyncTaskStories.Story currentItem = (AsyncTaskStories.Story) getItem(position);

        // get VIEWS
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.titleStory);
        TextView textViewItemCreator = (TextView) convertView.findViewById(R.id.creator);
        TextView textViewItemNumberTurns = (TextView) convertView.findViewById(R.id.numberTurns);

        // set DATA
        textViewItemName.setText(currentItem.title);
        textViewItemCreator.setText(currentItem.creator);
        textViewItemNumberTurns.setText(currentItem.turn.toString()+"/"+currentItem.max_turns.toString());

        // ON CLICK ON VOTE GAME
        ImageButton vote = (ImageButton)
                convertView.findViewById(R.id.joinGame);
        vote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StoryFirstWriteActivity.class);
                i.putExtra("myStory", true);
                i.putExtra("id", position);
                context.startActivity(i);
            }
        });

        // returns the view for the current row
        return convertView;
    }

}

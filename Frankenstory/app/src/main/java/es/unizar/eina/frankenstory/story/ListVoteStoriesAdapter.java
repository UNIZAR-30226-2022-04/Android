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
import es.unizar.eina.frankenstory.story.AsyncTaskStories;
import es.unizar.eina.frankenstory.story.StoryActivity;
import es.unizar.eina.frankenstory.story.VoteStoryActivity;

public class ListVoteStoriesAdapter extends BaseAdapter{

    private StoryActivity context; //context
    private List<AsyncTaskStories.Story> items; //data source of the list adapter

    //public constructor
    public ListVoteStoriesAdapter(StoryActivity context, List<AsyncTaskStories.Story> items) {
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
                    inflate(R.layout.row_story_vote, parent, false);
        }

        // get current item to be displayed
        AsyncTaskStories.Story currentItem = (AsyncTaskStories.Story) getItem(position);

        // get VIEWS
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.titleStory);
        TextView textViewItemCreator = (TextView) convertView.findViewById(R.id.creator);

        // set DATA
        textViewItemName.setText(currentItem.title);
        textViewItemCreator.setText(currentItem.creator);

        // ON CLICK ON BUTTON JOIN GAME
        ImageButton joinGame = (ImageButton)
                convertView.findViewById(R.id.vote);
        joinGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), VoteStoryActivity.class);
                context.startActivity(i);
            }
        });

        // returns the view for the current row
        return convertView;
    }

}

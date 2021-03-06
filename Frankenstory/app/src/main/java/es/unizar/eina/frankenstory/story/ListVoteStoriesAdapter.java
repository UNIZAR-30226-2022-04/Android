package es.unizar.eina.frankenstory.story;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        TextView textVote = (TextView) convertView.findViewById(R.id.voteText);
        ImageView imageVote = (ImageView) convertView.findViewById(R.id.vote);

        // set DATA
        textViewItemName.setText(currentItem.title);
        textViewItemCreator.setText(currentItem.creator);

        if (currentItem.meVoted) {
            imageVote.setImageResource(R.drawable.wait);
            textVote.setTextSize(9);
            textVote.setText("ESPERANDO");
            imageVote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), VoteStoryActivity.class);
                    i.putExtra("id", currentItem.story_id.toString());
                    i.putExtra("alreadyVoted", currentItem.meVoted);
                    context.startActivity(i);
                }
            });
        } else {
            // ON CLICK ON BUTTON VOTE GAME
            imageVote.setImageResource(R.drawable.vote);
            textVote.setTextSize(12);
            textVote.setText("VOTAR");
            imageVote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), VoteStoryActivity.class);
                    i.putExtra("id", currentItem.story_id.toString());
                    i.putExtra("alreadyVoted", currentItem.meVoted);
                    context.startActivity(i);
                }
            });
        }

        // returns the view for the current row
        return convertView;
    }

}

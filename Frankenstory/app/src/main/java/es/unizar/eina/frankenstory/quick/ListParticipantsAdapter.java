package es.unizar.eina.frankenstory.quick;

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
import es.unizar.eina.frankenstory.story.AsyncTaskStories;
import es.unizar.eina.frankenstory.story.StoryActivity;
import es.unizar.eina.frankenstory.story.StoryNotFirstWriteActivity;

public class ListParticipantsAdapter extends BaseAdapter{

    private QuickGameRoom context; //context
    private List<AsyncTaskGetRoom.Participants> items; //data source of the list adapter

    //public constructor
    public ListParticipantsAdapter(QuickGameRoom context, List<AsyncTaskGetRoom.Participants> items) {
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
                    inflate(R.layout.row_participants, parent, false);
        }

        // get current item to be displayed
        AsyncTaskGetRoom.Participants currentItem = (AsyncTaskGetRoom.Participants) getItem(position);

        // get VIEWS
        ImageView playerImage = (ImageView) convertView.findViewById(R.id.playerImage);
        TextView playerName = (TextView) convertView.findViewById(R.id.playerName);
        TextView playerStars = (TextView) convertView.findViewById(R.id.playerStars);

        // set DATA
        if (currentItem.picture == 0) playerImage.setImageResource(R.raw.icon0);
        else if (currentItem.picture == 1) playerImage.setImageResource(R.raw.icon1);
        else if (currentItem.picture == 2) playerImage.setImageResource(R.raw.icon2);
        else if (currentItem.picture == 3) playerImage.setImageResource(R.raw.icon3);
        else if (currentItem.picture == 4) playerImage.setImageResource(R.raw.icon4);
        else if (currentItem.picture == 5) playerImage.setImageResource(R.raw.icon5);
        else if (currentItem.picture == 6) playerImage.setImageResource(R.raw.icon6);
        else if (currentItem.picture == 7) playerImage.setImageResource(R.raw.icon7);
        else if (currentItem.picture == 8) playerImage.setImageResource(R.raw.icon8);
        else if (currentItem.picture == 9) playerImage.setImageResource(R.raw.icon9);

        playerName.setText(currentItem.username);
        playerStars.setText("x" + currentItem.stars);

        // returns the view for the current row
        return convertView;
    }

}

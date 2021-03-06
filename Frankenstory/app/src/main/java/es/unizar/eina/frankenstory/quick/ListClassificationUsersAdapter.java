package es.unizar.eina.frankenstory.quick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.general.AsyncTaskMainMenu;
import es.unizar.eina.frankenstory.general.MainMenuActivity;

public class ListClassificationUsersAdapter extends BaseAdapter{

    private QuickPointsActivity context; //context
    private List<AsyncTaskPointsQuickGame.Participant> items; //data source of the list adapter

    //public constructor
    public ListClassificationUsersAdapter(QuickPointsActivity context, List<AsyncTaskPointsQuickGame.Participant> items) {
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
        AsyncTaskPointsQuickGame.Participant currentItem = (AsyncTaskPointsQuickGame.Participant) getItem(position);

        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_main_menu, parent, false);
        }

        // get VIEWS
        TextView textPosition = (TextView) convertView.findViewById(R.id.position);
        TextView textName = (TextView) convertView.findViewById(R.id.friendName);
        TextView textStars = (TextView) convertView.findViewById(R.id.friendStars);
        ImageView crown = (ImageView) convertView.findViewById(R.id.crown);

        // set DATA
        textPosition.setText((position+1)+"??");
        textName.setText(currentItem.username);
        textStars.setText("X"+currentItem.stars);
        if (position == 0) crown.setVisibility(View.VISIBLE);
        else crown.setVisibility(View.GONE);

        // returns the view for the current row
        return convertView;
    }

}

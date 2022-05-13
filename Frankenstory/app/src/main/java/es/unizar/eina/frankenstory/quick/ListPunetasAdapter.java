package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class ListPunetasAdapter extends BaseAdapter {

    private QuickPlayActivity context; //context
    private List<AsyncTaskGetRoom.Participants> items; //data source of the list adapter
    private String punetaSelected;
    private Integer mooncoinsPayed;

    //public constructor
    public ListPunetasAdapter(QuickPlayActivity context, List<AsyncTaskGetRoom.Participants> items, String punetaSelected, Integer mooncoinsPayed) {
        this.context = context;
        // CANT SEND TO MYSELF OR ANYONE WHO ALREADY HAS A PUNETA
        List<AsyncTaskGetRoom.Participants> itemsWithoutMe = new ArrayList<AsyncTaskGetRoom.Participants>();
        String me = ((MyApplication) context.getApplication()).getUsername();
        for (AsyncTaskGetRoom.Participants p : items){
            if (!p.username.equals(me) && !context.alreadyHasAPuneta(p.username)) itemsWithoutMe.add(p);
        }
        this.items = itemsWithoutMe;
        this.punetaSelected = punetaSelected;
        this.mooncoinsPayed = mooncoinsPayed;
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

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_punetas, parent, false);
        }

        // get current item to be displayed
        AsyncTaskGetRoom.Participants currentItem = (AsyncTaskGetRoom.Participants) getItem(position);

        // get VIEWS
        ImageView playerImage = (ImageView) convertView.findViewById(R.id.iconPunetaName);
        TextView playerName = (TextView) convertView.findViewById(R.id.namePunetaFriend);

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

        // On click on savePuneta
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                QuickPlayActivity.FriendPuneta newPuneta = new QuickPlayActivity.FriendPuneta();
                newPuneta.puneta = punetaSelected;
                newPuneta.username = currentItem.username;
                context.paragraphToSend.listFriendPuneta.add(newPuneta);
                context.subtractMooncoins(mooncoinsPayed);
                context.closePunetas();
            }
        });

        // returns the view for the current row
        return convertView;
    }

}

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

public class ListPetitionsAdapter extends BaseAdapter {
    private FriendsActivity context; //context
    private List<AsyncTaskFriends.Friend> items; //data source of the list adapter

    //public constructor
    public ListPetitionsAdapter(FriendsActivity context, List<AsyncTaskFriends.Friend> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public AsyncTaskFriends.Friend getItem(int position) {
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
                    inflate(R.layout.row_friend_petitions, parent, false);
        }

        // get current item to be displayed
        String currentItem = getItem(position).username;

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.friendName);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem);

        // ON CLICK ON BUTTON CANCEL PETITION
        ImageButton cancelPetition = (ImageButton)
                convertView.findViewById(R.id.cancel);
        cancelPetition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //--- CANCEL PETITION
                AsyncTaskAnswerPetition myTaskSearch = new AsyncTaskAnswerPetition(context);
                myTaskSearch.execute(currentItem, "false");
            }
        });

        // ON CLICK ON BUTTON CANCEL PETITION
        ImageButton acceptPetition = (ImageButton)
                convertView.findViewById(R.id.accept);
        acceptPetition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //--- ACCEPT PETITION
                AsyncTaskAnswerPetition myTaskSearch = new AsyncTaskAnswerPetition(context);
                myTaskSearch.execute(currentItem, "true");
            }
        });

        // returns the view for the current row
        return convertView;
    }
}

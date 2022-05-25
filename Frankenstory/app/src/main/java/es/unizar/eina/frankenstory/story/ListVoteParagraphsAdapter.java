package es.unizar.eina.frankenstory.story;

import android.content.Intent;
import android.media.Image;
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
import es.unizar.eina.frankenstory.story.AsyncTaskGetParagraphs;
import es.unizar.eina.frankenstory.story.VoteStoryActivity;

public class ListVoteParagraphsAdapter extends BaseAdapter {

    private VoteStoryActivity context; //context
    private List<AsyncTaskGetParagraphs.Paragraph> items; //data source of the list adapter

    //public constructor
    public ListVoteParagraphsAdapter(VoteStoryActivity context, List<AsyncTaskGetParagraphs.Paragraph> items) {
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

    //-------------------

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    //_--------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_paragraphs_vote, parent, false);
        }

        // get current item to be displayed
        AsyncTaskGetParagraphs.Paragraph currentItem = (AsyncTaskGetParagraphs.Paragraph) getItem(position);

        // ONLY HEART ON FIRST ONE
        ImageView heart = (ImageView) convertView.findViewById(R.id.imageVote);
        TextView textViewItem = (TextView) convertView.findViewById(R.id.body);
        if (position == context.votedParagraph){
            heart.setVisibility(View.VISIBLE);
            textViewItem.setBackgroundColor(context.getResources().getColor(R.color.verde_parrafo_seleccionado));
            context.selectedView = convertView;
            Log.d("PUESTO EL VOTADO",position+"-"+currentItem.text+"-"+currentItem.username);
        } else {
            heart.setVisibility(View.GONE);
            textViewItem.setBackgroundColor(context.getResources().getColor(R.color.verde_parrafo));
            Log.d("NO EL VOTADO",position+"-"+currentItem.text+"-"+currentItem.username);
        }
        /*
        if (context.selectedView == null && position == 0 && !context.alreadyVoted){
            heart.setVisibility(View.VISIBLE);
            textViewItem.setBackgroundColor(context.getResources().getColor(R.color.verde_parrafo_seleccionado));
            context.selectedView = convertView;
            Log.d("PUESTO EL PRIMERO",position+"-"+currentItem.text+"-"+currentItem.username);
        } else if (position != context.votedParagraph){
            heart.setVisibility(View.GONE);
            textViewItem.setBackgroundColor(context.getResources().getColor(R.color.verde_parrafo));
            Log.d("NO EL PRIMERO",position+"-"+currentItem.text+"-"+currentItem.username);
        }*/

        // get VIEWS
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.body);
        TextView textViewItemCreator = (TextView) convertView.findViewById(R.id.autor);

        // set DATA
        textViewItemName.setText(currentItem.text);
        textViewItemCreator.setText(currentItem.username);

        // returns the view for the current row
        return convertView;
    }

}

package es.unizar.eina.frankenstory.quick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.story.AsyncTaskGetParagraphs;
import es.unizar.eina.frankenstory.story.VoteStoryActivity;

public class ListQuickVotedParagraphsAdapter extends BaseAdapter {

    private QuickVotedActivity context; //context
    private List<AsyncTaskResumeQuickVoted.Paragraph> items; //data source of the list adapter

    //public constructor
    public ListQuickVotedParagraphsAdapter(QuickVotedActivity context, List<AsyncTaskResumeQuickVoted.Paragraph> items) {
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
                    inflate(R.layout.row_quick_paragraphs_voted, parent, false);
        }

        // get current item to be displayed
        AsyncTaskResumeQuickVoted.Paragraph currentItem = (AsyncTaskResumeQuickVoted.Paragraph) getItem(position);

        // ONLY CROWN ON WINNER
        ImageView crown = (ImageView) convertView.findViewById(R.id.imageVote);
        if (context.winner == position){
            crown.setVisibility(View.VISIBLE);
        }else {
            crown.setVisibility(View.GONE);
        }

        // get VIEWS
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.body);
        TextView textViewItemCreator = (TextView) convertView.findViewById(R.id.autor);

        // set DATA
        textViewItemName.setText(currentItem.body);
        textViewItemCreator.setText(currentItem.username);

        // returns the view for the current row
        return convertView;
    }

}

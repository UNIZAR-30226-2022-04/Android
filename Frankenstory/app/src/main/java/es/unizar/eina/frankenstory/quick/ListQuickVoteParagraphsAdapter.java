package es.unizar.eina.frankenstory.quick;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.story.AsyncTaskGetParagraphs;
import es.unizar.eina.frankenstory.story.VoteStoryActivity;

public class ListQuickVoteParagraphsAdapter extends BaseAdapter {

    private QuickVoteActivity context; //context
    private List<AsyncTaskResumeQuickVote.Paragraph> items; //data source of the list adapter

    //public constructor
    public ListQuickVoteParagraphsAdapter(QuickVoteActivity context, List<AsyncTaskResumeQuickVote.Paragraph> items) {
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
                    inflate(R.layout.row_quick_paragraphs_vote, parent, false);
        }

        // ONLY HEART ON FIRST ONE
        ImageView heart = (ImageView) convertView.findViewById(R.id.imageVote);
        if (position == 0){
            heart.setVisibility(View.VISIBLE);
            TextView textViewItem = (TextView) convertView.findViewById(R.id.body);
            textViewItem.setBackgroundColor(context.getResources().getColor(R.color.verde_parrafo_seleccionado));
            context.selectedView = convertView;
        } else heart.setVisibility(View.GONE);

        // get current item to be displayed
        AsyncTaskResumeQuickVote.Paragraph currentItem = (AsyncTaskResumeQuickVote.Paragraph) getItem(position);

        // get VIEWS
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.body);

        //SET COLOURS TO WORDS
        String content = currentItem.body;

        content = content.replace(currentItem.words.get(0),
                "<font color='#0ca789'>" + currentItem.words.get(0) + "</font>");
        content = content.replace(currentItem.words.get(0).toLowerCase(),
                "<font color='#0ca789'>" + currentItem.words.get(0) + "</font>");

        content = content.replace(currentItem.words.get(1),
                "<font color='#0ca789'>" + currentItem.words.get(1) + "</font>");
        content = content.replace(currentItem.words.get(1).toLowerCase(),
                "<font color='#0ca789'>" + currentItem.words.get(1) + "</font>");

        content = content.replace(currentItem.words.get(2),
                "<font color='#0ca789'>" + currentItem.words.get(2) + "</font>");
        content = content.replace(currentItem.words.get(2).toLowerCase(),
                "<font color='#0ca789'>" + currentItem.words.get(2) + "</font>");

        // set DATA
        textViewItemName.setText(Html.fromHtml(content));

        // returns the view for the current row
        return convertView;
    }

}

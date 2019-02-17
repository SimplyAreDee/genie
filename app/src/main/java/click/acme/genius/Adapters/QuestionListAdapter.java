package click.acme.genius.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import click.acme.genius.Models.Question;
import click.acme.genius.R;
import click.acme.genius.Views.QuestionListItemViewHolder;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListItemViewHolder> {

    private ArrayList<Question> mDataSet;

    public QuestionListAdapter(ArrayList<Question> questions) {
        mDataSet = questions;
    }

    @NonNull
    @Override
    public QuestionListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_layout_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListItemViewHolder holder, int position) {
        holder.updateWithData(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

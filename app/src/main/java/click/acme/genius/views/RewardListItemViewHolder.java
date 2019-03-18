package click.acme.genius.views;

import android.view.View;
import android.widget.ImageView;
import com.google.firebase.Timestamp;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import click.acme.genius.helpers.RewardHelper;
import click.acme.genius.models.Reward;
import click.acme.genius.R;

public class RewardListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_layout_reward_item_imageview)
    ImageView mImageView;

    public RewardListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Reward reward) {
        int badgeIdentifier = RewardHelper.getRessouceId(reward.getRessourceId());
        mImageView.setImageResource( badgeIdentifier );
    }

    public static String getStringValueOfElapsedTimeSince(Timestamp date){
        if(date != null){
            long diff = Timestamp.now().toDate().getTime() - date.toDate().getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if(days >= 1){
                return String.valueOf(days) + " jrs";
            }

            if(hours >= 1){
                return String.valueOf(hours) + " hrs";
            }

            return String.valueOf(minutes) + " min";
        }

        return "";
    }
}

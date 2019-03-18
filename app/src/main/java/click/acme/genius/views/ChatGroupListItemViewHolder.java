package click.acme.genius.views;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import click.acme.genius.models.Chat;
import click.acme.genius.R;

public class ChatGroupListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_layout_chat_group_item_textview)
    TextView mTitleView;
    @BindView(R.id.fragment_layout_chat_group_item_textview_badge)
    TextView mBadgeCountView;
    @BindView(R.id.fragment_layout_chat_group_item_textContainer)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.fragment_layout_chat_group_item_imageView)
    ImageView mImageView;
    @BindView(R.id.fragment_layout_chat_group_item_cardView)
    CardView mCardView;

    public ChatGroupListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Chat chat) {
        mTitleView.setText(chat.getChatShortcutName());

        int textViewHeight = mTitleView.getHeight();

        tuneCardView(mCardView,textViewHeight,textViewHeight);

        mBadgeCountView.setText(String.valueOf( chat.getMessageCount() ));

        if(chat.getChatShortcutImageUrl() != null && !chat.getChatShortcutImageUrl().isEmpty()){
            Glide.with(mImageView.getContext())
                    .load(chat.getChatShortcutImageUrl())
                    .into(mImageView);
        }else{
            mTitleView.setText(chat.getChatShortcutName());
        }

        colorizeRandomlyCell(mTitleView);
    }

    private CardView tuneCardView(CardView cardView,int layoutHeight,int layoutWidth){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(layoutHeight, layoutWidth);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(layoutHeight / 2);

        return cardView;
    }

    private TextView colorizeRandomlyCell(TextView textView){
        Random rnd = new Random();

        int r = rnd.nextInt(256);
        int g = rnd.nextInt(256);
        int b = rnd.nextInt(256);

        int color = Color.argb(255,r,g,b);

        textView.setBackgroundColor(color);

        int brightness = (r * 299 + g * 587 + b * 114) / 1000;

        if (brightness < 123) {
            textView.setTextColor(Color.argb(255,255,255,255));
        } else {
            textView.setTextColor(Color.argb(255,0,0,0));
        }

        return textView;
    }
}

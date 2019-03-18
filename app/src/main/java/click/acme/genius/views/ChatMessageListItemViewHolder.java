package click.acme.genius.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import click.acme.genius.models.ChatMessage;
import click.acme.genius.models.User;
import click.acme.genius.R;

public class ChatMessageListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_layout_chat_detail_item_rootview)
    ConstraintLayout mRootView;
    @BindView(R.id.fragment_layout_chat_detail_item_profile_container_profile_imageContainer)
    RelativeLayout mProfileContainer;
    @BindView(R.id.fragment_layout_chat_detail_item_profile_container_profile_image)
    ImageView mImageViewProfile;
    @BindView(R.id.fragment_layout_chat_detail_item_profile_container_is_certified_image)
    ImageView mImageViewIsCertified;
    @BindView(R.id.fragment_layout_chat_detail_item_message_container_image_sent_cardview)
    CardView mCardViewImageSent;
    @BindView(R.id.fragment_layout_chat_detail_item_message_container_image_sent_cardview_image)
    ImageView mImageViewSent;
    @BindView(R.id.fragment_layout_chat_detail_item_message_container_text_message_container_text_view)
    TextView mTextViewMessage;
    @BindView(R.id.fragment_layout_chat_detail_item_message_container_text_view_date)
    TextView mTextViewDate;

    private final int colorCurrentUser;
    private final int colorRemoteUser;

    public ChatMessageListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.colorAccent);
        colorRemoteUser = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
    }

    public void updateWithData(ChatMessage message, RequestManager glide) {

        boolean isCurrentUser = message.isSenderIsMe();

        this.mTextViewMessage.setText(message.getContent());
        this.mTextViewMessage.setTextAlignment(isCurrentUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

        if(isCurrentUser){
            glide.load(User.getCurrentUser().getAvatarUrl()).into(mImageViewProfile);
        }else{
            //int w = 150, h = 150;

            //Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            //Bitmap bitmap = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
            ///Canvas canvas = new Canvas(bitmap);
        }

        if(message.getDateCreated() != null){
           this.mTextViewDate.setText(message.getDateCreated());
        }

        this.mImageViewIsCertified.setVisibility(message.isFromCertifiedProfil() ? View.VISIBLE : View.INVISIBLE);


        // Update all views alignment depending is current user or not
        this.updateDesignDependingSender(isCurrentUser);
    }

    private void updateDesignDependingSender(Boolean isSender){
        // PROFILE CONTAINER

    }

}

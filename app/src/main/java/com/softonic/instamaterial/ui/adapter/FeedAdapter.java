package com.softonic.instamaterial.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.ui.model.FeedItem;
import com.softonic.instamaterial.ui.view.LoadingFeedItemView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import static com.softonic.instamaterial.ui.activity.main.MainPresenter.LIKE_REMOTE_DISLIKE;
import static com.softonic.instamaterial.ui.activity.main.MainPresenter.LIKE_REMOTE_LIKE;
import static com.softonic.instamaterial.ui.activity.main.MainPresenter.LIKE_TAP_LIKED;
import static com.softonic.instamaterial.ui.activity.main.MainPresenter.LIKE_TAP_SOURCE_BUTTON;
import static com.softonic.instamaterial.ui.activity.main.MainPresenter.LIKE_TAP_SOURCE_IMAGE;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int VIEW_TYPE_DEFAULT = 1;
  public static final int VIEW_TYPE_LOADER = 2;

  private final String userId;
  private final List<FeedItem> feedItems = new ArrayList<>();

  private Context context;
  private OnFeedItemClickListener onFeedItemClickListener;

  private boolean showLoadingView = false;

  public FeedAdapter(Context context, String userId) {
    this.context = context;
    this.userId = userId;
  }

  public List<FeedItem> getItems() {
    return feedItems;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_DEFAULT) {
      View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
      CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
      setupClickableViews(view, cellFeedViewHolder);
      return cellFeedViewHolder;
    } else if (viewType == VIEW_TYPE_LOADER) {
      LoadingFeedItemView view = new LoadingFeedItemView(context);
      view.setLayoutParams(new LinearLayoutCompat.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT)
      );
      return new LoadingCellFeedViewHolder(view);
    }

    return null;
  }

  private void setupClickableViews(final View view, final CellFeedViewHolder cellFeedViewHolder) {
    cellFeedViewHolder.btnComments.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onFeedItemClickListener.onCommentsClick(view,
            feedItems.get(cellFeedViewHolder.getAdapterPosition()));
      }
    });
    cellFeedViewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int adapterPosition = cellFeedViewHolder.getAdapterPosition();
        FeedItem feedItem = feedItems.get(adapterPosition);
        onFeedItemClickListener.onLikeClick(feedItem, LIKE_TAP_SOURCE_IMAGE);
      }
    });
    cellFeedViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int adapterPosition = cellFeedViewHolder.getAdapterPosition();
        FeedItem feedItem = feedItems.get(adapterPosition);
        onFeedItemClickListener.onLikeClick(feedItem, LIKE_TAP_SOURCE_BUTTON);
      }
    });
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    ((CellFeedViewHolder) viewHolder).bindView(feedItems.get(position), userId);

    if (getItemViewType(position) == VIEW_TYPE_LOADER) {
      bindLoadingFeedItem((LoadingCellFeedViewHolder) viewHolder);
    }
  }

  private void bindLoadingFeedItem(final LoadingCellFeedViewHolder holder) {
    holder.loadingFeedItemView.setOnLoadingFinishedListener(
        new LoadingFeedItemView.OnLoadingFinishedListener() {
          @Override
          public void onLoadingFinished() {
            showLoadingView = false;
            notifyDataSetChanged();
          }
        });
    holder.loadingFeedItemView.startLoading();
  }

  @Override
  public int getItemViewType(int position) {
    if (showLoadingView && position == 0) {
      return VIEW_TYPE_LOADER;
    } else {
      return VIEW_TYPE_DEFAULT;
    }
  }

  @Override
  public int getItemCount() {
    return feedItems.size();
  }

  public void addAll(List<FeedItem> feedItems, boolean animated) {
    this.feedItems.addAll(feedItems);
    if (animated) {
      notifyItemRangeInserted(0, feedItems.size());
    } else {
      notifyDataSetChanged();
    }
  }

  public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
    this.onFeedItemClickListener = onFeedItemClickListener;
  }

  public void addLoadingView(FeedItem feedItem) {
    if (getItemPosition(feedItem.getPhotoId()) == -1) {
      showLoadingView = true;
      feedItems.add(0, feedItem);
      notifyItemInserted(0);
    }
  }

  public void updatePhotoLike(String photoId, String userId, int likeTapFlags) {
    int currentPosition = getItemPosition(photoId);
    if (currentPosition != -1) {
      FeedItem feedItem = feedItems.get(currentPosition);
      if ((likeTapFlags & LIKE_TAP_LIKED) != 0) {
        feedItem.getUserLikes().add(userId);
      } else {
        feedItem.getUserLikes().remove(userId);
      }
      notifyItemChanged(currentPosition, likeTapFlags);
    }
  }

  public void updatePhotoLikeCounter(String photoId, String userId, int likeTapFlags) {
    int currentPosition = getItemPosition(photoId);
    if (currentPosition != -1) {
      FeedItem feedItem = feedItems.get(currentPosition);
      if ((likeTapFlags & LIKE_REMOTE_LIKE) != 0) {
        feedItem.getUserLikes().add(userId);
        notifyItemChanged(currentPosition, likeTapFlags);
      } else if ((likeTapFlags & LIKE_REMOTE_DISLIKE) != 0) {
        feedItem.getUserLikes().remove(userId);
        notifyItemChanged(currentPosition, likeTapFlags);
      }
    }
  }

  private int getItemPosition(String photoId) {
    int position = -1;
    for (int i = 0; i < feedItems.size() && position == -1; i++) {
      FeedItem feedItem = feedItems.get(i);
      if (feedItem.getPhotoId().equals(photoId)) {
        position = i;
      }
    }
    return position;
  }

  public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivUserAvatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.btnComments)
    ImageButton btnComments;
    @BindView(R.id.btnLike)
    ImageButton btnLike;
    @BindView(R.id.vBgLike)
    View vBgLike;
    @BindView(R.id.ivLike)
    ImageView ivLike;
    @BindView(R.id.tsLikesCounter)
    TextSwitcher tsLikesCounter;
    @BindView(R.id.vImageRoot)
    FrameLayout vImageRoot;

    FeedItem feedItem;

    public CellFeedViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    public void bindView(FeedItem feedItem, String userId) {
      this.feedItem = feedItem;
      Picasso.with(itemView.getContext())
          .load(Uri.parse(feedItem.getUserAvatarUrl()))
          .into(ivUserAvatar);
      tvUserName.setText(feedItem.getUserNickname());
      Picasso.with(itemView.getContext())
          .load(Uri.parse(feedItem.getPhotoSourceUrl()))
          .into(ivPhoto);
      if (feedItem.getPhotoDescription().isEmpty()) {
        tvDescription.setVisibility(View.GONE);
      } else {
        tvDescription.setVisibility(View.VISIBLE);
        tvDescription.setText(
            getStylizedText(feedItem.getUserNickname(), feedItem.getPhotoDescription()));
      }

      List<String> userLikes = feedItem.getUserLikes();
      int likesCount = userLikes != null ? userLikes.size() : 0;
      btnLike.setImageResource(
          userLikes != null && userLikes.contains(userId) ? R.drawable.ic_heart_red
              : R.drawable.ic_heart_outline_grey);
      tsLikesCounter.setCurrentText(
          itemView.getResources().getQuantityString(R.plurals.likes_count, likesCount, likesCount));
    }

    private Spannable getStylizedText(String username, String description) {
      Spannable descriptionSpan = new SpannableString(username + " " + description);

      descriptionSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#21425D")), 0,
          username.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      descriptionSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
          username.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      return descriptionSpan;
    }

    public FeedItem getFeedItem() {
      return feedItem;
    }
  }

  public static class LoadingCellFeedViewHolder extends CellFeedViewHolder {

    LoadingFeedItemView loadingFeedItemView;

    public LoadingCellFeedViewHolder(LoadingFeedItemView view) {
      super(view);
      this.loadingFeedItemView = view;
    }

    @Override
    public void bindView(FeedItem feedItem, String userId) {
      super.bindView(feedItem, userId);
    }
  }

  public interface OnFeedItemClickListener {
    void onLikeClick(FeedItem feedItem, int likeSource);

    void onCommentsClick(View v, FeedItem feedItem);
  }
}

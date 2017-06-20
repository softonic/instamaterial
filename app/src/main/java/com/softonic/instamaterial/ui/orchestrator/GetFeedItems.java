package com.softonic.instamaterial.ui.orchestrator;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.interactors.GetPhotos;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.ui.model.FeedItem;
import java.util.LinkedList;
import java.util.List;

public class GetFeedItems extends UseCase<String, List<FeedItem>> {
  private final GetPhotos getPhotos;
  private final GetFeedItem getFeedItem;

  public GetFeedItems(UseCaseExecutor useCaseExecutor, GetPhotos getPhotos,
      GetFeedItem getFeedItem) {
    super(useCaseExecutor);
    this.getPhotos = getPhotos;
    this.getFeedItem = getFeedItem;
  }

  @Override public ObservableTask<List<FeedItem>> createObservableTask(String input) {
    return new ObservableTask<List<FeedItem>>() {
      @Override public void run(Subscriber<List<FeedItem>> result) {
        List<FeedItem> feedItems = new LinkedList<>();
        List<Photo> photos = getPhotos.createObservableTask(null).getResult();

        for (Photo photo : photos) {
          FeedItem feedItem = getFeedItem.createObservableTask(photo).getResult();
          if (feedItem != null) {
            feedItems.add(feedItem);
          }
        }

        result.onSuccess(feedItems);
      }
    };
  }
}

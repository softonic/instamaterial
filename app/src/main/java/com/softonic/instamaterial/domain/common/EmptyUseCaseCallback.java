package com.softonic.instamaterial.domain.common;

public class EmptyUseCaseCallback<R> implements UseCaseCallback<R> {
  @Override public void onSuccess(R result) {

  }

  @Override public void onError(Exception exception) {

  }
}

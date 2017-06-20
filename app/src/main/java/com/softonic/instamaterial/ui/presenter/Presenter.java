package com.softonic.instamaterial.ui.presenter;

public class Presenter<T extends Presenter.View> {
  protected T view;

  public void attach(T view) {
    this.view = view;
  }

  public interface View {

  }
}

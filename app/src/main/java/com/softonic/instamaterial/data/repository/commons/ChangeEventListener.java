package com.softonic.instamaterial.data.repository.commons;

import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public abstract class ChangeEventListener<T> implements ChildEventListener {
  private static final String TAG = "ChangeEventListener";

  private final Class<T> dataClass;
  private boolean initialDataLoaded = false;

  public ChangeEventListener(final DatabaseReference databaseReference, Class<T> dataClass) {
    this.dataClass = dataClass;
    databaseReference.addChildEventListener(this);
    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onDataChange: ");
        databaseReference.removeEventListener(this);
        initialDataLoaded = true;
      }

      @Override public void onCancelled(DatabaseError databaseError) {
      }
    });
  }

  @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    Log.d(TAG, "onChildAdded: ");
    if (initialDataLoaded) {
      try {
        T data = dataSnapshot.getValue(dataClass);
        onChildAdded(dataSnapshot.getKey(), data);
      } catch (DatabaseException ignored) {
      }
    }
  }

  @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

  }

  @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
    Log.d(TAG, "onChildRemoved: ");
    if (initialDataLoaded) {
      try {
        T data = dataSnapshot.getValue(dataClass);
        onChildRemoved(dataSnapshot.getKey(), data);
      } catch (DatabaseException ignored) {
      }
    }
  }

  @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

  }

  @Override public void onCancelled(DatabaseError databaseError) {

  }

  protected abstract void onChildAdded(String key, T data);

  protected abstract void onChildRemoved(String key, T data);
}

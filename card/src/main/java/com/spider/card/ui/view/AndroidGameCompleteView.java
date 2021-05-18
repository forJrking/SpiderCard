package com.spider.card.ui.view;

import android.app.AlertDialog;
import android.content.Context;

import com.spider.card.R;

import com.spider.card.facade.view.GameCompleteView;

public class AndroidGameCompleteView implements GameCompleteView {

  private final Context context;

  public AndroidGameCompleteView(Context context) {
    this.context = context;
  }

  @Override
  public void show() {
    new AlertDialog.Builder(context)
        .setTitle(R.string.game_complete_dialog_title)
        .setMessage(R.string.game_complete_dialog_message)
        .show();
  }

}

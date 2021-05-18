package com.spider.card.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.spider.card.R;
import com.spider.card.facade.view.GameCompleteView;
import com.spider.card.utils.MusicPlayer;

public class AndroidGameCompleteView implements GameCompleteView {

  private final Context context;

  public AndroidGameCompleteView(Context context) {
    this.context = context;
  }

  @Override
  public void show() {
      Dialog dialog = new Dialog(context, R.style.AppTheme_Dialog);
      ImageView view = new ImageView(context);
      view.setMaxHeight(720);
      view.setMaxHeight(554);
      view.setImageResource(R.mipmap.victory_icon);
      dialog.setContentView(view);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      dialog.setCancelable(true);
      dialog.setCanceledOnTouchOutside(true);
      dialog.show();
      MusicPlayer.getInstance(context).play(R.raw.solitaire_win);
  }

}

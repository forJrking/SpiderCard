package com.spider.card;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spider.card.business.SpiderSolitaire;
import com.spider.card.business.SpiderSolitaires;
import com.spider.card.facade.presenter.SpiderSolitairePresenter;
import com.spider.card.ui.view.AndroidDrawingCardsView;
import com.spider.card.ui.view.AndroidGameCompleteView;
import com.spider.card.ui.view.AndroidGameScoreView;
import com.spider.card.ui.view.AndroidSortedCardsView;
import com.spider.card.ui.view.AndroidSpiderSolitaireView;
import com.spider.card.utils.KeyValueUtil;
import com.spider.card.utils.MusicPlayer;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class MainActivity extends AppCompatActivity {

    private final Subject<Menu, Menu> createOptionsMenuEvents = PublishSubject.create();
    private AndroidSpiderSolitaireView spiderSolitaireView;
    private TextView timingTv, menuTv, replyTv;
    private Subscription subscription;
    private AndroidGameScoreView stepsTv;

    public Observable<Menu> getCreateOptionsMenuEvents() {
        return createOptionsMenuEvents.asObservable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spiderSolitaireView = findViewById(R.id.spiderSolitaireView);
        timingTv = findViewById(R.id.timing_tv);
        stepsTv = findViewById(R.id.steps_tv);
        menuTv = findViewById(R.id.menu_tv);
        replyTv = findViewById(R.id.reply_tv);
        menuTv.setOnClickListener(v -> {
            showMenu();
        });
        // DES：开始计时和结束游戏
        replyTv.setOnClickListener(v -> {
            boolean back = spiderSolitaireView != null && !spiderSolitaireView.onBackPressed();
        });
        newGame();
    }

    private void newGame() {
        int level = KeyValueUtil.getLevel();
        SpiderSolitaire solitaire = SpiderSolitaires.newGame(level);
        AndroidDrawingCardsView drawingCardsView = new AndroidDrawingCardsView(this);
        AndroidSortedCardsView sortedCardsView = new AndroidSortedCardsView(this);
        AndroidGameCompleteView gameCompleteView = new AndroidGameCompleteView(this);
        new SpiderSolitairePresenter(
                solitaire, spiderSolitaireView, drawingCardsView, sortedCardsView, stepsTv, gameCompleteView
        );
        startTiming();
        MusicPlayer.getInstance(this).play(R.raw.solitaire_deck);
    }

    private void startTiming() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    timingTv.setText(showTime(aLong));
                });
    }

    private String showTime(long iTime) {
        String str = "";
        long s = iTime % 60;
        long m = (((iTime - s) / 60) % 60);
        long h = ((iTime - s) / 3600);
        str += (h >= 10 ? h + ":" : h > 0 ? "0" + h + ":" : "00:");
        str += (m >= 10 ? m + ":" : m > 0 ? "0" + m + ":" : "00:");
        str += (s >= 10 ? s : s > 0 ? "0" + s : "00");
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private Button zeroBtn, oneBtn, twoBtn, fourBtn;

    private void showMenu() {
        Dialog dialog = new Dialog(this, R.style.AppTheme_Dialog);
        dialog.setContentView(R.layout.dialog_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.exit_btn).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        dialog.findViewById(R.id.new_btn).setOnClickListener(v -> {
            dialog.dismiss();
            newGame();
        });
        dialog.findViewById(R.id.help_btn).setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "帮助", Toast.LENGTH_LONG).show();
        });
        zeroBtn = dialog.findViewById(R.id.zero_btn);
        oneBtn = dialog.findViewById(R.id.one_btn);
        twoBtn = dialog.findViewById(R.id.two_btn);
        fourBtn = dialog.findViewById(R.id.four_btn);
        int level = KeyValueUtil.getLevel();
        switch (level) {
            case 0:
                zeroBtn.setEnabled(false);
                break;
            case 2:
                twoBtn.setEnabled(false);
                break;
            case 4:
                fourBtn.setEnabled(false);
                break;
            default:
                oneBtn.setEnabled(false);
                break;
        }
        zeroBtn.setOnClickListener(v -> {
            doLevel(0);
            oneBtn.setEnabled(true);
            twoBtn.setEnabled(true);
            fourBtn.setEnabled(true);
            zeroBtn.setEnabled(false);
        });
        oneBtn.setOnClickListener(v -> {
            doLevel(1);
            oneBtn.setEnabled(false);
            twoBtn.setEnabled(true);
            fourBtn.setEnabled(true);
            zeroBtn.setEnabled(true);
        });
        twoBtn.setOnClickListener(v -> {
            doLevel(2);
            twoBtn.setEnabled(false);
            oneBtn.setEnabled(true);
            fourBtn.setEnabled(true);
            zeroBtn.setEnabled(true);
        });
        fourBtn.setOnClickListener(v -> {
            doLevel(4);
            fourBtn.setEnabled(false);
            twoBtn.setEnabled(true);
            oneBtn.setEnabled(true);
            zeroBtn.setEnabled(true);
        });

        dialog.show();
    }

    private void doLevel(int i) {
        KeyValueUtil.setLevel(i);
        Toast.makeText(this, "下局生效!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (spiderSolitaireView != null && !spiderSolitaireView.onBackPressed()) {
            // don't finish()
            // let user finish() through "Exit" menu item
            Toast.makeText(this, R.string.hint_message_exit_through_menu, Toast.LENGTH_LONG).show();
        }
    }

}
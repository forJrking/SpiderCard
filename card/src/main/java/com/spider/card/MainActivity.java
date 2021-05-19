package com.spider.card;

import android.os.Bundle;
import android.view.Menu;
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
        stepsTv.setOnWin(this::stopTiming);
        newGame();
    }

    private void showMenu() {
        MenuDialog menuDialog = new MenuDialog(this, R.style.AppTheme_Dialog);
        menuDialog.show();
    }

    public void newGame() {
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
        stopTiming();
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
        stopTiming();
    }

    private void stopTiming() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
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
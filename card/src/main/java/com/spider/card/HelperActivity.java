package com.spider.card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class HelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ViewPager view = findViewById(R.id.view_pager);
        androidx.appcompat.app.ActionBar supportActionBar = getSupportActionBar();
        // 显示返回按钮
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle("左右滑动查看游戏规则");

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HelperFragment.newInstance(0));
        fragments.add(HelperFragment.newInstance(1));
        fragments.add(HelperFragment.newInstance(2));
        fragments.add(HelperFragment.newInstance(3));
        view.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class HelperFragment extends Fragment {

        private final static String[] tips = {
                "移动单张/多张牌,被移动牌要比移动到位置牌数值小1(无关花色),例如:9移动到10下面",
                "一般在上方牌无法移动后,点击右下角牌堆,发牌",
                "自动收牌,当牌的大小由 K-A 从大到小且花色要一致,\n例如 K♠,Q♠,J♠,10♠,9♠,8♠,7♠,6♠,5♠,4♠,3♠,2♠,♠A",
                "获胜,所有牌都收堆结束,即可获得胜利",
        };

        private final static int[] res = {
                R.raw.move,
                R.raw.fapai,
                R.raw.shou,
                R.raw.win,
        };

        public static HelperFragment newInstance(int po) {
            Bundle args = new Bundle();
            args.putInt("arg", po);
            HelperFragment fragment = new HelperFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.helper_fragment, container, false);
            return root;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            int arg = getArguments().getInt("arg", 0);
            TextView tv = view.findViewById(R.id.tip_tv);
            GifImageView gif = view.findViewById(R.id.gif);
            tv.setText(tips[arg]);
            gif.setImageResource(res[arg]);
        }
    }

}
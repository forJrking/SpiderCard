package com.spider.card;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.spider.card.utils.KeyValueUtil;

/**
 * @description:
 * @author: forjrking
 * @date: 2021/5/19 2:26 下午
 */
public class MenuDialog extends Dialog {
    public MenuDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected MenuDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private Button zeroBtn, oneBtn, twoBtn, fourBtn;
    private ImageButton skin1Btn, skin2Btn;

    private void init() {
        Dialog dialog = this;
        dialog.setContentView(R.layout.dialog_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.exit_btn).setOnClickListener(v -> {
            dialog.dismiss();
            MainActivity context = getMainActivity();
            context.finish();
        });
        dialog.findViewById(R.id.new_btn).setOnClickListener(v -> {
            dialog.dismiss();
            MainActivity context = getMainActivity();
            context.newGame();
        });
        dialog.findViewById(R.id.help_btn).setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(getContext(), R.string.help_tip, Toast.LENGTH_LONG).show();
        });
        zeroBtn = dialog.findViewById(R.id.zero_btn);
        oneBtn = dialog.findViewById(R.id.one_btn);
        twoBtn = dialog.findViewById(R.id.two_btn);
        fourBtn = dialog.findViewById(R.id.four_btn);
        skin1Btn = dialog.findViewById(R.id.skin1_btn);
        skin2Btn = dialog.findViewById(R.id.skin2_btn);

        SwitchCompat switchBtn = dialog.findViewById(R.id.voice_switch);
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

        switchBtn.setChecked(KeyValueUtil.openVoice());
        switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            KeyValueUtil.setVoiceSwitch(isChecked);
        });

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

        int skin = KeyValueUtil.getSkin();
        if (skin == 2) {
            skin2Btn.setBackgroundResource(R.color.colorAccent);
            skin1Btn.setBackgroundResource(R.color.white);
        } else {
            skin1Btn.setBackgroundResource(R.color.colorAccent);
            skin2Btn.setBackgroundResource(R.color.white);
        }
        skin1Btn.setOnClickListener(v -> {
            skin1Btn.setBackgroundResource(R.color.colorAccent);
            skin2Btn.setBackgroundResource(R.color.white);
            doSkin(1);
        });
        skin2Btn.setOnClickListener(v -> {
            skin2Btn.setBackgroundResource(R.color.colorAccent);
            skin1Btn.setBackgroundResource(R.color.white);
            doSkin(2);
        });
    }

    private MainActivity getMainActivity() {
        Context context= getContext();
        if(context instanceof MainActivity) {
           return (MainActivity) context;
        }else {
            ContextThemeWrapper wrapper = (ContextThemeWrapper) context;
            Context baseContext = wrapper.getBaseContext();
            return (MainActivity) baseContext;
        }
    }

    private void doSkin(int i) {
        KeyValueUtil.setSkin(i);
    }

    private void doLevel(int i) {
        KeyValueUtil.setLevel(i);
        Toast.makeText(getContext(), R.string.next_game, Toast.LENGTH_SHORT).show();
    }


}

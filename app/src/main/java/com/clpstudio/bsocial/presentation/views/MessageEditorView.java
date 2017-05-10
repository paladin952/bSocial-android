package com.clpstudio.bsocial.presentation.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.MessageTypedResolver;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 5/6/17.
 */

public class MessageEditorView extends FrameLayout {

    private OnTextSubmited onTextSubmitedListener;

    public interface OnTextSubmited {
        void onTextSubmited(String text);

        void onGifSelected(String gifText);
    }

    @BindView(R.id.messageEditText)
    EditText editText;

    public MessageEditorView(@NonNull Context context) {
        super(context);
        init();
    }

    public MessageEditorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageEditorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MessageEditorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_message_editor, this, true);
        ButterKnife.bind(this, view);

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (onTextSubmitedListener != null) {
                    String text = editText.getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        switch (MessageTypedResolver.getMessageType(text)) {
                            case MessageTypedResolver.TYPE_GIF_MESSAGE:
                                //nothing
                                break;
                            default:
                                onTextSubmitedListener.onTextSubmited(text);
                        }
                        editText.setText("");
                    }
                }
                return true;
            }
            return false;
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (!TextUtils.isEmpty(text)) {
                    String textStr = text.toString();
                    if (textStr.startsWith("@gif ")) {
                        String tokens[] = textStr.split("@gif ");
                        if (tokens.length > 0) {
                            Log.d("luci", "gif token = " + tokens[1]);
                            onTextSubmitedListener.onGifSelected(tokens[1]);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setOnTextSubmitedListener(OnTextSubmited onTextSubmitedListener) {
        this.onTextSubmitedListener = onTextSubmitedListener;
    }
}

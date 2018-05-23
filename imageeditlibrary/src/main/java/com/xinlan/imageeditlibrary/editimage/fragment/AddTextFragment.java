package com.xinlan.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.ui.ColorPicker;
import com.xinlan.imageeditlibrary.editimage.view.TextStickerView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.easing.Linear;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.YGravity;


/**
 * 添加文本贴图
 *
 * @author 潘易
 */
public class AddTextFragment extends BaseEditFragment implements TextWatcher, OnClickListener {
    public static final int INDEX = ModuleConfig.INDEX_ADDTEXT;
    public static final String TAG = AddTextFragment.class.getName();

    private View mainView;
    private View backToMenu;// 返回主菜单


    private TextStickerView mTextStickerView;// 文字贴图显示控件

    private ColorPicker mColorPicker;

    private int mTextColor = Color.BLACK;
    private InputMethodManager imm;

    private SaveTextStickerTask mSaveTask;

    private EasyPopup textPopup;
    private LinearLayout llTextInput;
    private LinearLayout llSelectColor;
    private LinearLayout llSelectFont;
    private RecyclerView rvHotText;
    private TextView tvRecommentText;
    private TextView tvConfirm;
    private EditText etTextInput;


    public static AddTextFragment newInstance() {
        AddTextFragment fragment = new AddTextFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mainView = inflater.inflate(R.layout.fragment_edit_image_add_text, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextStickerView = (TextStickerView) getActivity().findViewById(R.id.text_sticker_panel);
        textPopup = EasyPopup.create()
                .setContentView(getActivity(), R.layout.fragment_addtext_pop)
                .setAnimationStyle(R.style.BottomPopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .apply();
        llTextInput = mainView.findViewById(R.id.ll_input);
        llSelectColor = mainView.findViewById(R.id.ll_select_color);
        llSelectFont = mainView.findViewById(R.id.ll_select_font);
        //etTextInput = mainView.findViewById(R.id.et_temp);

        tvConfirm = textPopup.findViewById(R.id.tv_confirm);
        tvRecommentText = textPopup.findViewById(R.id.tv_recommend);
        rvHotText = textPopup.findViewById(R.id.rv_hot_text);
        etTextInput = textPopup.findViewById(R.id.et_input);

        llSelectFont.setOnClickListener(this);
        llSelectColor.setOnClickListener(this);
        llTextInput.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvRecommentText.setOnClickListener(this);

        mColorPicker = new ColorPicker(getActivity(), 255, 0, 0);
        etTextInput.addTextChangedListener(this);
        mTextStickerView.setEditText(etTextInput);
        mTextStickerView.setTextColor(Color.BLACK);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString().trim();
        mTextStickerView.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_select_font) {

        } else if (id == R.id.ll_select_color) {

        } else if (id == R.id.ll_input) {
            textPopup.showAtAnchorView(mainView, YGravity.ALIGN_BOTTOM, YGravity.CENTER);
            onShow();
        } else if (id == R.id.tv_confirm) {

        } else if (id == R.id.tv_recommend) {
            if (rvHotText.getVisibility() == View.VISIBLE) {
                rvHotText.setVisibility(View.GONE);
            } else {
                rvHotText.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 颜色选择 按钮点击
     */
    private final class SelectColorBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mColorPicker.show();
            Button okColor = (Button) mColorPicker.findViewById(R.id.okColorButton);
            okColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextColor(mColorPicker.getColor());
                    mColorPicker.dismiss();
                }
            });
        }
    }//end inner class

    /**
     * 修改字体颜色
     *
     * @param newColor
     */
    private void changeTextColor(int newColor) {
        this.mTextColor = newColor;
        mTextStickerView.setTextColor(mTextColor);
    }

    public void hideInput() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null && isInputMethodShow()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isInputMethodShow() {
        return imm.isActive();
    }

    /**
     * 返回按钮逻辑
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end class

    /**
     * 返回主菜单
     */
    @Override
    public void backToMain() {
        hideInput();
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(MainMenuFragment.INDEX);
        activity.mainImage.setVisibility(View.VISIBLE);
        mTextStickerView.setVisibility(View.GONE);
    }

    @Override
    public void onShow() {
        mTextStickerView.resetView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            activity.mode = EditImageActivity.MODE_TEXT;
            activity.mainImage.setImageBitmap(activity.getMainBit());
            mTextStickerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 保存贴图图片
     */
    public void applyTextImage() {
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }

        //启动任务
        mSaveTask = new SaveTextStickerTask(activity);
        mSaveTask.execute(activity.getMainBit());
    }

    /**
     * 文字合成任务
     * 合成最终图片
     */
    private final class SaveTextStickerTask extends StickerTask {

        public SaveTextStickerTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            float[] f = new float[9];
            m.getValues(f);
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);
            //System.out.println("scale = " + scale_x + "       " + scale_y + "     " + dx + "    " + dy);
            mTextStickerView.drawText(canvas, mTextStickerView.layout_x,
                    mTextStickerView.layout_y, mTextStickerView.mScale, mTextStickerView.mRotateAngle);
            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            mTextStickerView.clearTextContent();
            mTextStickerView.resetView();

            activity.changeMainBitmap(result, true);
            backToMain();
        }
    }//end inner class

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSaveTask != null && !mSaveTask.isCancelled()) {
            mSaveTask.cancel(true);
        }
    }
}// end class

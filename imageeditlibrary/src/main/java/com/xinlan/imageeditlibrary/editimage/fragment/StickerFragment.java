package com.xinlan.imageeditlibrary.editimage.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.adapter.StickerAdapter;
import com.xinlan.imageeditlibrary.editimage.adapter.StickerTypeAdapter;
import com.xinlan.imageeditlibrary.editimage.model.StickerBean;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.utils.PhotoUtils;
import com.xinlan.imageeditlibrary.editimage.view.StickerItem;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.easing.Linear;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 贴图分类fragment
 *
 * @author panyi
 */
public class StickerFragment extends BaseEditFragment implements OnClickListener {
    public static final int INDEX = ModuleConfig.INDEX_STICKER;

    public static final String TAG = StickerFragment.class.getName();
    public static final String STICKER_FOLDER = "stickers";

    private View mainView;
    private View backToType;// 返回类型选择
    private StickerView mStickerView;// 贴图显示控件
    private StickerAdapter mStickerAdapter;// 贴图列表适配器

    private LoadStickersTask mLoadStickersTask;
    private List<StickerBean> stickerBeanList = new ArrayList<StickerBean>();

    private SaveStickersTask mSaveTask;
    private LinearLayout llTakePhoto;
    private LinearLayout llOpenAlbum;
    private LinearLayout llTemplate;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;

    public static StickerFragment newInstance() {
        StickerFragment fragment = new StickerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.fragment_edit_image_sticker_type,
                null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mStickerView = activity.mStickerView;
        llTakePhoto = mainView.findViewById(R.id.ll_take_photo);
        llOpenAlbum = mainView.findViewById(R.id.ll_open_album);
        llTemplate = mainView.findViewById(R.id.ll_template);
        llTakePhoto.setOnClickListener(this);
        llOpenAlbum.setOnClickListener(this);
        llTemplate.setOnClickListener(this);
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_STICKERS;
        activity.mStickerFragment.getmStickerView().setVisibility(
                View.VISIBLE);
    }


    //导入贴图数据
    private void loadStickersData() {
        if (mLoadStickersTask != null) {
            mLoadStickersTask.cancel(true);
        }
        mLoadStickersTask = new LoadStickersTask();
        mLoadStickersTask.execute(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    Log.e("fdsf", "拍照完成");
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(imageUri, getActivity());
                    if (bitmap != null) {
                        activity.changeMainBitmap(bitmap, false);
                    }
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    imageUri = Uri.fromFile(fileUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(getActivity(), data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        newUri = FileProvider.getUriForFile(getActivity(), "com.pufei.gxdt.fileprovider", new File(newUri.getPath()));
                    Bitmap bitmap1 = PhotoUtils.getBitmapFromUri(newUri, getActivity());
                    if (bitmap1 != null) {
                        activity.changeMainBitmap(bitmap1, false);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_take_photo) {
            takePhoto();
        } else if (id == R.id.ll_open_album) {
            openAlbum();
        } else if (id == R.id.ll_template) {
            useTemplate();
        }
    }

    private void useTemplate() {

    }

    private void openAlbum() {
        requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
            @Override
            public void granted() {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, CODE_GALLERY_REQUEST);
            }

            @Override
            public void denied() {
                Toast.makeText(getActivity(), "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void takePhoto() {
        requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
            @Override
            public void granted() {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    //通过FileProvider创建一个content类型的Uri
                    imageUri = FileProvider.getUriForFile(getActivity(), "com.pufei.gxdt.fileprovider", fileUri);
                // PhotoUtils.takePicture(getActivity(), imageUri, CODE_CAMERA_REQUEST);
                Intent intentCamera = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                }
                intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //将拍照结果保存至photo_file的Uri中，不保留在相册中
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentCamera, CODE_CAMERA_REQUEST);
            }

            @Override
            public void denied() {
                Toast.makeText(getActivity(), "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 导入贴图数据
     */
    private final class LoadStickersTask extends AsyncTask<Integer, Void, Void> {
        private Dialog loadDialog;

        public LoadStickersTask() {
            super();
            loadDialog = BaseActivity.getLoadingDialog(getActivity(), R.string.saving_image, false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            stickerBeanList.clear();
            AssetManager assetManager = getActivity().getAssets();
            try {
                String[] lists = assetManager.list(STICKER_FOLDER);
                for (String parentPath : lists) {

                }//end for each
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadDialog.dismiss();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadDialog.dismiss();
        }
    }//end inner class

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadStickersTask != null) {
            mLoadStickersTask.cancel(true);
        }
    }

    /**
     * 跳转至贴图详情列表
     *
     * @param path
     */
    public void swipToStickerDetails(String path) {
        mStickerAdapter.addStickerImages(path);
    }

    /**
     * 从Assert文件夹中读取位图数据
     *
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 选择贴图加入到页面中
     *
     * @param path
     */
    public void selectedStickerItem(String path) {
        mStickerView.addBitImage(getImageFromAssetsFile(path));
    }

    public StickerView getmStickerView() {
        return mStickerView;
    }

    public void setmStickerView(StickerView mStickerView) {
        this.mStickerView = mStickerView;
    }

    /**
     * 返回主菜单页面
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end inner class

    @Override
    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(0);
        mStickerView.setVisibility(View.GONE);
    }

    /**
     * 保存贴图任务
     *
     * @author panyi
     */
    private final class SaveStickersTask extends StickerTask {
        public SaveStickersTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            LinkedHashMap<Integer, StickerItem> addItems = mStickerView.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);// 乘以底部图片变化矩阵
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }// end for
        }

        @Override
        public void onPostResult(Bitmap result) {
            mStickerView.clear();
            activity.changeMainBitmap(result, true);
            backToMain();
        }
    }// end inner class

    /**
     * 保存贴图层 合成一张图片
     */
    public void applyStickers() {
        // System.out.println("保存 合成图片");
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        mSaveTask = new SaveStickersTask((EditImageActivity) getActivity());
        mSaveTask.execute(activity.getMainBit());
    }
}// end class


package com.vkc.bluetyga.activity.shopimage;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vkc.bluetyga.R;
import com.vkc.bluetyga.activity.shopimage.model.ImageListModel;
import com.vkc.bluetyga.constants.VKCUrlConstants;
import com.vkc.bluetyga.manager.AppPrefenceManager;
import com.vkc.bluetyga.manager.HeaderManager;
import com.vkc.bluetyga.utils.AndroidMultiPartEntity;
import com.vkc.bluetyga.utils.CustomToast;
import com.vkc.bluetyga.volleymanager.VolleyWrapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user2 on 11/8/17.
 */

public class ShopImageActivity extends Activity implements VKCUrlConstants, View.OnClickListener {

    ImageView imageShop;
    Activity mContext;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;
    private Uri mImageCaptureUri;
    int ACTIVITY_REQUEST_CODE = 700;
    int ACTIVITY_FINISH_RESULT_CODE = 701;
    String filePath = "";
    Button btnCapture, btnUpload;
    ArrayList<ImageListModel> imageList;
    ImageView image1, image2, image1Delete, image2Delete;
    RelativeLayout relative1, relative2;
    private File destination = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_image);
        mContext = this;
        initUI();
    }

    private void initUI() {
        imageList = new ArrayList<>();
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ShopImageActivity.this, getResources().getString(R.string.shop_image));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

        imageShop = (ImageView) findViewById(R.id.imageShop);
        image1 = (ImageView) findViewById(R.id.imageOne);
        image2 = (ImageView) findViewById(R.id.imageTwo);
        image1Delete = (ImageView) findViewById(R.id.deleteImage1);
        image2Delete = (ImageView) findViewById(R.id.deleteImage2);
        image1Delete.setVisibility(View.GONE);
        image2Delete.setVisibility(View.GONE);
        relative1 = (RelativeLayout) findViewById(R.id.relative1);
        relative2 = (RelativeLayout) findViewById(R.id.relative2);

        btnCapture = (Button) findViewById(R.id.buttonCapture);
        btnUpload = (Button) findViewById(R.id.buttonUpload);
        getImage();
        mImageBack.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image1Delete.setOnClickListener(this);
        image2Delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == btnCapture) {
            showCamera();
        } else if (v == btnUpload) {
            if (filePath.equals("")) {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(21);
            } else {
                try {
                    UploadFileToServer upload = new UploadFileToServer();
                    upload.execute();
                } catch (Exception e) {

                }
            }
        } else if (v == image1Delete) {

            DialogConfirm dialog = new DialogConfirm(mContext, imageList.get(0).getId());
            dialog.show();
            //  deleteImage(imageList.get(0).getId());
        } else if (v == image2Delete) {

            DialogConfirm dialog = new DialogConfirm(mContext, imageList.get(1).getId());
            dialog.show();

        }
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        private JSONObject obj;
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Uploading...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();


                HttpPost httppost = new HttpPost(UPLOAD_IMAGE);

                File file = new File(filePath);
                FileBody bin1 = new FileBody(file.getAbsoluteFile());


                AndroidMultiPartEntity entity;
                entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });


                entity.addPart("cust_id", new StringBody(AppPrefenceManager.getCustomerId(mContext)));
                entity.addPart("role", new StringBody(AppPrefenceManager.getUserType(mContext)));
                entity.addPart("image", bin1);
                httppost.setEntity(entity);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    responseString = EntityUtils.toString(r_entity);


                } else {

                    responseString = EntityUtils.toString(r_entity);
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            System.out.print("Result " + result);
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject responseObj = obj.optJSONObject("response");
            responseString = responseObj.optString("status");

            if (responseString.equalsIgnoreCase("Success")) {

                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(19);
                getImageHistory();
            } else if (responseString.equalsIgnoreCase("Exceeded")) {

                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(20);
                getImageHistory();
            } else {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(0);
            }
        }

    }

    public void showCamera() {
        /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
        myDir.mkdirs();
        File file = new File(myDir, "tmp_avatar_"
                + String.valueOf(System.currentTimeMillis()) + ".JPEG");
        mImageCaptureUri = Uri.fromFile(file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            try {
                Uri selectedImage = data.getData();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                //Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                destination.mkdirs();
                FileOutputStream fo;
                try {
                    if (destination.exists ()) destination.delete ();

                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                filePath = destination.getAbsolutePath();
                imageShop.setImageBitmap(bitmap);

                //    CallForAPI(true);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
     /*   if (requestCode == ACTIVITY_REQUEST_CODE) {
            if (resultCode == ACTIVITY_FINISH_RESULT_CODE) {
                finish();
            }
        } else {
            Bitmap bitmap = null;
            Uri imageUri = null;
            if (resultCode != Activity.RESULT_OK)
                return;
            switch (requestCode) {
                case 0:
                    imageUri = mImageCaptureUri;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            if (bitmap != null) {
                try {
                    File tempFile = new File(imageUri.getPath());
                    long size = tempFile.length();
                    ByteArrayOutputStream byteArrayOutputStream;
                    Log.e("Size image:", "" + size);
                    int minSize = 600;
                    int widthOfImage = bitmap.getWidth();
                    int heightOfImage = bitmap.getHeight();
                    Log.e("Width", "" + widthOfImage);
                    Log.e("Height", "" + heightOfImage);
                    int newHeight = 600;
                    int newWidht = 600;
                    if (widthOfImage < minSize && heightOfImage < minSize) {
                        newWidht = widthOfImage;
                        newHeight = heightOfImage;
                    } else {
                        if (widthOfImage >= heightOfImage) {
                            //int newheght = heightOfImage/600;
                            float ratio = (float) minSize / widthOfImage;
                            Log.e("Multi width greater", "" + minSize + "/" + widthOfImage + "=" + ratio);
                            newHeight = (int) (heightOfImage * ratio);
                            newWidht = minSize;
                        } else if (heightOfImage > widthOfImage) {
                            float ratio = (float) minSize / heightOfImage;
                            newWidht = (int) (widthOfImage * ratio);
                            newHeight = minSize;
                        }

                    }
                    if (size > 1024 * 1024) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    }


                    if (size > (4 * 1024 * 1024)) {
                        //CustomStatusDialog(RESPONSE_LARGE_IMAGE);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } else {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int bounding = dpToPx(mContext.getResources().getDisplayMetrics().density);
                        float xScale = (100 * (float) bounding) / width;
                        float yScale = (100 * (float) bounding) / height;
                        float scale = (xScale <= yScale) ? xScale : yScale;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scale, scale);
                        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);


                        BitmapDrawable result = new BitmapDrawable(scaledBitmap);

                        imageShop.setImageDrawable(result);
                        imageShop.setScaleType(ImageView.ScaleType.FIT_XY);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        filePath = "/sdcard/file" + "vkc" + ".jpg";
                        File f = new File(filePath);
                        try {
                            f.createNewFile();
                            FileOutputStream fos = null;

                            fos = new FileOutputStream(f);
                            fos.write(byteArray);
                            fos.close();
                            filePath = f.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                    // CustomStatusDialog(RESPONSE_OUT_OF_MEMORY);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }
        }*/
    }

    private int dpToPx(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void getImage() {
        try {


            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(UPLOADED_IMAGE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {
                                        JSONObject objData = objResponse.optJSONObject("data");
                                        String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);

                                        getImageHistory();
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }
    }

    public void getImageHistory() {
        imageList.clear();
        try {


            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_IMAGE_HISTORY);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {
                                        JSONArray objData = objResponse.optJSONArray("data");
                                        if (objData.length() > 0) {
                                            for (int i = 0; i < objData.length(); i++) {
                                                JSONObject obj = objData.optJSONObject(i);
                                                ImageListModel model = new ImageListModel();
                                                model.setImage(obj.getString("image"));
                                                model.setId(obj.getString("id"));
                                                imageList.add(model);
                                            }
                                            if (imageList.size() > 1) {
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }

                                                if (!imageList.get(1).getImage().equals("")) {
                                                    relative2.setVisibility(View.VISIBLE);
                                                    image2Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(1).getImage()).resize(200, 200).centerInside().into(image2);
                                                } else {
                                                    relative2.setVisibility(View.GONE);
                                                    image2Delete.setVisibility(View.GONE);
                                                }
                                            } else {

                                                relative2.setVisibility(View.GONE);
                                                image2Delete.setVisibility(View.GONE);
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }
                                            }

                                        } else {

                                            relative1.setVisibility(View.GONE);
                                            image1Delete.setVisibility(View.GONE);
                                            relative2.setVisibility(View.GONE);
                                            image2Delete.setVisibility(View.GONE);
                                            // initUI();
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(51);

                                        }
                                       /* String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);*/
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }
    }


    public void deleteImage(String id) {

        try {


            String[] name = {"id"};
            String[] values = {id};

            final VolleyWrapper manager = new VolleyWrapper(DELETE_IMAGE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    //JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = rootObject.optString("status");
                                    if (status.equals("Success")) {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(52);
                                        getImageHistory();
                                    } else if (status.equals("Error")) {
                                        getImageHistory();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }
    }

    public class DialogConfirm extends Dialog implements
            android.view.View.OnClickListener {

        public Activity mActivity;
        String type, message, id;

        public DialogConfirm(Activity a, String id) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.id = id;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_delete_image);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textYes = (TextView) findViewById(R.id.textYes);
            TextView textNo = (TextView) findViewById(R.id.textNo);


            textYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteImage(id);
                    dismiss();
                }
            });


            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();


                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }
}


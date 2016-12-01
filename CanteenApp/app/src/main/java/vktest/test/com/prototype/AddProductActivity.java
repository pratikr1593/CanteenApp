package vktest.test.com.prototype;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Бейбут on 24.04.2016.
 */
public class AddProductActivity extends AppCompatActivity{
    ProgressDialog loading;

    private ImageButton buttonTakePicture;
    private Button addProduct;

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 3;

    private String selectedImagePath = "";
    private String imgPath = "";

    public static final String UPLOAD_URL = "http://bekabot.co.nf/upload.php";
    public static final String UPLOAD_KEY = "image";

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;
    private Uri uriSavedImage;

    private EditText product1;
    private EditText product2;
    private EditText product3;
    private EditText price;

    private String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewOne);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonTakePicture = (ImageButton)findViewById(R.id.takePicture);
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                final View textEntryView = factory.inflate(R.layout.view_camera_options, null);

                final TextView choosePicture = (TextView) textEntryView.findViewById(R.id.chooseFromGallery);
                final TextView takePicture = (TextView) textEntryView.findViewById(R.id.takePictureNow);
                final AlertDialog.Builder alert = new AlertDialog.Builder(AddProductActivity.this, R.style.AppCompatAlertDialogStyle);

                alert.setTitle("Choose one")
                        .setView(textEntryView);
                choosePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
                    }
                });

                takePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//
//
//                        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
//                        imagesFolder.mkdirs();
//
//                        File image = new File(imagesFolder, "QR_" + timeStamp + ".png");
//                        uriSavedImage = Uri.fromFile(image);
//
//                        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//                        startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                });
                alert.show();
            }
        }

        );

        addProduct = (Button) findViewById(R.id.addProductButton);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String generatedString = UUID.randomUUID().toString();
                final String orderID = generatedString.substring(0, Math.min(generatedString.length(), 5));
                uploadImage();
                Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/products/" + orderID);
                myFirebaseRef.child("id").setValue(orderID);

                myFirebaseRef.child("product1").setValue(product1.getText().toString());
                myFirebaseRef.child("product2").setValue(product2.getText().toString());
                myFirebaseRef.child("product3").setValue(product3.getText().toString());
                myFirebaseRef.child("price").setValue(price.getText().toString());
                myFirebaseRef.child("image_id").setValue(responseString);
                Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                finish();



            }
        });

        product1 = (EditText) findViewById(R.id.product1name);
        product2 = (EditText) findViewById(R.id.product2name);
        product3 = (EditText) findViewById(R.id.product3name);
        price = (EditText) findViewById(R.id.addPrice);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("IMAGESIZE", bitmap.getByteCount() + "");
                imageView.setImageBitmap(bitmap);


            }

//            else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
//            {
//
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriSavedImage);
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(90);
//                    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.d("IMAGESIZE", bitmap.getByteCount() + "");
//                imageView.setImageBitmap(bitmap);
//
//            }
            else if(requestCode== CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==Activity.RESULT_OK){

// Describe the columns you'd like to have returned. Selecting from the Thumbnails location gives you both the Thumbnail Image ID, as well as the original image ID
                String[] projection = {
                        MediaStore.Images.Thumbnails._ID,  // The columns we want
                        MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.KIND,
                        MediaStore.Images.Thumbnails.DATA};
                String selection = MediaStore.Images.Thumbnails.KIND + "="  + // Select only mini's
                        MediaStore.Images.Thumbnails.MINI_KIND;

                String sort = MediaStore.Images.Thumbnails._ID + " DESC";

//At the moment, this is a bit of a hack, as I'm returning ALL images, and just taking the latest one. There is a better way to narrow this down I think with a WHERE clause which is currently the selection variable
                Cursor myCursor = this.managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, selection, null, sort);

                long imageId = 0l;
                long thumbnailImageId = 0l;
                String thumbnailPath = "";

                try{
                    myCursor.moveToFirst();
                    imageId = myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
                    thumbnailImageId = myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
                    thumbnailPath = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                }
                finally{myCursor.close();}

                //Create new Cursor to obtain the file Path for the large image

                String[] largeFileProjection = {
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA
                };

                String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
                myCursor = this.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, largeFileProjection, null, null, largeFileSort);
                String largeImagePath = "";

                try{
                    myCursor.moveToFirst();

//This will actually give yo uthe file path location of the image.
                    largeImagePath = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                }
                finally{myCursor.close();}
                // These are the two URI's you'll be interested in. They give you a handle to the actual images
                Uri uriLargeImage = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
                Uri uriThumbnailImage = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, String.valueOf(thumbnailImageId));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriThumbnailImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
// I've left out the remaining code, as all I do is assign the URI's to my own objects anyways...


            }

        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void uploadImage(){

        class UploadImage extends AsyncTask<Bitmap,Void,String> {


            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddProductActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                responseString = s;
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                Log.d("SIZEOF", bitmap.getByteCount() + "");

                bitmap = getResizedBitmap(bitmap, 500);
                Log.d("SIZEOF", bitmap.getByteCount() + "");


                String uploadImage = getStringImage(bitmap);



                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
//        if(loading.isShowing())
//        {
//            loading.dismiss();
//        }

    }
}

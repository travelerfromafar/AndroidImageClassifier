package com.example.startimageclassify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startimageclassify.fragment.fragment_main;
import com.example.startimageclassify.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class camera extends AppCompatActivity {

    Button camera, gallery,show_comp;
    ImageView imageView, value;
    TextView result;
    String textResult, textGet;
//        test_1, test_2, test_3, test_4, test_5,
//        test_6, test_7, test_8, test_9, test_10;

    int imageSize = 224, maxPos = 0;
    String[] classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        classes = getResources().getStringArray(R.array.VegRecog);

        camera = findViewById(R.id.button_camera);
        gallery = findViewById(R.id.button_gallery);
        show_comp = findViewById(R.id.Show_Comp);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");
            if (bitmap != null) {
                int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                ImageClassify(bitmap);
            } else {
                Intent intent = getIntent();
                Uri imageUri = Uri.parse(intent.getStringExtra("galleryImage"));
                Bitmap imageGal = null;
                try {
                    imageGal = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageClassify(imageGal);
            }
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });

        show_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTab(classes[maxPos]);
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
//                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            // find the index of the class with the biggest confidence.
//            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
//            String[] classes = {"Spinach", "Carrot", "Tomato"};
            result.setText(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap imAgeTick = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(imAgeTick.getWidth(), imAgeTick.getHeight());
                imAgeTick = ThumbnailUtils.extractThumbnail(imAgeTick, dimension, dimension);
//                imageView.setImageBitmap(image);
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);

                ImageClassify(imAgeTick);
//                Checker();
            }else{
                Uri dat = data.getData();
                Bitmap imagePro = null;
                try {
                    imagePro = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageClassify(imagePro);
//                imageView.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Checker() {
        textResult = result.getText().toString();
        if (textResult.matches(classes[0]) ||
                textResult.matches(classes[1]) ||
                textResult.matches(classes[2])
            ) { OpenTab(textGet = textResult);
        } else {
            Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
//            return;
        }
    }

    private void OpenTab(String NameRes) {
        Intent i = new Intent(camera.this, fragment_main.class);
        i.putExtra("key", NameRes);
        startActivity(i);

        overridePendingTransition(R.anim.slide_up_out, R.anim.slide_up_in);
    }

    private void ImageClassify(Bitmap gitImg){
        imageView.setImageBitmap(gitImg);
        gitImg = Bitmap.createScaledBitmap(gitImg, imageSize, imageSize, false);
        classifyImage(gitImg);
    }
}
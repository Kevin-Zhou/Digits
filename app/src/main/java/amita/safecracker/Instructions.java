package amita.safecracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Twins on 24/07/2015.
 */
public class Instructions extends Activity {
    Button playAgain;//lets the user play again
    Context context;//needed to start a new intent;
    TextView welcome;
    int imageHeight;
    Button start;
    Button instructions;
    ImageView pic;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
        welcome = (TextView) findViewById(R.id.welcome);
        start = (Button) findViewById(R.id.startGame);
        instructions =(Button) findViewById(R.id.Instructions);
         context=this;//activity is a subclass of context
        Intent i = getIntent();

//declaring fonts
        Typeface tf_light = Typeface.createFromAsset(getAssets(),
                "fonts/font_light.ttf");

        //find screen width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //scale picture
        pic = (ImageView) findViewById(R.id.imageView);
        scaleImage(pic,metrics.widthPixels);

        //does textstuff, copied from Kevin's code, sets, size, font, and color

        welcome.setTextSize((int) (0.06 * getWidth()));
        welcome.setTypeface(tf_light);
        welcome.setTextColor(Color.parseColor("#ff437863"));

        start.setTextSize((int) (0.025 * getWidth()));
        start.setTypeface(tf_light);
        instructions.setTextSize((int) (0.025 * getWidth()));
        instructions.setTypeface(tf_light);
        click1();
        click2();
        //shareButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        //shareButton.setId(R.id.share);

        //added code
        start.setGravity(Gravity.CENTER);
        start.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
        start.setTextColor(Color.rgb(43, 78, 63));
        instructions.setGravity(Gravity.CENTER);
        instructions.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
        instructions.setTextColor(Color.rgb(43, 78, 63));
    }


    private void scaleImage(ImageView view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
        imageHeight=params.height;
    }
    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }


    public void click1() {
        //Select a specific button to bundle it with the action you want
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cal = new Intent(getApplicationContext(), Instructions5.class);
                // passing array index
                startActivity(cal);
                finish();
            }
        });
    }


    public void click2() {
        //Select a specific button to bundle it with the action you want
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cal = new Intent(getApplicationContext(), Instructions2.class);
                // passing array index
                startActivity(cal);
                finish();
            }
        });
    }

    public float getWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        return screenWidth;
    }



}

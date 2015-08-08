package amita.safecracker;

import android.annotation.TargetApi;
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
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import blurEffect.BlurBehind;

public class BlurredActivity extends Activity {
    LinearLayout numberCircles; // Colored bubbles behind the answer
    TextView result;//tells you if you won or lost & the time taken
    // TextView a1, a2, a3, and a4 form the answer in the number circles. They tell you the number that you had to guess
    TextView a1;
    TextView a2;
    TextView a3;
    TextView a4;
    TextView score;// Tells you your score
    Button playAgain;//lets the user play again
    Context context;//needed to start a new intent;
    int scoreData;


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
    }

    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurred);
        BlurBehind.getInstance()
                .withAlpha(250)
                .withFilterColor(Color.parseColor("#000000"))
                .setBackground(this);



        // THE FOLLOWING CODE CALCULATES THE PLAYER'S SCORE
        scoreData = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
       ImageView pic = (ImageView) findViewById(R.id.imageView);
        scaleImage(pic,metrics.widthPixels);






// THE FOLLOWING CODE MAKES THE SHARE/SHOP/NEXT BUTTONS
        //the layout on which you are working
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);
        //set the properties for button
        final Button shareButton = new Button(this);
        shareButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        shareButton.setText("Share");
        shareButton.setId(R.id.share);
        shareButton.setTextSize((int) (0.02 * getWidth()));
        shareButton.setTextColor(Color.rgb(255, 255, 255));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int) (0.01 * getWidth()), 0, (int) (0.01 * getWidth()), 0);
        shareButton.setLayoutParams(params);
        final GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(Integer.parseInt(MainActivity.currentGradient)); // Receive from MainActivity.java
        gdDefault.setCornerRadius(20);
        shareButton.setBackground(gdDefault);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/font.ttf");
        Typeface tf_light = Typeface.createFromAsset(getAssets(),
                "fonts/font_light.ttf");
        shareButton.setTypeface(tf);
        shareButton.setHeight((int) (0.1 * getWidth()));
        shareButton.setPadding((int) (0.03 * getWidth()),(int) (0.015 * getWidth()),(int) (0.03 * getWidth()),(int) (0.015 * getWidth()));
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bring up social media panel, where they can share stuff
                Intent i = new Intent(context, SocialActivity.class);
                startActivity(i);
                finish();//ends current activity
            }
        });
        shareButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    GradientDrawable gdHover = new GradientDrawable();
                    gdHover.setColor(Color.rgb(255, 255, 255));
                    gdHover.setCornerRadius(20);
                    shareButton.setBackground(gdHover);
                    shareButton.setTextColor(Integer.parseInt(MainActivity.currentGradient)); // Receive from MainActivity.java
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    shareButton.setBackground(gdDefault);
                    shareButton.setTextColor(Color.rgb(255, 255, 255));
                }
                return false;
            }
        });
        layout.addView(shareButton);
        final Button nextButton = new Button(this);
        nextButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        nextButton.setId(R.id.loopGame);
        nextButton.setTextSize((int) (0.02 * getWidth()));
        nextButton.setTextColor(Color.rgb(255, 255, 255));
        nextButton.setLayoutParams(params);
        nextButton.setBackground(gdDefault);
        nextButton.setTypeface(tf);
        nextButton.setHeight((int) (0.1 * getWidth()));
        nextButton.setPadding((int) (0.03 * getWidth()), (int) (0.015 * getWidth()), (int) (0.03 * getWidth()), (int) (0.015 * getWidth()));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the game goes on until the current row is smaller than or equal to 10 and if the player hasn't won or lost yet
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                finish();//ends current activity
            }
        });
        nextButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    GradientDrawable gdHover = new GradientDrawable();
                    gdHover.setColor(Color.rgb(255, 255, 255));
                    gdHover.setCornerRadius(20);
                    nextButton.setBackground(gdHover);
                    nextButton.setTextColor(Integer.parseInt(MainActivity.currentGradient));  // Receive from MainActivity.java
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    nextButton.setBackground(gdDefault);
                    nextButton.setTextColor(Color.rgb(255, 255, 255));
                }
                return false;
            }
        });
        layout.addView(nextButton);

        numberCircles = (LinearLayout) findViewById(R.id.numberCircles);
        TextView c1;
        TextView c2;
        TextView c3;
        TextView c4;
        c1 = (TextView) findViewById(R.id.c1);
        c2 = (TextView) findViewById(R.id.c2);
        c3 = (TextView) findViewById(R.id.c3);
        c4 = (TextView) findViewById(R.id.c4);
        c1.setMinimumHeight(140);
        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(getHeight()) + " " + String.valueOf(getStatusBarHeight()) + " " + String.valueOf(getWidth()), Toast.LENGTH_LONG);
        toast.show();
        c1.setMinimumWidth((int) (0.14 * getWidth()));
        c1.setMinimumHeight((int) (0.14 * getWidth()));
        c1.setLayoutParams(params);
        c2.setMinimumWidth((int) (0.14 * getWidth()));
        c2.setMinimumHeight((int) (0.14 * getWidth()));
        c2.setLayoutParams(params);
        c3.setMinimumWidth((int) (0.14 * getWidth()));
        c3.setMinimumHeight((int) (0.14 * getWidth()));
        c3.setLayoutParams(params);
        c4.setMinimumWidth((int) (0.14 * getWidth()));
        c4.setMinimumHeight((int) (0.14 * getWidth()));
        c4.setLayoutParams(params);
        GradientDrawable numberCircle = new GradientDrawable();
        numberCircle.setColor(Integer.parseInt(MainActivity.currentGradient));  // Receive from MainActivity.java
        numberCircle.setCornerRadius(((int) (0.14 * getWidth())/2));
        c1.setBackground(numberCircle);
        c2.setBackground(numberCircle);
        c3.setBackground(numberCircle);
        c4.setBackground(numberCircle);
//why
        result = (TextView) findViewById(R.id.result);
        result.setTextSize((int) (0.02 * getWidth()));
        result.setTypeface(tf);
        result.setTextColor(Integer.parseInt(MainActivity.currentGradient)); // Receive from MainActivity.java
        result.forceLayout();
        score = (TextView) findViewById(R.id.score);
        score.setTextSize((int) (0.1 * getWidth()));
        score.setTypeface(tf_light);
        score.setTextColor(Integer.parseInt(MainActivity.currentGradient)); // Receive from MainActivity.java
        playAgain = (Button) findViewById(R.id.loopGame);
        a1 = (TextView) findViewById(R.id.c1);
        a1.setTextSize((int) (0.03 * getWidth()));
        a1.setTypeface(tf);
        a1.setTextColor(Color.rgb(255, 255, 255));
        a2 = (TextView) findViewById(R.id.c2);
        a2.setTextSize((int) (0.03 * getWidth()));
        a2.setTypeface(tf);
        a2.setTextColor(Color.rgb(255, 255, 255));
        a3 = (TextView) findViewById(R.id.c3);
        a3.setTextSize((int) (0.03 * getWidth()));
        a3.setTypeface(tf);
        a3.setTextColor(Color.rgb(255, 255, 255));
        a4 = (TextView) findViewById(R.id.c4);
        a4.setTextSize((int) (0.03 * getWidth()));
        a4.setTypeface(tf);
        a4.setTextColor(Color.rgb(255, 255, 255));

        context = this;//activity is a subclass of context

        Intent i = getIntent();
        int position = i.getExtras().getInt("val");
        long timeVal = i.getExtras().getLong("time");
        String  answer = i.getExtras().getString("answer");
        char[] answers = answer.toCharArray();
        a1.setText(Character.toString(answers[0]));
        a2.setText(Character.toString(answers[1]));
        a3.setText(Character.toString(answers[2]));
        a4.setText(Character.toString(answers[3]));

        int time;
        int timeMinute;
        String timeString;
        time = (int) (timeVal / 1000);//you want it in seconds not milliseconds
        timeMinute = (int) Math.floor(time / 60);
        if(time>60) {
            timeString = "Time: " + String.valueOf(timeMinute) + "m" + String.valueOf(time % 60) + "s";
        }
        else{
            timeString =  "Time: "  + String.valueOf(time) + "s";
        }

        if (position == 1) {
            result.setText("You Lost!" + " | "  + timeString);
            playAgain.setText("Try Again");
        } else {
            result.setText("Got It!" + " | "  + timeString);
            playAgain.setText("Next Level");
        }

    }

    // This method separates the individual characters of a String with many spaces
    public static String expand(String text) {
        String result = "";
        for(int i = 0; i < text.length(); i++) {
            if (i < text.length() - 1) {
                result += text.charAt(i) + "      ";
            } else {
                result += text.charAt(i);
            }
            }
        return result;
    }
    public float getWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        return screenWidth;
    }
    public float getHeight(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y - getStatusBarHeight();
        return screenHeight;
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}

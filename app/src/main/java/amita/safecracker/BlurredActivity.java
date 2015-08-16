package amita.safecracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import blurEffect.BlurBehind;

public class BlurredActivity extends Activity {
    LinearLayout numberCircles; // Colored bubbles behind the answer
    TextView result;//tells you if you won or lost & the time taken
    // TextView a1, a2, a3, and a4 form the answer in the number circles. They tell you the number that you had to guess
    TextView a1;
    TextView a2;
    TextView a3;
    TextView a4;
    int imageHeight;
    TextView score;// Tells you your score
    TextView bestScore;//Gives your best score
    Button playAgain;//lets the user play again
    Context context;//needed to start a new intent;
    int scoreData;
    SharedPreferences prefs;
    //buttons=linear layout for buttons
    //numberCircles=linear layout for number circles
    //result=textview that displays the result
    //score=textview that displays the score

    private int scaleImage(ImageView view, int boundBoxInDp) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

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
        return params.height;

    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        int lost;
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        lost = prefs.getInt("lost", 0);
        if (lost == 1) {
            try {
                // Play lost sound effect
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.lose);
                mp.start();
            } catch (IllegalStateException e) {

            }
        } else {
            try {
                // Play lost sound effect
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.win);
                mp.start();
            } catch (IllegalStateException e) {

            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurred);
        BlurBehind.getInstance()
                .withAlpha(250)
                .withFilterColor(Color.parseColor("#000000"))
                .setBackground(this);


        // THE FOLLOWING CODE CALCULATES THE PLAYER'S SCORE

        Intent i = getIntent();
        long scoreData = i.getExtras().getLong("scoreV");


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ImageView pic = (ImageView) findViewById(R.id.imageView);
        imageHeight = scaleImage(pic, metrics.widthPixels);
        System.out.println(imageHeight / getHeight());


// THE FOLLOWING CODE MAKES THE SHARE/SHOP/NEXT BUTTONS
        //the layout on which you are working
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);
        RelativeLayout.LayoutParams params7 = (RelativeLayout.LayoutParams) layout.getLayoutParams();
        params7.setMargins(0, 0, 0, (int) (0.184410646 * imageHeight));
        layout.setLayoutParams(params7);
        //set the properties for button
        final Button shareButton = new Button(this);
        //shareButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        shareButton.setText("Share");
        //shareButton.setId(R.id.share);
        shareButton.setGravity(Gravity.CENTER);
        shareButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
        shareButton.setTextColor(Color.rgb(255, 255, 255));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        params.setMargins((int) (0.01 * getWidth()), 0, (int) (0.01 * getWidth()), 0);
        shareButton.setLayoutParams(params);
        final GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(Integer.parseInt(MainActivity.currentAccent)); // Receive from MainActivity.java
        gdDefault.setCornerRadius(20);
        shareButton.setBackground(gdDefault);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/font.ttf");
        Typeface tf_light = Typeface.createFromAsset(getAssets(),
                "fonts/font_light.ttf");
        shareButton.setTypeface(tf);
        shareButton.setHeight((int) ((0.093155893) * imageHeight));
        shareButton.setPadding((int) (0.035185185 * getWidth()), 0, (int) (0.035185185 * getWidth()), 0);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  // Bring up social media panel, where they can share stuff
                Intent i = new Intent(context, SocialActivity.class);
                startActivity(i);
                finish();//ends current activity*/
                shareIt();
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
                    shareButton.setTextColor(Integer.parseInt(MainActivity.currentAccent)); // Receive from MainActivity.java
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
        nextButton.setGravity(Gravity.CENTER);
        nextButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
        nextButton.setTextColor(Color.rgb(255, 255, 255));
        nextButton.setLayoutParams(params);
        nextButton.setBackground(gdDefault);
        nextButton.setTypeface(tf);
        nextButton.setHeight((int) ((0.093155893) * imageHeight));
        nextButton.setPadding((int) (0.035185185 * getWidth()), 0, (int) (0.035185185 * getWidth()), 0);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the game goes on until the current row is smaller than or equal to 10 and if the player hasn't won or lost yet
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                try {
                    // Play click sound effect
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.next_level);
                    mp.start();
                } catch (IllegalStateException e) {

                }
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
                    nextButton.setTextColor(Integer.parseInt(MainActivity.currentAccent));  // Receive from MainActivity.java
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    nextButton.setBackground(gdDefault);
                    nextButton.setTextColor(Color.rgb(255, 255, 255));
                }
                return false;
            }
        });
        layout.addView(nextButton);

        numberCircles = (LinearLayout) findViewById(R.id.numberCircles);
        RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) numberCircles.getLayoutParams();
        params5.setMargins(0, (int) (0.188212927 * imageHeight), 0, 0);
        numberCircles.setLayoutParams(params5);

        TextView c1;
        TextView c2;
        TextView c3;
        TextView c4;
        c1 = (TextView) findViewById(R.id.c1);
        c2 = (TextView) findViewById(R.id.c2);
        c3 = (TextView) findViewById(R.id.c3);
        c4 = (TextView) findViewById(R.id.c4);
        c1.setMinimumHeight(140);
        c1.setMinimumWidth((int) (0.133079847 * imageHeight));
        c1.setMinimumHeight((int) (0.133079847 * imageHeight));
        c1.setLayoutParams(params);
        c2.setMinimumWidth((int) (0.133079847 * imageHeight));
        c2.setMinimumHeight((int) (0.133079847 * imageHeight));
        c2.setLayoutParams(params);
        c3.setMinimumWidth((int) (0.133079847 * imageHeight));
        c3.setMinimumHeight((int) (0.133079847 * imageHeight));
        c3.setLayoutParams(params);
        c4.setMinimumWidth((int) (0.133079847 * imageHeight));
        c4.setMinimumHeight((int) (0.133079847 * imageHeight));
        c4.setLayoutParams(params);
        GradientDrawable numberCircle = new GradientDrawable();
        numberCircle.setColor(Integer.parseInt(MainActivity.currentAccent));  // Receive from MainActivity.java
        numberCircle.setCornerRadius(((int) (0.14 * getWidth()) / 2));
        c1.setBackground(numberCircle);
        c2.setBackground(numberCircle);
        c3.setBackground(numberCircle);
        c4.setBackground(numberCircle);
//why
        result = (TextView) findViewById(R.id.result);
        RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams) result.getLayoutParams();
        params6.setMargins(0, (int) (0.047528517 * imageHeight), 0, 0);
        result.setLayoutParams(params6);
        result.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.05 * imageHeight));
        result.setTypeface(tf);
        result.setPadding(0, (int) (-0.189701897 * result.getTextSize()), 0, (int) (-0.135501355 * result.getTextSize()));
        result.setTextColor(Integer.parseInt(MainActivity.currentAccent)); // Receive from MainActivity.java
        result.forceLayout();
        score = (TextView) findViewById(R.id.score);
        score.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.28 * imageHeight));
        score.setText(String.valueOf(scoreData));
        score.setPadding(0, (int) (-0.23 * score.getTextSize()), 0, (int) (-0.26 * score.getTextSize()));
        score.setTypeface(tf_light);
        RelativeLayout.LayoutParams params8 = (RelativeLayout.LayoutParams) score.getLayoutParams();
        params8.setMargins(0, 0, 0, (int) (0.068441064 * imageHeight));
        score.setLayoutParams(params8);
        score.setTextColor(Integer.parseInt(MainActivity.currentAccent)); // Receive from MainActivity.java
        //the best score textView
        bestScore = (TextView) findViewById(R.id.bestScore);
        bestScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.04 * imageHeight));
        bestScore.setPadding(0, (int) (-0.13 * bestScore.getTextSize()), 0, (int) (-0.26 * bestScore.getTextSize()));
        bestScore.setTypeface(tf);
        bestScore.setText(String.valueOf(scoreData));
        RelativeLayout.LayoutParams params9 = (RelativeLayout.LayoutParams) bestScore.getLayoutParams();
        // params9.setMargins(0, (int)(1.150441064*imageHeight), 0, 0);//tablet
        //  params9.setMargins(0, (int)(1.250441064*imageHeight), 0, 0); //phone
        //params9.addRule(Layout.BELOW,nextBUTTON.getID());
       params9.setMargins(0, (int) (0.713 * getHeight()), 0, 0);
       // params9.setMargins(0, (int) (1.200441064 * imageHeight), 0, 0);

        bestScore.setLayoutParams(params9);
        bestScore.setTextColor(Integer.parseInt(MainActivity.currentAccent)); // Receive from MainActivity.java


//This is for saving your highscore
        //This gets the shared preferences
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        //it will load the previous score
        long score = prefs.getLong("key", 0); //0 is the default value

        if (score < scoreData) {
            //if the old score is less than the score they just got, then the new score is saved
            prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("key", scoreData);
            editor.commit();
            //This new saved score is retrived and is shown to the user.
            prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            score = prefs.getLong("key", 0); //0 is the default value
        }
        bestScore.setText("Best Score: "+String.valueOf(score));


        //playAgain = (Button) findViewById(R.id.loopGame);
        a1 = (TextView) findViewById(R.id.c1);
        a1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.037 * getHeight()));
        a1.setTypeface(tf);
        a1.setTextColor(Color.rgb(255, 255, 255));
        a2 = (TextView) findViewById(R.id.c2);
        a2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.037 * getHeight()));
        a2.setTypeface(tf);
        a2.setTextColor(Color.rgb(255, 255, 255));
        a3 = (TextView) findViewById(R.id.c3);
        a3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.037 * getHeight()));
        a3.setTypeface(tf);
        a3.setTextColor(Color.rgb(255, 255, 255));
        a4 = (TextView) findViewById(R.id.c4);
        a4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.037 * getHeight()));

        a4.setTypeface(tf);
        a4.setTextColor(Color.rgb(255, 255, 255));

        context = this;//activity is a subclass of context


        int position = i.getExtras().getInt("val");
        long timeVal = i.getExtras().getLong("time");
        String answer = i.getExtras().getString("answer");
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
        if (time > 60) {
            timeString = "Time: " + String.valueOf(timeMinute) + "m" + String.valueOf(time % 60) + "s";
        } else {
            timeString = "Time: " + String.valueOf(time) + "s";
        }

        if (position == 1) {
            result.setText("You Lost!" + " | " + timeString);
            nextButton.setText("Try Again");
        } else {
            result.setText("Got It!" + " | " + timeString);
            nextButton.setText("Next Level");
        }

    }

    // This method separates the individual characters of a String with many spaces
    public static String expand(String text) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (i < text.length() - 1) {
                result += text.charAt(i) + "      ";
            } else {
                result += text.charAt(i);
            }
        }
        return result;
    }

    public float getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        return screenWidth;
    }

    public float getHeight() {
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

    private void shareIt() {
        // This new saved score is retrived and used in the tweet
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        long score = prefs.getLong("key", 0); //0 is the default value

// Share's user's score when they click "share"
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Just got a #highscore of " + String.valueOf(score) + " on @DigitsGame. Loving this app www.digitsgame.ml";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check This Out");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}

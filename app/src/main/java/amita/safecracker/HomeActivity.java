package amita.safecracker;

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
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Twins on 24/07/2015.
 */
public class HomeActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "yBo3DqaGXDbcE3cGugMDRQwKK";
    private static final String TWITTER_SECRET = "LQWF2vrV1e0bPzEfPVF8bLhBj6fstEQraSwGs8RrrjrkGcdZJz";

    Context context;//needed to start a new intent;
    ImageButton instructions;
    ImageButton play;
    ImageButton multiplayer;
    ImageView pic;
    LinearLayout linearLayout;
    TextView best;
    SharedPreferences prefs;

int imageHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_home);
        instructions =(ImageButton) findViewById(R.id.instructions);
        play = (ImageButton) findViewById(R.id.play);
        multiplayer = (ImageButton) findViewById(R.id.multiplayer);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        context=this;//activity is a subclass of context
        Intent i = getIntent();

        //declaring fonts
        Typeface tf_light = Typeface.createFromAsset(getAssets(),
                "fonts/font_light.ttf");

        //find screen width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //scale picture and text
        pic = (ImageView) findViewById(R.id.imageView3);
        scaleImage(pic,metrics.widthPixels);

        //to center the pic in code
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(pic.getLayoutParams());
        marginParams.setMargins((int)(0.25*getWidth()), (int)(0.17*getHeight()), (int)(0.25*getWidth()), (int)(0.2*getHeight()));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        //layoutParams.setMargins(0, (int)(0.08*getHeight()), 0, (int)(0.05*getHeight()));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //pic.setLayoutParams(marginParams);
        pic.setLayoutParams(layoutParams);

        // To center the instructions/play/multiplayer buttons in code
        RelativeLayout.LayoutParams buttonsLayoutParams = new RelativeLayout.LayoutParams(linearLayout.getLayoutParams());
        buttonsLayoutParams.setMargins((int)(0.05*getWidth()), (int)(0.82*getHeight()), (int)(0.05*getWidth()), 0);
        buttonsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linearLayout.setLayoutParams(buttonsLayoutParams);

        // Creating SETTINGS/SHARE buttons
        final ImageButton shareButton = (ImageButton)  findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });
        shareButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    shareButton.setImageResource(R.drawable.share_pressed);
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // Play click sound effect
                                    MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.click);
                                    click.start();
                                    Thread.sleep(500);
                                    click.release();
                                } catch (IllegalStateException e) {

                                } catch (InterruptedException e) {

                                }
                            }
                        }).start();
                    } catch (IllegalStateException e) {

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    shareButton.setImageResource(R.drawable.share);
                }
                return false;
            }
        });
        final ImageButton settingsButton = (ImageButton)  findViewById(R.id.setting);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });
        settingsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    settingsButton.setImageResource(R.drawable.settings_pressed);
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // Play click sound effect
                                    MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.click);
                                    click.start();
                                    Thread.sleep(500);
                                    click.release();
                                } catch (IllegalStateException e) {

                                } catch (InterruptedException e) {

                                }
                            }
                        }).start();
                    } catch (IllegalStateException e) {

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    settingsButton.setImageResource(R.drawable.settings);
                }
                return false;
            }
        });


        // Creating the "BEST" textview
        best = (TextView) findViewById(R.id.best);
        //This gets the shared preferences
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        //it will load the previous score
        long score = prefs.getLong("key", 0); //0 is the default value
        best.setText("BEST: " + score);
        best.setTypeface(tf_light);
        best.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.07 * imageHeight));
        // To center
        RelativeLayout.LayoutParams bestLayoutParams = new RelativeLayout.LayoutParams(best.getLayoutParams());
        bestLayoutParams.setMargins((int)(0.05*getWidth()), (int)(0.05*getHeight()), (int)(0.05*getWidth()), 0);
        bestLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        best.setLayoutParams(bestLayoutParams);


        //creating the three INSTRUCTIONS/PLAY/MULTIPLAYER buttons
        instructions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    instructions.setImageResource(R.drawable.instructions_pressed);
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // Play click sound effect
                                    MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.click);
                                    click.start();
                                    Thread.sleep(500);
                                    click.release();
                                } catch (IllegalStateException e) {

                                } catch (InterruptedException e) {

                                }
                            }
                        }).start();
                    } catch (IllegalStateException e) {

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    instructions.setImageResource(R.drawable.instructions);
                }
                return false;
            }
        });
        play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    play.setImageResource(R.drawable.play_pressed);
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // Play click sound effect
                                    MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.click);
                                    click.start();
                                    Thread.sleep(500);
                                    click.release();
                                } catch (IllegalStateException e) {

                                } catch (InterruptedException e) {

                                }
                            }
                        }).start();
                    } catch (IllegalStateException e) {

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setImageResource(R.drawable.play);
                }
                return false;
            }
        });
        multiplayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    multiplayer.setImageResource(R.drawable.multiplayer_pressed);
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // Play click sound effect
                                    MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.click);
                                    click.start();
                                    Thread.sleep(500);
                                    click.release();
                                } catch (IllegalStateException e) {

                                } catch (InterruptedException e) {

                                }
                            }
                        }).start();
                    } catch (IllegalStateException e) {

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    multiplayer.setImageResource(R.drawable.multiplayer);
                }
                return false;
            }
        });
        click1();
        click2();
        click3();
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



    public float getWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        return screenWidth;

    }

    public float getHeight()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public void click1() {
        //Select a specific button to bundle it with the action you want
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cal = new Intent(getApplicationContext(), Instructions5.class);
                // passing array index
                startActivity(cal);
                //finish();
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
               // finish();
            }
        });
    }

    public void click3() {
        //Select a specific button to bundle it with the action you want
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cal = new Intent(getApplicationContext(), Instructions5.class);
                // passing array index
                startActivity(cal);
                // finish();
            }
        });
    }

    private void shareIt() {
        // This new saved score is retrived and used in the tweet
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
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

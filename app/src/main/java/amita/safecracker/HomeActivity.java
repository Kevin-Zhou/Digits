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
import android.widget.Space;

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
    Button start;
    Button multiplayer;
    Button instructions;
    ImageView pic;
    Space space1;
    Space space2;
    LinearLayout buttonsLayout;
    int imageHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_home);
        start = (Button) findViewById(R.id.startGame);
        multiplayer = (Button) findViewById(R.id.multiplayer);
        instructions =(Button) findViewById(R.id.Instructions);
        buttonsLayout=(LinearLayout) findViewById(R.id.buttonsLinearLayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonsLayout.getLayoutParams();
        params.setMargins(0, 0, 0, (int) (getHeight() / 7));
        buttonsLayout.setLayoutParams(params);
        instructions.setWidth((int) ((100 / 720) * getHeight() * 0.5965202983));
        instructions.setHeight((int) ((100 / 720) * getHeight() * 0.5965202983));
        multiplayer.setWidth((int) ((100 / 720) * getHeight() * 0.5965202983));
        multiplayer.setHeight((int) ((100 / 720) * getHeight() * 0.5965202983));
        start.setWidth((int) ((100 / 720) * getHeight() * 0.5965202983));
        start.setHeight((int) (( 100/ 720) * getHeight()*0.5965202983));
        space1=(Space)(findViewById(R.id.space1));
        space2=(Space)(findViewById(R.id.space2));
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) space1.getLayoutParams();
        params2.width=(int) (0.0416666667 * getWidth());
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) space2.getLayoutParams();
        params3.width=(int) (0.0416666667 * getWidth());
        System.out.println(getWidth());

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

        scaleImage(pic,(int)(getHeight()/(2)));
        //to center the pic in code
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(pic.getLayoutParams());

        marginParams.setMargins((int)(0.05*getWidth()), (int)(0.15*getHeight()), (int)(0.05*getWidth()), (int)(0.05*getHeight()));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        //layoutParams.setMargins(0, (int)(0.08*getHeight()), 0, (int)(0.05*getHeight()));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        //pic.setLayoutParams(marginParams);
        pic.setLayoutParams(layoutParams);



        // Creating SETTINGS/SHARE buttons
        final ImageButton shareButton = (ImageButton)  findViewById(R.id.share);
        RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) shareButton.getLayoutParams();
        params4.width=(int)(0.046875*getHeight());
        params4.height=(int)(0.046875*getHeight());
        params4.setMargins(0,(int)(0.046875*getHeight()),(int)(0.08333333*getWidth()),0);
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
        RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) settingsButton.getLayoutParams();
        params5.width=(int)(0.046875*getHeight());
        params5.height=(int)(0.046875*getHeight());
        params5.setMargins((int) (0.08333333 * getWidth()), (int) (0.046875 * getHeight()), 0, 0);
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


        //creating the three INSTRUCTIONS/PLAY/MULTIPLAYER buttons
        start.setTextSize((int) (0.025 * getWidth()));
        start.setTypeface(tf_light);
        start.setText(Html.fromHtml("&#9654;"));
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                }
                return false;
            }
        });
        multiplayer.setTextSize((int) (0.025 * getWidth()));
        multiplayer.setTypeface(tf_light);
        multiplayer.setText(Html.fromHtml("&#128101;"));
        multiplayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                }
                return false;
            }
        });
        instructions.setTextSize((int) (0.025 * getWidth()));
        instructions.setTypeface(tf_light);
        instructions.setText(Html.fromHtml("&#9776;"));
        instructions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                }
                return false;
            }
        });
        click1();
        click2();
        click3();
        //added code
        start.setGravity(Gravity.CENTER);
        start.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
        instructions.setGravity(Gravity.CENTER);
        instructions.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
        multiplayer.setGravity(Gravity.CENTER);
        multiplayer.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.05 * imageHeight));
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
        start.setOnClickListener(new View.OnClickListener() {
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

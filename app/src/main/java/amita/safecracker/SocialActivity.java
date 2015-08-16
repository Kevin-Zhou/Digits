package amita.safecracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import java.io.File;

public class SocialActivity extends Activity {
    SharedPreferences prefs;
    Context context;//needed to start a new intent;
    TextView share;
    TextView start;
    ImageView pic;

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_social);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });


        // This new saved score is retrived and used in the tweet
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        long score = prefs.getLong("key", 0); //0 is the default value
        File myImageFile = new File("/app/src/main/res/drawable/hero.png");
        Uri myImageUri = Uri.fromFile(myImageFile);
//        TweetComposer.Builder builder = new TweetComposer.Builder(this)
//                .text("Just got a #highscore of " + String.valueOf(score) + " on @DigitsGame. Loving this app www.digitsgame.ml")
//                .image(myImageUri);
//        builder.show();



        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("Digits: The Numbers Game")
                .setContentDescription("Just got a #highscore of " + String.valueOf(score) + " on @DigitsGame. Loving this app www.digitsgame.ml")
                .setContentUrl(Uri.parse("Just got a #highscore of \" + String.valueOf(score) + \" on @DigitsGame. Loving this app https://digitsgame.ml"))
                .build();

        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);


        // Saved gradient
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        int currentGradient1  = prefs.getInt("currentGradient1", 0); //0 is the default value
        int currentGradient2  = prefs.getInt("currentGradient2", 0); //0 is the default value
        int currentGradient3  = prefs.getInt("currentGradient3", 0); //0 is the default value
        int[] currentGradient = {currentGradient1, currentGradient2, currentGradient3};
        // Draw gradient background
        View layout = findViewById(R.id.relativeLayout);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                currentGradient);
        layout.setBackgroundDrawable(gd);



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
        share = (TextView) findViewById(R.id.shareTextView);
        share.setTextSize((int) (0.06 * getWidth()));
        share.setPadding(0,(int) (0.06 * getWidth()), 0, 0);
        share.setTypeface(tf_light);
        share.setTextColor(Integer.parseInt(MainActivity.currentAccent)); // Receive from MainActivity.java
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_social, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}

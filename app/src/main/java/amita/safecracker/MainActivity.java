package amita.safecracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import blurEffect.BlurBehind;
import blurEffect.OnBlurCompleteListener;


public class MainActivity extends Activity {
    TextView textview; //used to create the 40 textviews
    Chronometer chronometer;
    TextView[] textviews = new TextView[40]; //40 text views for each of the 40 cells, array used to store them
    GridLayout layout; //gridlayout which is used
    Rect outRect = new Rect();
    int[] location = new int[2];
    int randomNum = getRandomNumber(); //get a random number
    int[] digits = new int[4]; //array which stores the digits of the answer
    int time = 0;
    Chronometer timer;
    float width;
    float height;
    RelativeLayout view;
    int currentRow = 1; //the row which the player is currently on (starts off with row 1)
    boolean won = false; //keeps track of whether the player has won
    boolean lost = false; //keeps track of whether player has lost
    Button submitButton; //button to submit answer
    Context context;
    // The following variables are for the app background
    int[] gradient1 = new int[]{android.graphics.Color.rgb(0, 157, 255), android.graphics.Color.rgb(0, 157, 255), android.graphics.Color.rgb(0, 255, 212)};
    int[] gradient2 = new int[]{android.graphics.Color.rgb(255, 70, 0), android.graphics.Color.rgb(255, 70, 0), android.graphics.Color.rgb(255, 0, 150)};
   int[] gradient3 = new int[]{android.graphics.Color.rgb(255, 25, 0), android.graphics.Color.rgb(255, 25, 0), android.graphics.Color.rgb(255, 208, 0)};
    int[] gradient4 = new int[]{android.graphics.Color.rgb(0, 255, 68), android.graphics.Color.rgb(0, 255, 68), android.graphics.Color.rgb(242, 255, 0)};
    int[] gradient5 = new int[]{android.graphics.Color.rgb(174, 255, 0), android.graphics.Color.rgb(174, 255, 0), android.graphics.Color.rgb(0, 230, 255)};
    int[][] gradients = new int[][]{gradient1, gradient2, gradient3, gradient4, gradient5};
    int num = 0;
    int previousNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateBG(); // Makes new BG gradient


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y - getStatusBarHeight(); //we need to change this because height isn't always equal to the screen size-statuc bar height (some android phones have a bottom bar too)
        view = (RelativeLayout) findViewById(R.id.fullScreenLayout); //this is the relative layout
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        layout = (GridLayout) findViewById(R.id.grid); //set the layout to the grid layout
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
        params.setMargins(50, 0, 0, (int) (0.015625 * height)); //1280 20
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) chronometer.getLayoutParams();
        params2.setMargins((int) (height / 1280), 0, 0, 0); //1280 1
        layout.setLayoutParams(params);
        chronometer.setLayoutParams(params2);
        context = this;
        submitButton = (Button) findViewById(R.id.button);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        int dig1 = Integer.parseInt(Character.toString(Integer.toString(randomNum).charAt(0)));
        int dig2 = Integer.parseInt(Character.toString(Integer.toString(randomNum).charAt(1)));
        int dig3 = Integer.parseInt(Character.toString(Integer.toString(randomNum).charAt(2)));
        int dig4 = Integer.parseInt(Character.toString(Integer.toString(randomNum).charAt(3)));
        for (int counter = 0; counter < 40; counter++) { //creating the 40 textvies
            textview = new TextView(this); //creating a new textview
            textview.setText("0"); //by default I set the text of each textview as 0


            textview.setWidth((int) (width / (6.5))); //this is where I set the width of the cells
            textview.setHeight((int) (0.06796875 * height)); //1280 87, 2560 174
            System.out.print("Height: ");
            System.out.println(getStatusBarHeight());
            textview.setGravity(Gravity.CENTER);

            //by the way, to set the height of entire grid, you can go into the xml and change the height of the gridlayout
            //you can also set the height of each individual cell using textview.setHeight()
            textview.setTextSize((float) 0.0244375 * height); //this is where I set the font size, set it to whatever you want
            textview.setTextColor(Color.WHITE);
            Typeface tf = Typeface.createFromAsset(getAssets(),
                    "fonts/font.ttf");
            textview.setTypeface(tf);
            chronometer.setTypeface(tf);
            chronometer.setTextSize((float) 0.018328125 * height);
            textviews[counter] = textview; //store the newly created textview in the array
            layout.addView(textview);//add the textviews to the layout, the textviews get added right and  downwards, to textviews[0] would be the one in the top left corner
        }
        for (int counter = 0; counter < 36; counter++) {
            textviews[counter].setText(" ");
        }
        //set the last 4 textviews (the ones on the bottom row) to have text that are blue
        //this indicates to the player which row they are on (they start off on the bottom row)

        //fill in the digits array with the digits of the answer
        digits[0] = dig1;
        digits[1] = dig2;
        digits[2] = dig3;
        digits[3] = dig4;
        System.out.println(randomNum);
        clickSubmit(); //when the submit button is clicked
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public void clickSubmit() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the game goes on until the current row is smaller than or equal to 10 and if the player hasn't won or lost yet
                if (currentRow <= 10 && won == false && lost == false) {
                    //once the user clicks submit, the program will look through their answer
                    int numCorrect = 0; //stores how many digits are right but in the wrong position (empty circle)
                    int numExactlyCorrect = 0; //stores how many digits are in the right position (filled in circle)
                    int[] ansDigits = new int[4]; //array to store the digits of the answer
                    ArrayList<Integer> otherDigits = new ArrayList<Integer>();
                    ArrayList<Integer> otherDigitsAns = new ArrayList<Integer>();
                    //set the digits of the answer by taking the integers in the corresponding textviews
                    ansDigits[0] = Integer.parseInt(textviews[-4 * currentRow + 40].getText().toString());
                    ansDigits[1] = Integer.parseInt(textviews[-4 * currentRow + 41].getText().toString());
                    ansDigits[2] = Integer.parseInt(textviews[-4 * currentRow + 42].getText().toString());
                    ansDigits[3] = Integer.parseInt(textviews[-4 * currentRow + 43].getText().toString());

                    //go through the digits of the actual answer and given answer to see how many are correctly positioned
                    for (int counter = 0; counter < digits.length; counter++) {
                        if (digits[counter] == ansDigits[counter]) {
                            numExactlyCorrect++;

                        } else { //if it wasn't correctly positioned, add to the arraylists
                            otherDigits.add(digits[counter]);
                            otherDigitsAns.add(ansDigits[counter]);
                        }
                    }
                    for (int counter = 0; counter < 10; counter++) {
                        int num = 0;
                        int num2 = 0;
                        for (int counter2 = 0; counter2 < otherDigits.size(); counter2++) {
                            if (counter == otherDigits.get(counter2)) {
                                num++;
                            }
                            if (counter == otherDigitsAns.get(counter2)) {
                                num2++;
                            }
                        }
                        if ((num == num2) && num != 0) {
                            numCorrect++;
                        }


                    }

                    if (numExactlyCorrect == 4) //if they got 4 numbers exactly, right, that means they guessed the right number
                    {
                        won = true; //set the won variable to true
                        System.out.println("YOU WON!!! =D");
                        chronometer.stop();
                        final long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        System.out.print(elapsedMillis + " sa");
                        //Starts a new activity
                        // https://github.com/500px/500px-android-blur
                        BlurBehind.getInstance().execute(MainActivity.this, new OnBlurCompleteListener() {
                            @Override
                            public void onBlurComplete() {
                                Intent intent = new Intent(MainActivity.this, BlurredActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("val", 2);//sends the class info that the user won
                                intent.putExtra("time", elapsedMillis);
                                String ans=String.valueOf(digits[0])+String.valueOf(digits[1])+String.valueOf(digits[2])+String.valueOf(digits[3]);
                               intent.putExtra("answer", ans);
                                startActivity(intent);
                            }
                        });
                    }
                    System.out.print(numExactlyCorrect);
                    System.out.println(numCorrect);
                    currentRow++; //increment the current row by 1
                    //set the textviews in the current row to have blue font but first they need to become visible to the user
                    if (currentRow <= 10) {
                        textviews[-4 * currentRow + 40].setText("0");
                        textviews[-4 * currentRow + 41].setText("0");
                        textviews[-4 * currentRow + 42].setText("0");
                        textviews[-4 * currentRow + 43].setText("0");


                    }
                    //if the current row is greater than 10 and they still haven't won yet, they've lost
                    if (won == false && currentRow > 10) {
                        lost = true;
                        System.out.println("BOO YOU LOST");
                        chronometer.stop();
                        final long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        System.out.print(elapsedMillis + " sa");
                        //this starts the new activity
                        // https://github.com/500px/500px-android-blur
                        BlurBehind.getInstance().execute(MainActivity.this, new OnBlurCompleteListener() {
                            @Override
                            public void onBlurComplete() {
                                Intent intent = new Intent(MainActivity.this, BlurredActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("val", 1);//sends the class info that the user won
                                intent.putExtra("time", elapsedMillis);
                                String ans=String.valueOf(digits[0])+String.valueOf(digits[1])+String.valueOf(digits[2])+String.valueOf(digits[3]);
                                intent.putExtra("answer", ans);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }

    //this method just returns of a coordinate(x,y) lies within a given view
    private boolean inViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && won == false && lost == false) {
            int x = (int) event.getRawX(); //get x and y coordinates of the touch
            int y = (int) event.getRawY();
            //the touch only applied for the textviews that are in the current row
            for (int counter = -4 * currentRow + 40; counter < -4 * currentRow + 44; counter++) {
                if (inViewInBounds(textviews[counter], x, y)) {
                    int num = Integer.parseInt(textviews[counter].getText().toString()); //take the number in the texview
                    if (num == 9) { //if the number of equal to 9 set it back to 0
                        textviews[counter].setText("0");
                    } else { //if it isn't equal to 9, increment it up by 1
                        textviews[counter].setText(Integer.toString(num + 1));
                    }
                }
            }
        }
        return true;
    }

    @Override //this method doesn't do anything
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //random number generator
    public static int getRandomNumber() {
        int num1 = (int) (Math.random() * 10);
        int num2 = (int) (Math.random() * 10);
        int num3 = (int) (Math.random() * 10);
        int num4 = (int) (Math.random() * 10);
        while ((num1 == num2 || num1 == num3 || num1 == num4 || num2 == num3 || num3 == num4 || num4 == num2) || num1 == 0) {
            num1 = (int) (Math.random() * 10);
            num2 = (int) (Math.random() * 10);
            num3 = (int) (Math.random() * 10);
            num4 = (int) (Math.random() * 10);
        }
        String num1Str = Integer.toString(num1);
        String num2Str = Integer.toString(num2);
        String num3Str = Integer.toString(num3);
        String num4Str = Integer.toString(num4);
        return Integer.parseInt(num1Str + num2Str + num3Str + num4Str);
    }

    @Override //this method doesn't do anything
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

    // This method picks a new BG gradient and draws it
    public void updateBG() {
        // Pick a random background gradient
        Random random = new Random();
        num = random.nextInt(5);
        while (num == previousNum) {
             num = random.nextInt(5);
            System.out.println("ENTERED THE WHILE LOOP$@#$!#$@#$!#$#Q$!#$!#$!#R!#$!#");
        }
        // Draw gradient background
        View layout = findViewById(R.id.fullScreenLayout);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
               gradients[num]);
        layout.setBackgroundDrawable(gd);
        previousNum = num;
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

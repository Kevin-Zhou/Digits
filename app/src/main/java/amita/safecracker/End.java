package amita.safecracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * Created by Twins on 09/07/2015.
 */
public class End extends Activity {
    TextView view;//tells you if you won or lost
    TextView timeTaken;//tells you the time taken
    Button playAgain;//lets the user play again
    Context context;//needed to start a new intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);
        view = (TextView) findViewById(R.id.result);
        timeTaken = (TextView) findViewById(R.id.time);
        playAgain = (Button) findViewById(R.id.loopGame);
        context=this;//activity is a subclass of context


        Intent i = getIntent();
        int position = i.getExtras().getInt("val");
        long timeVal= i.getExtras().getLong("time");
        if(position==1){
            view.setText("You Lost");
            playAgain.setText("Try Again");
        }
        else{
            view.setText("You Won");
            playAgain.setText("Move on to the next level");
        }
        int time=(int)(timeVal/1000);//you want it in seconds not milliseconds
        int timeMinute = (int) Math.floor(time / 60);

        timeTaken.setText("Time: "+String.valueOf(timeMinute)+" h " +String.valueOf(time));
onClick();//what happens if you click the button
    }



    public void onClick() {
        playAgain.setOnClickListener(new OnClickListener()     {
            @Override
            public void onClick(View view) {
                //the game goes on until the current row is smaller than or equal to 10 and if the player hasn't won or lost yet

                Intent i= new Intent(context,MainActivity.class);
                startActivity(i);
                finish();//ends current activity
            }
        });
    }

}

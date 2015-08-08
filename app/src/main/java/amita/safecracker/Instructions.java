package amita.safecracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Twins on 24/07/2015.
 */
public class Instructions extends Activity {
    Button playAgain;//lets the user play again
    Context context;//needed to start a new intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        playAgain = (Button) findViewById(R.id.startGame);
        context=this;//activity is a subclass of context
        Intent i = getIntent();
        onClick();//what happens if you click the button
    }


    public void onClick() {
        playAgain.setOnClickListener(new View.OnClickListener()     {
            public void onClick(View view) {
                //the game goes on until the current row is smaller than or equal to 10 and if the player hasn't won or lost yet
                Intent i= new Intent(context,MainActivity.class);
                startActivity(i);
            }
        });
    }




}

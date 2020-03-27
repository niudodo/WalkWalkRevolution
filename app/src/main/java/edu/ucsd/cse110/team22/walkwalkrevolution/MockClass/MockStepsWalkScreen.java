package edu.ucsd.cse110.team22.walkwalkrevolution.MockClass;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;

public class MockStepsWalkScreen extends AppCompatActivity {
    private long steps;
    private String mocked_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_steps_walk_screen);

        steps = getIntent().getLongExtra("steps", 0);

        Button addSteps = findViewById(R.id.addsteps);
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steps = steps + 500;
            }
        });

        Button addTime = findViewById(R.id.addtime);
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mocked_time = ((TextView)findViewById(R.id.timeinput)).getText().toString();

                    /* Check if the format of the time inout is correct */

                    System.out.println(mocked_time);

                } catch (NullPointerException e) {
                    System.out.println(e);
                }
            }
        });

        Button goBack = findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("steps",steps);

                /* Pass the time on to the walkscreen activity*/

                data.putExtra("time", mocked_time);
                setResult(RESULT_OK, data);
                finish();


            }
        });

    }

}

package edu.ucsd.cse110.team22.walkwalkrevolution.MockClass;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;

public class MockStepsHomePage extends AppCompatActivity {
    private long steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_steps_home_page);

        steps = getIntent().getLongExtra("steps", 0);

        Button addSteps = findViewById(R.id.addsteps);
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steps = steps + 500;
            }
        });
        Button goBack = findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("steps",steps);
                setResult(RESULT_OK, data);
                finish();


            }
        });

    }
}

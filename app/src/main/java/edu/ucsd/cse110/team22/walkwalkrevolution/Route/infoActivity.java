package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;

public class infoActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page);

        StorageHandler s = StorageHandler.getStorage(this);
        String rId = s.retrieveItem("idOfRouteToBeDisplayed", String.class);
        Route r = s.retrieveItem(rId, Route.class);
        TextView name = (TextView) findViewById(R.id.name);
        name.setText("Route Name: "+ r.name);

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("Time: "+ r.time);

        TextView startLoc = (TextView) findViewById(R.id.startLoc);
        startLoc.setText("Starting Location: "+ r.startLocation);

        TextView notes = (TextView) findViewById(R.id.notes);
        notes.setText("Notes: "+ r.notes);

        TextView distance = (TextView) findViewById(R.id.distance);
        distance.setText("Distance: "+ r.dist);

        TextView steps = (TextView) findViewById(R.id.step);
        steps.setText("Steps: "+ r.stepCount);

        TextView fav = (TextView) findViewById(R.id.favorite);
        if(r.favorite){
            fav.setText("Favorite: Yes");
        }else{
            fav.setText("Favorite: No");
        }

        TextView terrain = (TextView) findViewById(R.id.terrain);
        terrain.setText("Terrain Type: "+ r.terrainType);

        TextView sOrT = (TextView) findViewById(R.id.street);
        sOrT.setText("Street Or Trail: "+ r.streetsOrTrail);

        TextView diff = (TextView) findViewById(R.id.difficulty);
        diff.setText("Route Name: "+ r.difficulty);

        TextView surface = (TextView) findViewById(R.id.surface);
        surface.setText("Surface: "+ r.surface);

        TextView loop = (TextView) findViewById(R.id.loop);
        loop.setText("Loop/Out and Back: "+ r.loopOrOut_and_back);

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}

package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.List;

import edu.ucsd.cse110.team22.walkwalkrevolution.MainActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.FirebaseStoreAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

public class RouteAdapter extends ArrayAdapter<Route> {
    final public static String DIST_UNIT;
    final public static String DIST_PREFIX_TEXT="Distance: ";
    final public static String TIME_PREFIX_TEXT="Time: ";
    final public static String STEP_PREFIX_TEXT="Steps: ";
    public static String distUnit = "Miles";

    static {
        DIST_UNIT = "miles";
    }

    private int resourceId;
    Context c;

    public RouteAdapter(Context context, int resource, List<Route> objects) {
        super(context, resource, objects);
        c = context;
        resourceId=resource;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Route r =getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent, false);

        TextView steps =(TextView)view.findViewById(R.id.steps);
        TextView distance =(TextView)view.findViewById(R.id.dist);
        TextView time =(TextView)view.findViewById(R.id.time);

        TextView routeName =(TextView)view.findViewById(R.id.routeName);
        if(!r.username.equals(User.myUser.getName()))
            addInitialIcon(r.username, view.findViewById(R.id.route_entry_title_ll));
        routeName.setText(r.name);
        String distText = DIST_PREFIX_TEXT+r.dist+distUnit;
        String timeText = TIME_PREFIX_TEXT + r.time;
        String stepText = STEP_PREFIX_TEXT + r.stepCount;

        distance.setText(distText);
        time.setText(timeText);
        steps.setText(stepText);

        Button edit = (Button)view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageHandler s = StorageHandler.getStorage(c);
                s.saveItem(StorageHandler.idOfRouteToBeEdited,r.id);
                switchToEditPage();
            }
        });

        /*add listener to repeat button*/
        Button repeatBtn = (Button)view.findViewById(R.id.repeat);
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageHandler s = StorageHandler.getStorage(c);
                s.saveItem(StorageHandler.idOfRouteToBeRepeated, r.id);
                switchToWalkPage();
            }
        });

        Button details = (Button)view.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageHandler s = StorageHandler.getStorage(c);
                s.saveItem(StorageHandler.idOfRouteToBeDisplayed, r.id);
                switchToInfoPage();
            }
        });

        CheckBox favoriteCheckbox = view.findViewById(R.id.route_entry_favorite);
        favoriteCheckbox.setChecked(r.favorite);

        favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                r.favorite = buttonView.isChecked();

                StorageStore ss = new FirebaseStoreAdapter();
                ss.setUp("users", User.myUser.getUid(), "routes", (Activity)c);
                ss.updateRoute(r);

                StorageHandler s = StorageHandler.getStorage(c);
                s.saveItem(r.id, r);
            }
        });


        return view;
    }



    private void addInitialIcon(String name, LinearLayout ll){
        TextView padding = new TextView((Activity)c);
        padding.setWidth(convertDpToPx(c, 4));
        padding.setHeight(convertDpToPx(c, 30));
        ll.addView(padding, 0);
        TextView initialIcon = new TextView((Activity)c);
        Drawable unwrappedDrawable = ContextCompat.getDrawable(c, R.drawable.round_button);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable,User.generateIconColor(name));
        initialIcon.setBackground(wrappedDrawable);
        initialIcon.setText(User.generateInitial(name));
        initialIcon.setTextColor(Color.WHITE);
        initialIcon.setGravity(Gravity.CENTER);
        initialIcon.setPadding(0,0,0,0);
        initialIcon.setHeight(convertDpToPx(c, 30));
        initialIcon.setWidth(convertDpToPx(c, 30));
        initialIcon.setIncludeFontPadding(false);
        ll.addView(initialIcon, 0);
    }

    public static int convertDpToPx(Context context, float dp) {
        float result = dp * context.getResources().getDisplayMetrics().density;
        return (int)result;
    }

/*--------------------------------Activity Switcher Method ---------------------------------------*/

    private void switchToEditPage(){
        Intent intent = new Intent(c, EditScreen.class);
        c.startActivity(intent);
    }

    private void switchToInfoPage(){
        Intent intent = new Intent(c, infoActivity.class);
        c.startActivity(intent);
    }

    private void switchToWalkPage(){
        Intent intent = new Intent(c, RepeatRouteActivity.class);
        intent.putExtra(RepeatRouteActivity.FITNESS_SERVICE_KEY, MainActivity.fitnessServiceKey);
        c.startActivity(intent);
    }
}

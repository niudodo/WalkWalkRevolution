package edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;

/**
 * UserTeamView class is use to display the team member of current user
 * "User Has A UserTeamView"
 */
public class UserTeamView {
    User user;
    LinearLayout teamListView;

    /**
     * Constructor of UserTeamView
     * @param user
     */
    public UserTeamView(User user){
        this.user = user;
    }

    /**
     * buildTeamView: This function will build a Team View that display all members in the team a long with
     * the pending invited member but name in gray italic; all name should have a unique initial icon
     * @param activity --- context to display team list
     */
    public void buildTeamView(Activity activity){

        //get the view from activity
        this.teamListView = activity.findViewById(R.id.teamlist);

        //reset/remove all views preexist in the list view
        this.teamListView.removeAllViews();
        this.teamListView.setOrientation(LinearLayout.VERTICAL);

        //add team member to the listview
        for(User u:user.getTeam().getMembers()){
            LinearLayout eachTeamView = new LinearLayout(activity);
            eachTeamView.setOrientation(LinearLayout.HORIZONTAL);

            TextView whiteSpace = new TextView(activity);
            whiteSpace.setWidth(20);

            TextView v = new TextView(activity);
            v.setText('\n' + u.getName());
            v.setTextSize(TypedValue.COMPLEX_UNIT_SP,17 );

            //get unique color initial icons
            FloatingActionButton fab = new FloatingActionButton(activity);
            fab.setBackgroundTintList(ColorStateList.valueOf(u.getIconColor()));
            fab.setImageBitmap(textAsBitmap(u.getInitial(), 40, Color.WHITE));

            // append all view element to the list view
            eachTeamView.addView(fab);
            eachTeamView.addView(whiteSpace);
            eachTeamView.addView(v);

            TextView spacing = new TextView(activity);
            spacing.setHeight(15);
            //append the entry to the list view
            this.teamListView.addView(spacing);
            this.teamListView.addView(eachTeamView);
        }

        //add pending invited member to the view
        for(User i : user.getTeam().getInvitations()){
                LinearLayout eachTeamView = new LinearLayout(activity);
                eachTeamView.setOrientation(LinearLayout.HORIZONTAL);

                TextView v = new TextView(activity);
                v.setText(i.getName());
                v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                v.setTextColor(Color.GRAY);
                v.setTypeface(null, Typeface.ITALIC);

                TextView whiteSpace = new TextView(activity);
                whiteSpace.setWidth(20);

                FloatingActionButton fab = new FloatingActionButton(activity);
                fab.setBackgroundTintList(ColorStateList.valueOf(i.getIconColor()));
                fab.setImageBitmap(textAsBitmap(i.getInitial(), 40, Color.WHITE));

                eachTeamView.addView(fab);
                eachTeamView.addView(whiteSpace);
                eachTeamView.addView(v);

                TextView spacing = new TextView(activity);
                spacing.setHeight(15);
                this.teamListView.addView(spacing);
                this.teamListView.addView(eachTeamView);
        }
    }

    /**
     * textAsBitmap will generate bitmap of the initial
     * @param text
     * @param textSize
     * @param textColor
     * @return
     */
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Bitmap image;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        if (width > 0 && height > 0){
            image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }else{
            image = null;
        }

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);

        return image;
    }
}

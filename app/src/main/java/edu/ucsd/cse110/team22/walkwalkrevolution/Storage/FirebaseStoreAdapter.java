package edu.ucsd.cse110.team22.walkwalkrevolution.Storage;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.team22.walkwalkrevolution.Message.IMessage;
import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.Invitation;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.Team;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

/**
 * FirebaseStoreAdapter implements StorageStore and will subscribe, send message and recieve notification
 * from firebase
 * Date: 3/6/2020
 * author: SZ
 */
public class FirebaseStoreAdapter implements StorageStore {
    Activity activity;
    String DOCUMENT_KEY;
    public final static String TAG = "FirebaseStoreAdapter:";
    public final static String NAME_KEY = "name";
    public final static String TIME_KEY = "time";
    public final static String STARTLOCATION_KEY = "startLocation";
    public final static String NOTE_KEY = "note";
    public final static String FAVORITE_KEY = "favorite";
    public final static String TERRAIN_KEY = "terrain";
    public final static String LOOP_KEY = "loop";
    public final static String TRAIL_KEY = "trail";
    public final static String SURFACE_KEY = "surface";
    public final static String DIFFICULTY_KEY = "difficulty";
    public final static String DIST_KEY = "distance";
    public final static String STEPCOUNT_KEY = "stepCount";
    public final static String LAST_WALKED_DATE_KEY = "last walked";



    CollectionReference collection;

    /**
     * setup the firebase store. invitations will be initialize to base on the keys from parameters.
     * @param collectionKey---highest level directory in database
     * @param documentKey-----sub directory in collection
     * @param messageKey------sub directory in document
     * @param activity--------activity using the store
     */
    @Override
    public void setUp(String collectionKey, String documentKey, String messageKey, Activity activity){
        collection = FirebaseFirestore.getInstance()
                .collection(collectionKey)
                .document(documentKey)
                .collection(messageKey);
        this.activity=activity;
        this.DOCUMENT_KEY=documentKey;
    }

    /**
     * subscribeToNotificationsTopic() will connect to Document associate with Document_key and device wil;
     * recieve a notification when there is a change in this document
     */
    @Override
    public void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    /**
     * sendNotification will send a message to the database under collectionReference invitations
     * @param newMessage
     */
    @Override
    public void sendNotification(Map<String, String> newMessage){
        collection.add(newMessage).addOnSuccessListener(result -> {
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    public void deleteMessage(String id){
        collection.document(id).delete();
    }

    public void updateRoute(Route route){
        collection.document(route.getRemoteId()).set(Route.getDataMap(route));
    }


    /**
     * initTeamMessageListener will pull down team information in firebase and initialized the
     * team of my user. users/[USERNAME]/team
     * @param team
     * @param user
     */
    public void initTeamMessageListener(Team team, User user){
        collection.addSnapshotListener((newChatSnapShot, error)->{
            if(error != null){
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }
            if(newChatSnapShot != null&& !newChatSnapShot.isEmpty()){
                List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                documentChanges.forEach(change->{
                    QueryDocumentSnapshot document = change.getDocument();
                    User u = new User((String)document.get(NAME_KEY), "");
                    team.addTeamMember(u);
                    user.addTeam(team);
                });
            }
        });
    }

    /**
     * initInvitationListener will pull down team invitation in firebase and initialzed the arraylist of user
     * users/[USERNAME]/invitations
     * @param user
     */
    public void initInvitationListener(User user){
        ArrayList<IMessage> msgList = new ArrayList<>();
        collection.addSnapshotListener((newChatSnapShot, error)->{
            if(error != null){
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }
            if(newChatSnapShot != null&& !newChatSnapShot.isEmpty()){
                List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                documentChanges.forEach(change->{
                    QueryDocumentSnapshot document = change.getDocument();
                    User u = new User(document.get("from").toString(), "");
                    user.addInvited(new Invitation(u.getTeam(),user));
                });
            }
        });
    }

    /** initRouteListener will pull down route data in firebase and initialized the arraylist of user
     * users/[USERNAME]/routes
     * @param user
     */
    public void initRouteListener(User user){
        collection.addSnapshotListener((newChatSnapShot, error)->{
            if(error != null){
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }
            if(newChatSnapShot !=null && !newChatSnapShot.isEmpty()){
                List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                documentChanges.forEach(change->{
                    QueryDocumentSnapshot document = change.getDocument();
                    String name = document.get(NAME_KEY).toString();
                    String time = document.get(TIME_KEY).toString();
                    String strLoc = document.get(STARTLOCATION_KEY).toString();
                    String note = document.get(NOTE_KEY).toString();
                    String favorite = document.get(FAVORITE_KEY).toString();
                    boolean favorited = favorite.equals("true");
                    String terrainType = document.get(TERRAIN_KEY).toString();
                    String loopOrOut_and_back = document.get(LOOP_KEY).toString();
                    String streetsOrTrail = document.get(TRAIL_KEY).toString();
                    String surface = document.get(SURFACE_KEY).toString();
                    String difficulty = document.get(DIFFICULTY_KEY).toString();
                    String dist = document.get(DIST_KEY).toString();
                    String stepCount = document.get(STEPCOUNT_KEY).toString();

                    Route route = new Route(user.getName(), name, time, strLoc, note, favorited, terrainType,
                            loopOrOut_and_back,streetsOrTrail,surface,difficulty,dist,stepCount);

                    route.setRemoteId(document.getId());

                    user.addRoute(route);
                    localizedRoute(route);
                });
            }
        });
    }

    /**
     * localizedRoute will store route object at local storage for later usage
     * @param r
     */
    public void localizedRoute(Route r){
        StorageHandler sh = StorageHandler.getStorage(activity);
        sh.saveItem(r.id, r);
    }


}

package edu.ucsd.cse110.team22.walkwalkrevolution.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Map;


/**
 * This class handle Storage storing and retrieval.
 */
public class StorageHandler {
    final public static String idOfRouteToBeEdited = "idOfRouteToBeEdited";
    final public static String idOfRouteToBeRepeated = "idOfRouteToBeRepeated";
    final public static String idOfRouteToBeDisplayed = "idOfRouteToBeDisplayed";

    private final Gson gson = new Gson();
    private SharedPreferences sharedPreferences = null;

    private static class StorageHolder {
         private static final StorageHandler SINGLETON = new StorageHandler();
    }

    private StorageHandler(){
    }

    public static StorageHandler getStorage (Context context) {
        return StorageHolder.SINGLETON.init(context);
    }

    private StorageHandler init(Context context){
        this.sharedPreferences = context.getSharedPreferences("database", Context.MODE_PRIVATE);
        return this;
    }

    /**
     * Saves an item in the storage
     * @param itemName the name of the item you wanna save
     * @param toStore the object to save
     */
    public void saveItem(String itemName, Object toStore){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(toStore);
        editor.putString(itemName, json);
        editor.apply();
    }

    /**
     * Retrieves an item from the storage.
     * @param itemName the name of the item you wanna retrieve
     * @param classOfT the class of item (ClassName.class)
     * @param <T> ClassName
     * @return a ClassName object
     */
    public <T> T retrieveItem(String itemName, Class<T> classOfT ){
        String json = sharedPreferences.getString(itemName, null);
        if (json == null){
            return null;
        }
        return gson.fromJson(json, classOfT);
    }

    public int sizeOfDB(){
        return sharedPreferences.getAll().size();
    }

    public String[] getAllKeys(){
        String result[] = new String[this.sizeOfDB()];
        Map<String, ?> allEntries = sharedPreferences.getAll();
        int i =0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            result[i] = entry.getKey();
            i++;
        }
        return result;
    }

    public void deleteKey(String k){
        sharedPreferences.edit().remove(k).commit();
    }

}



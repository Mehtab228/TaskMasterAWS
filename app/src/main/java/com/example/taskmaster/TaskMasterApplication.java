package com.example.taskmaster;

import android.app.Application;
import android.util.Log;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;


public class TaskMasterApplication extends Application {
    public final String TAG = "TaskMasterApplication";

    @Override
    public void onCreate(){
        super.onCreate();
        try{
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae){
            Log.e(TAG, "Error Initializing Amplify: " + ae.getMessage(), ae);
        }
    }
}

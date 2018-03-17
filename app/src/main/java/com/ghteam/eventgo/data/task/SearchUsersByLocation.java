package com.ghteam.eventgo.data.task;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ghteam.eventgo.data.entity.AppLocation;
import com.ghteam.eventgo.data.entity.UserLocationInfo;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.ghteam.eventgo.util.network.LocationUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 14.03.2018.
 */

public class SearchUsersByLocation extends BaseTask<Void, List<UserLocationInfo>> {
    private CollectionReference usersLocationInfo = FirestoreUtil.getReferenceToUserLocationInfo();

    private AppLocation currentAppLocation;
    private int rectangleHeight;
    private int rectangleWith;

    public SearchUsersByLocation(AppLocation currentAppLocation, int rectangleHeight, int rectangleWith) {
        this.currentAppLocation = currentAppLocation;
        this.rectangleHeight = rectangleHeight;
        this.rectangleWith = rectangleWith;
    }

    @Override
    public void execute(Void... params) {

        changeStatus(TaskStatus.IN_PROGRESS);

        LatLng[] rectangle = LocationUtil.calculateRectangle(currentAppLocation, rectangleHeight, rectangleWith);

        long topLeftAngle = LocationUtil.serializeLatLong(rectangle[0]);
        long bottomRightAngle = LocationUtil.serializeLatLong(rectangle[1]);

        if (topLeftAngle < bottomRightAngle) {
            long temp = topLeftAngle;
            topLeftAngle = bottomRightAngle;
            bottomRightAngle = temp;
        }

        usersLocationInfo.whereGreaterThan("serializedLocation", bottomRightAngle)
                .whereLessThan("serializedLocation", topLeftAngle).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            changeStatus(TaskStatus.SUCCESS);
                            publishResult(result.toObjects(UserLocationInfo.class));
                        } else {
                            changeStatus(TaskStatus.ERROR);
                        }
                    }
                });
    }
}

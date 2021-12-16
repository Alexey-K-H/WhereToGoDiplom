package ru.nsu.fit.wheretogo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.model.ClusterMarker;
import ru.nsu.fit.wheretogo.model.entity.Place;

public class PictureLoader {
    private static final String TAG = MapsActivity.class.getSimpleName();

    public static void loadPlaceThumbnail(Context context, Place place,
                                          int clusterNumber,
                                          List<ClusterMarker> clusterMarkers,
                                          ClusterManager<ClusterMarker> clusterManager) {
        Glide.with(context)
                .asBitmap()
                .load(place.getThumbnailLink())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        Log.i(TAG, "The image was obtained correctly");

                        ClusterMarker newClusterMarker = new ClusterMarker(
                                place.getId(),
                                new LatLng(place.getLatitude(), place.getLongitude()),
                                place.getName(),
                                "",
                                resource,
                                place
                        );

                        clusterManager.addItem(newClusterMarker);
                        clusterMarkers.add(newClusterMarker);
                        clusterManager.cluster();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

}
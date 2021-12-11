package ru.nsu.fit.wheretogo.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.model.ClusterMarker;

public class PictureLoader {
    private static final String TAG = MapsActivity.class.getSimpleName();

    public static void loadPlaceThumbnail(String link,
                                          int clusterNumber,
                                          List<ClusterMarker> clusterMarkers,
                                          ClusterManager<ClusterMarker> clusterManager) {
        try {
            Picasso.get().load(link).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.i(TAG, "The image was obtained correctly");
                    // clusterManager.updateItem() не работает,
                    // поэтому приходится заново присваивать лист маркеров при обновлении иконки
                    clusterManager.clearItems();
                    clusterManager.getClusterMarkerCollection().clear();

                    // Меняем иконку у маркера и снова присваиваем маркеры менеджеру
                    clusterMarkers.get(clusterNumber).setIconPicture(bitmap);
                    clusterManager.addItems(clusterMarkers);
                    clusterManager.cluster();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.e(TAG, "The image was not obtained");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.i(TAG, "Getting ready to get the image");
                }
            });
        } catch (java.lang.IllegalArgumentException e){
            Log.e(TAG, "The image was not obtained: NULL url");
        }
    }
}

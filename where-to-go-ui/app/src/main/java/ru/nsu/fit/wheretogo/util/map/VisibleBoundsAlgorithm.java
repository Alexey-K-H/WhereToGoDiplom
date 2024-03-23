package ru.nsu.fit.wheretogo.util.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithm;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.quadtree.PointQuadTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class VisibleBoundsAlgorithm<T extends ClusterItem> extends NonHierarchicalDistanceBasedAlgorithm<T>
        implements ScreenBasedAlgorithm<T> {

    private final float MIN_ZOOM_LEVEL = 12.8f;
    private final GoogleMap map;
    private LatLngBounds bounds;

    public VisibleBoundsAlgorithm(GoogleMap map) {
        this.map = map;
        bounds = map.getProjection().getVisibleRegion().latLngBounds;
    }

    @Override
    protected Collection<QuadItem<T>> getClusteringItems(PointQuadTree<QuadItem<T>> quadTree, float zoom) {

        List<QuadItem<T>> items = new ArrayList<>(quadTree.search(new Bounds(0, 1, 0, 1)));
        items.removeIf(item -> !bounds.contains(item.getPosition()));

        if (zoom < MIN_ZOOM_LEVEL) {
            return Collections.emptySet();
        }

        return items;
    }

    public void updateBounds() {
        bounds = map.getProjection().getVisibleRegion().latLngBounds;
    }

    @Override
    public boolean shouldReclusterOnMapMovement() {
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition position) {
        //required for clustering algorithm
    }
}

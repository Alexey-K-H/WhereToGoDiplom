package ru.nsu.fit.wheretogo.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.entity.Place;
import ru.nsu.fit.wheretogo.service.PlaceService;
import ru.nsu.fit.wheretogo.util.ImageResizer;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private final int height;
    private final Context context;
    private final List<Place> places = new ArrayList<>();
    private final List<Bitmap> images = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rowName;
        ImageView rowImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.favourite_name);
            rowImage = itemView.findViewById(R.id.imageView);
        }
    }

    public RecyclerViewAdapter(Context context, int height) {
        this.context = context;
        this.height = height;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_favourite_item, parent, false);
        view.setOnClickListener(v -> {
            TextView rowName = v.findViewById(R.id.favourite_name);
            Toast.makeText(context, "Clicked Item: " + rowName.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rowName.setText(places.get(position).getName());

        Bitmap icon = ImageResizer.resize(images.get(position),
                images.get(position).getWidth(),
                height / 5);
        holder.rowImage.setImageBitmap(icon);

        holder.itemView.setOnClickListener(view -> showPlaceDescription(position));
    }

    private void showPlaceDescription(int position) {
        View modelBottomSheet = LayoutInflater.from(context).inflate(
                R.layout.place_info_bottom_sheet,
                null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);

        TextView placeName = modelBottomSheet.findViewById(R.id.place_name);
        placeName.setText(places.get(position).getName());

        ImageView placeImage = modelBottomSheet.findViewById(R.id.place_image);
        placeImage.setImageBitmap(images.get(position));

        PlaceService placeService = ServiceGenerator.createService(PlaceService.class);
        Call<Place> placeCall = placeService.getPlace(places.get(position).getId());
        placeCall.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                TextView placeDescription = modelBottomSheet.findViewById(R.id.place_description);
                if (response.body() == null) {
                    Log.e(TAG, "Тело запроса данных места пустое");
                } else {
                    String descriptionText = response.body().getDescription();
                    descriptionText = descriptionText.substring(0, 1).toUpperCase()
                            + descriptionText.substring(1);
                    placeDescription.setText(descriptionText);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Place> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
        modelBottomSheet.findViewById(R.id.back_btn).setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(modelBottomSheet);
        dialog.show();
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<Bitmap> getImages() {
        return images;
    }


}

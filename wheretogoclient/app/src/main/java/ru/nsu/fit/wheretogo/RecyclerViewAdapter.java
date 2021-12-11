package ru.nsu.fit.wheretogo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.nsu.fit.wheretogo.util.ImageResizer;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int height;
    // Declare variables to store data from the constructor
    private final Context context;
    private final String[] names;
    private final int[] images;

    // Create a static inner class and provide references to all the Views for each data item.
    // This is particularly useful for caching the Views within the item layout for fast access.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare member variables for all the Views in a row
        TextView rowName;
        ImageView rowImage;

        // Create a constructor that accepts the entire row and search the View hierarchy to find each subview
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Store the item subviews in member variables
            rowName = itemView.findViewById(R.id.favourite_name);
            rowImage = itemView.findViewById(R.id.imageView);
        }
    }

    // Provide a suitable constructor
    public RecyclerViewAdapter(Context context, String[] names, int[] images, int height) {
        // Initialize the class scope variables with values received from constructor
        this.context = context;
        this.names = names;
        this.images = images;
        this.height = height;
    }

    // Create new views to be invoked by the layout manager
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a LayoutInflater object
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.single_favourite_item, parent, false);
        // To attach OnClickListener
        view.setOnClickListener(v -> {
            TextView rowName = v.findViewById(R.id.favourite_name);
            Toast.makeText(context, "Clicked Item: " + rowName.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        // Return a new holder instance
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    // Replace the contents of a view to be invoked by the layout manager
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the contents of the View with that element
        holder.rowName.setText(names[position]);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), images[position]);
        icon = ImageResizer.resize(icon, icon.getWidth(), height / 5);
        holder.rowImage.setImageBitmap(icon);
    }


}

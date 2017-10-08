package comp90018.project2.weather.location_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import comp90018.project2.weather.LocationListActivity;
import comp90018.project2.weather.R;
import comp90018.project2.weather.WeatherActivity;

/**
 * Adapter to bind a location item List to a view
 */
public class LocationItemAdapter extends ArrayAdapter<LocationItem> {

    /**
     * Adapter context
     */
    private Context mContext;

    /**
     * Adapter View layout
     */
    private int mLayoutResourceId;

    public LocationItemAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final LocationItem currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final Button locationButton = (Button) row.findViewById(R.id.locationItem);
        locationButton.setText(currentItem.getText());
        locationButton.setEnabled(true);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, WeatherActivity.class);
                intent.putExtra("location", locationButton.getText());
                mContext.startActivity(intent);
            }
        });

        locationButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mContext instanceof LocationListActivity) {
                        LocationListActivity activity = (LocationListActivity) mContext;
                        activity.removeItem(currentItem);
                }
                return false;
            }
        });

        return row;
    }

}
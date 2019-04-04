package com.example.android.bodashops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.example.android.bodashops.R;

public class RegistrationStepsAdapter extends ArrayAdapter<String> {

    private final String[] labels = {"Step 1", "Step 2", "Step 3", "Step 4"};

    public RegistrationStepsAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mLabel.setText(getItem(position));

        holder.mStepsView.setCompletedPosition(position % labels.length)
                .setLabels(labels)
                .setBarColorIndicator(
                        getContext().getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(getContext().getResources().getColor(R.color.orange))
                .setLabelColorIndicator(getContext().getResources().getColor(R.color.orange))
                .drawView();

        return convertView;
    }

    static class ViewHolder {

        TextView mLabel;
        StepsView mStepsView;

        public ViewHolder(View view) {
            mLabel = (TextView) view.findViewById(R.id.label);
            mStepsView = (StepsView) view.findViewById(R.id.stepsView);
        }
    }
}
package com.example.ajoy3.steganography;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Malabika on 12/1/2015.
 */
public class MyListAdapter  extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;
    Integer selected_position = -1;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;

    public MyListAdapter(Activity context, List<Model> list) {
        super(context, R.layout.list_row, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.listlabel);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkitem);

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if(isChecked){
//                        selected_position = position;
//                    }else{
//                        selected_position = -1;
//                    }
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setSelected(buttonView.isSelected()); // Set the value of checkbox to maintain its state.
//                    notifyDataSetChanged();
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.listlabel, viewHolder.text);
            convertView.setTag(R.id.checkitem, viewHolder.checkBox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBox.setTag(position); // This line is important.

        viewHolder.text.setText(list.get(position).getName());
        viewHolder.checkBox.setChecked(list.get(position).isSelected());

        return convertView;
    }
}

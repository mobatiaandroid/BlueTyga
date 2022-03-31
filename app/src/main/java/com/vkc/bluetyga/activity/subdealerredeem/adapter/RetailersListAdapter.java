package com.vkc.bluetyga.activity.subdealerredeem.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vkc.bluetyga.R;
import com.vkc.bluetyga.activity.dealers.model.DealerModel;
import com.vkc.bluetyga.activity.subdealerredeem.model.RetailerModel;
import com.vkc.bluetyga.appcontroller.AppController;

import java.util.ArrayList;

/**
 * Created by user2 on 3/4/18.
 */
public class RetailersListAdapter  extends BaseAdapter {
    Activity mActivity;

    LayoutInflater mLayoutInflater;
    ArrayList<DealerModel> listModel;
    private ArrayList<DealerModel> mOriginalValues; // Original Values
    private ArrayList<DealerModel> mDisplayedValues;

    public RetailersListAdapter(Activity mActivity,
                              ArrayList<RetailerModel> listModel) {

        this.mActivity = mActivity;
        // AppController.listDealers.clear();
        AppController.listRetailers = listModel;
        // this.notifyDataSetChanged();
        //System.out.println("Length" + listModel.size());
        // mLayoutInflater = LayoutInflater.from(mActivity);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return AppController.listRetailers.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }




    static class ViewHolder {

        TextView textName;
        CheckBox checkBox;

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View v = view;

        LayoutInflater mInflater = (LayoutInflater) mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater
                    .inflate(R.layout.item_dealer_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textName = (TextView) view.findViewById(R.id.textViewName);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_dealer);

            viewHolder.checkBox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {

                         /*   String idValue = AppController.dealersModels.get(
                                    position).getId();*/

                            int getPosition = (Integer) buttonView.getTag();
                            AppController.listRetailers.get(getPosition)
                                    .setIsChecked(isChecked);

                        }
                    });
            //   viewHolder.checkBox.setOnCheckedChangeListener(null);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.checkBox.setTag(position);
        viewHolder.textName.setText(AppController.listRetailers.get(position)
                .getUser_name());


        viewHolder.checkBox.setChecked(AppController.listRetailers.get(
                position).isChecked());

        return view;
    }

}

package com.vkc.bluetyga.activity.inbox.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vkc.bluetyga.R;
import com.vkc.bluetyga.activity.inbox.model.InboxModel;

import java.util.ArrayList;

/**
 * Created by user2 on 4/12/17.
 */
public class InboxAdapter extends BaseAdapter {
    Activity mActivity;
    ArrayList<InboxModel> listInbox;


    public InboxAdapter(Activity mActivity,
                        ArrayList<InboxModel> listInbox) {

        this.mActivity = mActivity;
        this.listInbox = listInbox;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listInbox.size();
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
        ImageView imageTile;
        TextView textTitle;
        TextView textDate;
        LinearLayout linearLayout;

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
                    .inflate(R.layout.item_inbox_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textTitle = (TextView) view.findViewById(R.id.textTitle);

            viewHolder.textDate = (TextView) view.findViewById(R.id.textDate);
            viewHolder.imageTile = (ImageView) view.findViewById(R.id.image_inbox);
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.thumbnail);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textTitle.setText(listInbox.get(position).getTitle());
        viewHolder.textDate.setText(listInbox.get(position).getCreatedon());

        if (listInbox.get(position).getImage().equals("")) {
            viewHolder.imageTile.setVisibility(View.INVISIBLE);
        } else {
            // viewHolder.imageTile.setVisibility(View.VISIBLE);
            viewHolder.imageTile.setVisibility(View.VISIBLE);
            String image_url = listInbox.get(position).getImage();
           // image_url=image_url.replaceAll(" ","%20");
            Picasso.with(mActivity).load(listInbox.get(position).getImage()).into(viewHolder.imageTile);
        }


      /*  viewHolder.textName.setText(AppController.listDealers.get(position)
                .getName());


        viewHolder.checkBox.setChecked(AppController.listDealers.get(
                position).isChecked());*/

        return view;
    }

}


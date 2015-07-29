package com.hermitfang.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        if (convertView == null) { // is recycled view ?
            // create one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivThumb = (ImageView) convertView.findViewById(R.id.ivThumb);

        tvCaption.setText(photo.caption);
        tvUser.setText(photo.username);
        tvComment1.setText(photo.comments);
        tvLikes.setText(photo.likes);
        // tvUsername.setText() //still have a username remains unused

        ivPhoto.setImageResource(0); // clear image first (this might be a recycled view)
        ivThumb.setImageResource(0); // as above
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto); // call picasso lib to load image
        Picasso.with(getContext()).load(photo.profilePicture).into(ivThumb); // call picasso lib to load image

        return convertView;
    }

}

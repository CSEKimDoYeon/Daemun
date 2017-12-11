package com.example.kimdoyeon.daemun.Daemun_main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kimdoyeon.daemun.Daemun_DB.Events;
import com.example.kimdoyeon.daemun.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by KimDoYeon on 2017-12-11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Events> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mTextView1 = (TextView)view.findViewById(R.id.card_textview1);
            mTextView2 = (TextView)view.findViewById(R.id.card_textview2);
            mTextView3 = (TextView)view.findViewById(R.id.card_textview3);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Events> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView1.setText("행사이름 : "+mDataset.get(position).event_name);
        holder.mTextView2.setText("일시 : "+mDataset.get(position).event_date);
        holder.mTextView3.setText("장소 : "+mDataset.get(position).event_place);
        //holder.mImageView.setImageResource(mDataset.get(position).event_img);
        //holder.mImageView.setImageBitmap(mDataset.get(position).event_bm);

        new LoadImagefromUrl().execute(holder.mImageView, mDataset.get(position).event_img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap> { // 이미지를 불러오는 아싱크테스크
    ImageView ivPreview = null;

    @Override
    protected Bitmap doInBackground( Object... params ) {
        this.ivPreview = (ImageView) params[0];
        String url = (String) params[1];
        return loadBitmap( url );
    }
    @Override
    protected void onPostExecute( Bitmap result ) {
        super.onPostExecute( result );
        ivPreview.setImageBitmap( result );
    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}




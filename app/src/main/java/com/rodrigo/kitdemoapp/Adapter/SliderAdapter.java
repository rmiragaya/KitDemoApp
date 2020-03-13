package com.rodrigo.kitdemoapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private static final String TAG = "SliderAdapter";


    private List<SliderItem> sliderItemList;
    private OnItemClickListener mListener;
//    private ViewPager2 viewPager2;

    /* interface for onclick on product in viewPager */
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public SliderAdapter(List<SliderItem> sliderItemList) {
        this.sliderItemList = sliderItemList;
//        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_pager_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItemList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageSlider);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        void setImage(SliderItem sliderItem){
            //convertBitmapToUri
//            Picasso.get().load(sliderItem.getImage().get)
//                    .placeholder(R.drawable.bandera_argentina)
//                    .error(R.drawable.bandera_brasil)
//                    .into(imageView);
            imageView.setImageBitmap(sliderItem.getImage());
        }
    }

    public void updateData(List<SliderItem> newsliderItemList){
        Log.d(TAG, "UpdateData: call");
        this.sliderItemList = newsliderItemList;
        this.notifyDataSetChanged();
    }
}

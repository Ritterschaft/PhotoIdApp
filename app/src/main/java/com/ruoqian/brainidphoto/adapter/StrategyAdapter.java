package com.ruoqian.brainidphoto.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.longtu.base.util.StringUtils;
import com.ruoqian.brainidphoto.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，图片
 */
public class StrategyAdapter extends BannerAdapter<String, StrategyAdapter.ImageHolder> {

    private Context context;

    public StrategyAdapter(List<String> mDatas, Context context) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.context = context;
    }

    //更新数据
    public void updateData(List<String> data) {
        //这里的代码自己发挥，比如如下的写法等等
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }


    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageHolder(imageView);
    }

    @Override
    public void onBindView(ImageHolder holder, String data, int position, int size) {
        if (!StringUtils.isEmpty(data)) {
            RoundedCorners roundedCorners = new RoundedCorners(3);
            RequestOptions options = new RequestOptions()
                    .bitmapTransform(roundedCorners)
                    .override(946, 448)
                    .error(R.mipmap.strategy_defalut_cover)
                    .placeholder(R.mipmap.strategy_defalut_cover);
            Glide.with(context)
                    .load(data)
                    .apply(options)
                    .into(holder.imageView);
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageHolder(@NonNull View view) {
            super(view);
            this.imageView = (ImageView) view;
        }
    }

}

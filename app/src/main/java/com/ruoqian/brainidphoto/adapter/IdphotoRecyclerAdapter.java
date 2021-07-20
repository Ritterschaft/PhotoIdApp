package com.ruoqian.brainidphoto.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.longtu.base.util.StringUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.dao.Idphoto;
import com.ruoqian.lib.listener.OnRecyclerViewItemClickListener;

import java.util.List;

public class IdphotoRecyclerAdapter extends RecyclerView.Adapter<IdphotoRecyclerAdapter.IdphotoViewholder> implements View.OnClickListener, View.OnLongClickListener {

    private List<Idphoto> listIdphotos;
    private OnRecyclerViewItemClickListener onItemClickListener;
    private Context context;

    public IdphotoRecyclerAdapter(List<Idphoto> listIdphotos, Context context, OnRecyclerViewItemClickListener onItemClickListener) {
        this.listIdphotos = listIdphotos;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public IdphotoViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.idphoto_item, parent, false);
        return new IdphotoViewholder(view);
    }

    @Override
    public void onBindViewHolder(IdphotoViewholder holder, final int position) {
        Idphoto idphoto = listIdphotos.get(position);
        holder.viewLine.setVisibility(View.GONE);
        if (position == 0) {
            holder.viewLine.setVisibility(View.VISIBLE);
        }
        if (idphoto != null) {
            TextPaint tp = holder.tvName.getPaint();
            tp.setFakeBoldText(true);
            if (!StringUtils.isEmpty(listIdphotos.get(position).getName())) {
                holder.tvName.setText(listIdphotos.get(position).getName());
            }

            try {
                holder.tvDesc.setText("像素：" + listIdphotos.get(position).getPWidth() + "×" + listIdphotos.get(position).getPHeight() + "px"
                        + "\n" + "冲印：" + listIdphotos.get(position).getMWidth() + "×" + listIdphotos.get(position).getMHeight() + "mm");
            } catch (Exception e) {
            }
        }
        holder.rlIdphoto.setTag(position);
        holder.rlIdphoto.setOnClickListener(this);
        holder.rlIdphoto.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return listIdphotos.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlIdphoto) {
            if (onItemClickListener == null) {
                return;
            }
            onItemClickListener.OnItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onItemClickListener == null) {
            return true;
        }
        if (v.getId() == R.id.rlIdphoto) {
            onItemClickListener.OnItemLongClick(v, (Integer) v.getTag());
        }
        return true;
    }

    public class IdphotoViewholder extends RecyclerView.ViewHolder {
        private View itemView;
        private View viewLine;
        private RelativeLayout rlIdphoto;
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvDesc;

        public IdphotoViewholder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            viewLine = itemView.findViewById(R.id.viewLine);
            rlIdphoto = itemView.findViewById(R.id.rlIdphoto);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }

}

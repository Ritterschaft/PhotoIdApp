package com.ruoqian.brainidphoto.adapter;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.longtu.base.util.StringUtils;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.dao.Idphoto;

import java.util.List;

public class IdphotoAdapter extends BaseAdapter {

    private List<Idphoto> listIdphotos;

    public IdphotoAdapter(List<Idphoto> listIdphotos) {
        this.listIdphotos = listIdphotos;
    }

    @Override
    public int getCount() {
        return listIdphotos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        IdphotoItemView idphotoItemView;
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.idphoto_item, null);
            idphotoItemView = new IdphotoItemView();
            idphotoItemView.ivIcon = convertView.findViewById(R.id.ivIcon);
            idphotoItemView.tvName = convertView.findViewById(R.id.tvName);
            idphotoItemView.tvDesc = convertView.findViewById(R.id.tvDesc);
            idphotoItemView.viewLine = convertView.findViewById(R.id.viewLine);
            convertView.setTag(idphotoItemView);
        } else {
            idphotoItemView = (IdphotoItemView) convertView.getTag();
        }

        idphotoItemView.viewLine.setVisibility(View.GONE);

        TextPaint tp = idphotoItemView.tvName.getPaint();
        tp.setFakeBoldText(true);

        if (!StringUtils.isEmpty(listIdphotos.get(position).getName())) {
            idphotoItemView.tvName.setText(listIdphotos.get(position).getName());
        }

        try {
            idphotoItemView.tvDesc.setText("像素：" + listIdphotos.get(position).getPWidth() + "×" + listIdphotos.get(position).getPHeight() + "px"
                    + "\n" + "冲印：" + listIdphotos.get(position).getMWidth() + "×" + listIdphotos.get(position).getMHeight() + "mm");
        } catch (Exception e) {
        }

        return convertView;
    }

    class IdphotoItemView {
        View viewLine;
        ImageView ivIcon;
        TextView tvName;
        TextView tvDesc;
    }
}

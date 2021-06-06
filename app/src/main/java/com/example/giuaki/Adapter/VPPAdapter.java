package com.example.giuaki.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.giuaki.Entities.VanPhongPham;
import com.example.giuaki.R;

import java.util.List;

public class VPPAdapter extends BaseAdapter {
    private List<VanPhongPham> list;
    private Activity activity;


    public VPPAdapter(Activity activity, List<VanPhongPham> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();

        // getLayout
         view = inflater.inflate(R.layout.vpptemplate, null);

        // getControl.
//        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
//        tvName.setText(items[i]);

        // Trả về view kết quả.
        return view;
    }
}

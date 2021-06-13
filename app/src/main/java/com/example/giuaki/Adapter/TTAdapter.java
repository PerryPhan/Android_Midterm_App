package com.example.giuaki.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.giuaki.Entities.VanPhongPham;
import com.example.giuaki.R;

import java.util.List;

public class TTAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    LayoutInflater inflater;


    public TTAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater.from(context));
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
        // getLayout
         view = inflater.inflate(R.layout.trangthaitemplate, null);

        // getControl.
        String trangthai = list.get(position);
        TextView ttview = (TextView) view.findViewById(R.id.trangthaiview);
        ttview.setText( trangthai );

        // setDecorate
        switch (trangthai){
            case "OPENNING":
                ttview.setBackgroundColor(
                        context.getResources().getColor(R.color.openning_color)
                );
                break;
            case "CONFIRMED":
                ttview.setBackgroundColor(
                        context.getResources().getColor(R.color.confirmed_color)
                );
                break;
            case "DELIVERED":
                ttview.setBackgroundColor(
                        context.getResources().getColor(R.color.delivered_color)
                );
                break;
            case "CANCELED":
                ttview.setBackgroundColor(
                        context.getResources().getColor(R.color.cancel_color)
                );
                break;
            default:
                break;
        }
        // Trả về view kết quả.
        return view;
    }
}

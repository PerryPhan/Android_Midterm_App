package com.example.giuaki.Chart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.example.giuaki.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ThongkeChart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke_chart);
        Bundle b = getIntent().getExtras();
        // Fetch data
        List<String> data = b.getStringArrayList("data");
        testData(data);

        // Control
        PieChart pieChart = findViewById(R.id.TK_chart);

        // Load data
        ArrayList<PieEntry> countVPPfromPB = new ArrayList<>();
        String tenPB = "";
        int chiso = 0;
        if( data.size() == 0) return;
        for( int i =0 ; i < data.size(); i++){
            if( i % 2 == 0 ){
                tenPB = data.get(i);
            }else if( i % 2 != 0){
                if( tenPB.equalsIgnoreCase("")) break;
                chiso = Integer.parseInt(data.get(i));
                if(chiso == 0) continue;
                countVPPfromPB.add(new PieEntry(chiso, tenPB));
                tenPB = "";
                chiso = 0;
            }
        }

        // Cấu hình pieDataSet = PieEntry
        PieDataSet pieDataSet = new PieDataSet(countVPPfromPB, "Phòng");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        // Cấu hình pieData = pieDataset
        PieData pieData = new PieData(pieDataSet);

        // Final
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("THỐNG KÊ SỐ LƯỢNG VPP ĐƯỢC CẤP CHO CÁC PHÒNG BAN");
        pieChart.animate();
    }
    public void testData( List<String> datas ){
        for( String data : datas){
            Log.d("data",data);
        }
    }


}
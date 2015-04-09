package com.ktl.moment.android.component.wheel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class HoloWheelActivity extends Activity {

    
    private AreaDbOption ado;
    private ArrayList<Area> areas = new ArrayList<Area>();
    private ArrayList<CityAdapter> cityAdapters = new ArrayList<HoloWheelActivity.CityAdapter>();
    private WheelView cities;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private WheelView province;
    private static final int AREA = 0x000000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cities_holo_layout);
        ViewUtils.inject(this);
        initData();
        province = (WheelView) findViewById(R.id.province);
        province.setWheelBackground(R.drawable.wheel_bg_holo);
        province.setWheelForeground(R.drawable.wheel_val_holo);
        province.setShadowColor(0x88FFFFFF, 0x00FFFFFF, 0x00FFFFFF);
        province.setVisibleItems(3);
        provinceAdapter = new ProvinceAdapter(this);
        province.setViewAdapter(provinceAdapter);
        province.setCurrentItem(0);
        cityAdapter =cityAdapters.get(0);
        
        province.addChangingListener(new OnWheelChangedListener() {
            
            

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityAdapter = cityAdapters.get(newValue);
                cities.setViewAdapter(cityAdapter);
                province.setCurrentItem(newValue);
            }
        });

        
        cities = (WheelView) findViewById(R.id.city);
        cities.setWheelBackground(R.drawable.wheel_bg_holo);
        cities.setWheelForeground(R.drawable.wheel_val_holo);
        cities.setShadowColor(0x88FFFFFF, 0x00FFFFFF, 0x00FFFFFF);
        cities.setVisibleItems(3);
        cities.setCurrentItem(0);
        cities.setViewAdapter(cityAdapters.get(0));
        cities.addChangingListener(new OnWheelChangedListener() {
            
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cities.setCurrentItem(newValue);
            }
        });
    }

    private void initData() {
        ado = new AreaDbOption ();
        areas = (ArrayList<Area>) ado.getAreas(this, "Provinces.xml");
        for(int i=0;i<areas.size();i++){
            CityAdapter cityAdapter = new CityAdapter(this,areas.get(i).getCitys());
            cityAdapters.add(cityAdapter);
        }
    }

    /**
     * Adapter for countries
     */
    private class CityAdapter extends AbstractWheelTextAdapter {
        // City names
        //final String cities[] = new String[] {"湖北", "四川", "河南", "北京", "福建","湖南","黑龙江"};
        private ArrayList<String> cities;
        /**
         * Constructor
         */
        protected CityAdapter(Context context,ArrayList<String> cities) {
            super(context, R.layout.city_holo_layout, NO_RESOURCE);
            setItemTextResource(R.id.city_name);
            this.cities = cities;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return cities.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return cities.get(index);
        }
    }
    /**
     * Adapter for countries
     */
    private class ProvinceAdapter extends AbstractWheelTextAdapter {
        // City names
        
        /**
         * Constructor
         */
        protected ProvinceAdapter(Context context) {
            super(context, R.layout.city_holo_layout, NO_RESOURCE);
            setItemTextResource(R.id.city_name);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return areas.size();
        }
        
        @Override
        public CharSequence getItemText(int index) {
            return areas.get(index).getProvince();
        }
    }
    
    @OnClick({R.id.btn_wheel_cancel,R.id.btn_wheel_confirm})
    public void onclick(View v) {
        switch(v.getId()){
        case R.id.btn_wheel_cancel:
            finish();
            break;
        case R.id.btn_wheel_confirm:
            String provinceText=(String) provinceAdapter.getItemText(province.getCurrentItem());
            int index=cities.getCurrentItem();
            String cityText=(String) cityAdapter.getItemText(index);
            Intent data=new Intent();
            data.putExtra("provinceText", provinceText);
            data.putExtra("cityText", cityText);
            HoloWheelActivity.this.setResult(AREA, data);
            finish();
            break;
            
        }
    }
    
}

package com.jyqqhw.gridpagerview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private GridPagerView gridPagerView;
    private List<String> lists = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData(){
        for(int i=0; i<28; i++){
            lists.add("棒棒哒 "+i);
        }
    }

    private void initView(){
        gridPagerView = (GridPagerView) findViewById(R.id.simple_grid_view);
        MyAdapter myAdapter = new MyAdapter(this, lists);
        gridPagerView.setAdapter(myAdapter);
        gridPagerView.setOnItemClickListener(new CustomLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(CustomLinearLayout<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "click item "+position, Toast.LENGTH_SHORT).show();
            }
        });
        gridPagerView.setOnItemLongClickListener(new CustomLinearLayout.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(CustomLinearLayout<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "long click item "+ position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        gridPagerView.setOnPageChangeListener(new CustomLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this, "current page is "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    class MyAdapter extends BaseAdapter{

        private List<String> stringList;
        private Context c;
        private LayoutInflater layoutInflater;

        public MyAdapter(Context context, List<String> lists){
            this.c = context;
            this.stringList = lists;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return stringList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(null == convertView){
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.layout_item_1, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.item_text);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(stringList.get(position));
            return convertView;
        }
    }

    class ViewHolder{
        TextView textView;
    }


}

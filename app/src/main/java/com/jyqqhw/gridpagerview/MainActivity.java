package com.jyqqhw.gridpagerview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jyqqhw.gridpagerview.resolver.CustomLinearLayout;
import com.jyqqhw.gridpagerview.resolver.GridPagerIndicator;
import com.jyqqhw.gridpagerview.resolver.GridPagerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private GridPagerView gridPagerView;
    private GridPagerIndicator gridPagerIndicator;
    private List<String> lists = new ArrayList<String>();
    private MyAdapter myAdapter;

    private Button addData, delData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initData();
        initView();

        initDialog();
    }


    private void initDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("我是标题");
        builder.setMessage("我是内容");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public Point test(int width, int height){

        String packageName = this.getPackageName();
        if( packageName.contains("bbk")|| packageName.contains("eebbk") ||
                packageName.equals("com.android.systemui") ||
                packageName.equals("com.android.camera2") ||
                packageName.equals("com.android.settings") ) {
        } else {

        }

        return new Point(width, height);
    }

    private void initData(){
        for(int i=0; i<28; i++){
            lists.add("棒棒哒 "+i);
        }
    }

    private void initView(){
        gridPagerView = (GridPagerView) findViewById(R.id.simple_grid_view);
        myAdapter = new MyAdapter(this, lists);
        gridPagerView.setAdapter(myAdapter);
        gridPagerView.setOnItemClickListener(new CustomLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(CustomLinearLayout<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.item_text);
                String txt = textView.getText().toString();
                Toast.makeText(MainActivity.this, "click item "+position+", text="+txt, Toast.LENGTH_SHORT).show();
            }
        });
        gridPagerView.setOnItemLongClickListener(new CustomLinearLayout.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(CustomLinearLayout<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.item_text);
                String txt = textView.getText().toString();
                Toast.makeText(MainActivity.this, "long click item "+ position+", text="+txt, Toast.LENGTH_SHORT).show();
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
            public void onPageDataSetChanged(boolean changed, int position) {

            }

        });
        gridPagerIndicator = (GridPagerIndicator) findViewById(R.id.simple_indicator);
        gridPagerIndicator.setGridPagerView(gridPagerView);

        addData = (Button) findViewById(R.id.add_data);
        delData = (Button) findViewById(R.id.del_data);
        MyClickListener myClickListener = new MyClickListener();
        addData.setOnClickListener(myClickListener);
        delData.setOnClickListener(myClickListener);
    }


    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.add_data:
                    lists.add("12345反击的卡拉基督教发");
                    myAdapter.notifyDataSetChanged();
                    break;
                case R.id.del_data:
                    if(lists.size()>0){
                        lists.remove(0);
                        myAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
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
                convertView = layoutInflater.inflate(R.layout.layout_item_1, parent, false);
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

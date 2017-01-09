package ntust.csie.app.orderingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class menu extends AppCompatActivity {
    ListView lvmenu;
    //ListAdapter lvadapter = null;
    ViewPager viewPager;
    ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Intent intent=getIntent();
        String message = intent.getStringExtra(NearFragment.POS);
        Log.d("a"+message,"a"+message);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        adapter=new ImageAdapter(this,message);
        viewPager.setAdapter(adapter);

        //lvmenu = (ListView) findViewById(R.id.lvmenu);
        //lvadapter = new ListAdapter(this,message);
        //lvmenu.setAdapter(lvadapter);

        //adapter = new MyAdapter(this);
        //lvmenu.setAdapter(adapter);
    }
}

/*class MyAdapter extends BaseAdapter {

        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        //Bundle bundle = new Bundle();
        //final recommendFragment fragment = new recommendFragment();

        @Override
        public int getCount() {return 5;}

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }*/
package ntust.csie.app.orderingsystem;

import android.content.Context;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by brian on 2016/12/27.
 */

public class ImageAdapter extends PagerAdapter {
    private Context context;
    ArrayList<Integer> resID = new ArrayList<Integer>();
    ArrayList<String> resname = new ArrayList<String>();

    ArrayList<String> res = new ArrayList<String>();
    String tableorder="tableorder";
    String order="foodorder";
    String restaurantid="restid";
    ArrayList<String> rescost = new ArrayList<String>();
    private LayoutInflater layoutInflater;
    String restid;
    public ImageAdapter(Context context,String a){
        restid=a;
        InputStream insr=context.getResources().openRawResource(R.raw.restaurantloc);
        BufferedReader br =null;
        try {
            br=new BufferedReader(new InputStreamReader(insr));
        }catch (Exception e1){
            e1.printStackTrace();
        }
        String line =null;

        try {
            for(;(line=br.readLine())!=null;){
                String word[];
                word=line.split("\\s+");
                res.add(word[0]);
            }} catch (IOException e) {
            e.printStackTrace();
        }
        InputStream ins=context.getResources().openRawResource(context.getResources().getIdentifier("a"+a, "raw", "ntust.csie.app.orderingsystem"));

        try {
            br=new BufferedReader(new InputStreamReader(ins));
        }catch (Exception e1){
            e1.printStackTrace();
        }
        this.context=context;
        int i=1;
        while(context.getResources().getIdentifier("a"+a+String.valueOf(i), "drawable", "ntust.csie.app.orderingsystem")!=0){
            resID.add(context.getResources().getIdentifier("a"+a+String.valueOf(i), "drawable", "ntust.csie.app.orderingsystem"));
            i++;
        }
        try {
            for(;(line=br.readLine())!=null;){
                String word[];
                word=line.split("\\s+");
                resname.add(word[0]);
                rescost.add(word[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getCount() {
        return resID.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item_view=layoutInflater.inflate(R.layout.pic,container,false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.image_view);
        TextView textcost =(TextView)item_view.findViewById(R.id.textcost);
        TextView textfood =(TextView)item_view.findViewById(R.id.textfood);
        textcost.setText(rescost.get(position)+"元");
        textfood.setText(resname.get(position));
        Bitmap bmp;
        int width=1400;
        int height=1000;
        bmp= BitmapFactory.decodeResource(context.getResources(),resID.get(position));//image is your image
        bmp=Bitmap.createScaledBitmap(bmp, width,height, true);
        imageView.setImageBitmap(bmp);
        container.addView(item_view);
        final Button btnorder=(Button)item_view.findViewById(R.id.btnorder);

        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statement="insert  into "+tableorder+" ("+restaurantid+", "+order+") values('"+res.get(Integer.parseInt(restid))+"','"+resname.get(position)+"')";
                MainActivity.nameDB.execSQL(statement);
                Toast.makeText(context, "新增訂單", Toast.LENGTH_SHORT).show();


            }
        });

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }
}

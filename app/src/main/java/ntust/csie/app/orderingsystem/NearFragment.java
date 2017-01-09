package ntust.csie.app.orderingsystem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yafun on 2017/1/7.
 */

public class NearFragment extends Fragment {
    public final static String POS="POS";
    ListView lvdata;
    TextView textview;
    String dbname="nameDB.db";
    String tablename="namelist";
    String tableindist="tableindist";
    String tableorder="tableorder";
    String order="foodorder";
    String restaurantid="restid";
    int n=1;
    String ID="num",restaurant="restaurant",lat="lat",lon="lon";
    String CREATE_TABLE="create table if not exists "+tablename+"(_id integer primary key, "+ID+" integer, "+restaurant+" text, "+lat+" double, "+lon+" double)";
    String CREATE_TABLE_INDIST="create table if not exists "+tableindist+"(_id integer primary key, "+restaurant+" text, "+lat+" double, "+lon+" double)";
    String CREATE_TABLE_ORDER="create table if not exists "+tableorder+"(_id integer primary key, "+restaurantid+" text, "+order+" text)";
    String DROP_TABLE="DROP TABLE IF EXISTS "+tablename;
    String DROP_TABLE_INDIST="DROP TABLE IF EXISTS "+tableindist;
    SimpleCursorAdapter adapter;
    LocationManager locationManager;
    LocationListener locationListener;
    private Cursor cursor3;
    private Cursor cursor1;
    private Cursor cursor;
    private SwipeRefreshLayout mSwipeLayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        lvdata = (ListView) view.findViewById(R.id.listView);
        textview = (TextView) view.findViewById(R.id.textView);

        this.registerForContextMenu(lvdata);

        locationManager=(LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateAdapter(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        initWordDB();

        Log.d(" ", CREATE_TABLE);
        lvdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(),menu.class);
                Bundle bundle = new Bundle();
                bundle.putString("POS", String.valueOf(position + 1));
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }



    void initWordDB(){
        InputStream ins=this.getResources().openRawResource(R.raw.restaurantloc);
        BufferedReader br =null;
        try {
            br=new BufferedReader(new InputStreamReader(ins));
        }catch (Exception e1){
            e1.printStackTrace();
        }
        String line =null;

        cursor=null;
        try {
            for(;(line=br.readLine())!=null;n++){
                String word[];
                word=line.split("\\s+");
                String statement="insert  into "+tablename+" ("+ID+", "+restaurant+", "+lat+", "+lon+") values("+n+",'"+word[0]+"', '"+Double.parseDouble(word[1]) +"', '"+Double.parseDouble(word[2])+"')";
                MainActivity.nameDB.execSQL(statement);
            }

            cursor=MainActivity.nameDB.rawQuery("select * from "+tablename,null);
            //Toast.makeText(getContext(),"Totally "+cursor.getCount()+" entries in database",Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //cursor.close();

    }
    void updateAdapter(Location location){
        cursor3 = null;
        cursor1 = null;


        MainActivity.nameDB.execSQL(CREATE_TABLE_INDIST);
        try {
            cursor3 =  MainActivity.nameDB.rawQuery("select * from " + tablename, null);
            if (cursor3.moveToFirst()) {
                do {
                    //assing values
                    String column1 = cursor3.getString(2);
                    String column2 = cursor3.getString(3);
                    String column3 = cursor3.getString(4);

                    if (disthavesine(location.getLatitude(), location.getLongitude(), Double.parseDouble(column2), Double.parseDouble(column3)) < 3) {
                        String statement = "insert  into " + tableindist + " (" + restaurant + ", " + lat + ", " + lon + ") values('" + column1 + "', '" + Double.parseDouble(column2) + "', '" + Double.parseDouble(column3) + "')";
                        MainActivity.nameDB.execSQL(statement);
                    }
                    //Do something Here with values

                } while (cursor3.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //cursor3.close();
        try {
            cursor1 = MainActivity.nameDB.rawQuery("select * from "+tableindist,null);
            if(cursor1!=null&&cursor1.getCount()>=0){
                textview.setVisibility(View.INVISIBLE);
                lvdata.setVisibility(View.VISIBLE);
                if (getActivity() != null) {
                    adapter=new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_2,cursor1,new String[]{restaurant},new int[]{android.R.id.text2},0);
                }

                lvdata.setAdapter(adapter);

            }else{
                lvdata.setVisibility(View.INVISIBLE);
                textview.setVisibility(View.VISIBLE);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        MainActivity.nameDB.execSQL(DROP_TABLE_INDIST);
        //cursor1.close();
    }


    double disthavesine(double p10,double p11, double p20,double p21){
        double radius=(6356.752+6378.137)/2;
        if(p10>-23.5&&p10<23.5&&p20>-23.5&&p20<23.5)
            radius=6378.137;
        else if((p10<-66.5&&p20<-66.5)||(p10>66.5&&p20>66.5))
            radius=6356.752;
        double distlat=rad2deg(p20-p10);
        double distlon=rad2deg(p21-p11);
        double a=Math.sin(distlat/2)*Math.sin(distlat/2)+Math.cos(rad2deg(p10))*Math.cos(rad2deg(p20))*Math.sin(distlon/2)*Math.sin(distlon/2);
        double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double dist=radius*c;
        return dist;
    }
    double rad2deg(double ran){
        return ran*Math.PI/180;
    }


}

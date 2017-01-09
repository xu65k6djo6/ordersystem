package ntust.csie.app.orderingsystem;


import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class OrderFragment extends Fragment

{
    String order="foodorder";
    String restaurantid="restid";
    ListView lvorder;
    TextView textView;
    Button btnDone;
    LinearLayout orderview,finishview;
    Handler BHandlerTime = new Handler();
    int n=0;
    SimpleCursorAdapter adapter;
    final  int MENU_DELETE= Menu.FIRST;
    public static int changed;
    private String resname;
    private String foodname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderlayout, null);
        textView=(TextView) view.findViewById(R.id.tvorder);
        lvorder=(ListView)view.findViewById(R.id.lvorder);
        //btnDone = (Button) view.findViewById(R.id.btnDone);

        registerForContextMenu(lvorder);
        BHandlerTime.postDelayed(timerRun, 1);

        return view;
    }
    void updateAdapter(){
        Cursor cursor2 =MainActivity.nameDB.rawQuery("select * from tableorder",null);
        if(cursor2!=null&&cursor2.getCount()>=0){

            adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, cursor2, new String[]{restaurantid, order}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            lvorder.setAdapter(adapter);
            textView.setVisibility(View.INVISIBLE);
            lvorder.setVisibility(View.VISIBLE);

        }else{
            lvorder.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }
    Runnable timerRun = new Runnable()
    {
        public void run()
        {
            Cursor cursor2 =MainActivity.nameDB.rawQuery("select * from tableorder",null);
            if(cursor2!=null&&cursor2.getCount()>=0){

                adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, cursor2, new String[]{restaurantid, order}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
                lvorder.setAdapter(adapter);
                textView.setVisibility(View.INVISIBLE);
                lvorder.setVisibility(View.VISIBLE);

            }else{
                lvorder.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }

            BHandlerTime.postDelayed(this, 1000);
            // 若要取消可以寫一個判斷在這決定是否啟動下一次即可
        }
    };
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d("22","22");
        if(v==lvorder){
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int lvpos=menuInfo.position;
        Cursor dataCursor=adapter.getCursor();
        dataCursor.moveToPosition(lvpos);
        resname=dataCursor.getString(1);
        foodname=dataCursor.getString(2);

        Log.d(resname,foodname);
        Log.d(String.valueOf(item.getItemId()),"fggbf");

        switch (item.getItemId()){
            case R.id.add:
                String statement="delete from tableorder"+" where _id = (SELECT MIN(_id) FROM tableorder WHERE restid = '"+resname+"' and foodorder = '"+foodname+"')";
                MainActivity.nameDB.execSQL(statement);
                //updateAdapter();
                break;

        }



        return true;
    }
    public String tableToString(SQLiteDatabase db, String tableName) {
        Log.d("","tableToString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        tableString += cursorToString(allRows);
        return tableString;
    }
    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }
    public void onDestroy() {


        //nameDB.close();
        BHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }
}

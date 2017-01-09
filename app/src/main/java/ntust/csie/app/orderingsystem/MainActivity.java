package ntust.csie.app.orderingsystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dlDrawer;
    private ListView lvDrawerItems;
    private Toolbar toolbar;
    private FragmentDrawerAdapter mDrawerAdapter;
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
    public static SQLiteDatabase nameDB=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        toolbar.setTitleTextAppearance(this,(int) getResources().getDimension(R.dimen.action_menu_item_text_size));
        dlDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
        mDrawerAdapter = new FragmentDrawerAdapter(this, dlDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerAdapter.syncState();
        dlDrawer.setDrawerListener(mDrawerAdapter);

        lvDrawerItems = (ListView) findViewById(R.id.lvDrawerItems);
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,mDrawerAdapter.getPageTitles());
        lvDrawerItems.setAdapter(adapter);

        lvDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerAdapter.setItemSelected(position);
                toolbar.setTitle(mDrawerAdapter.getPageTitle(position));
                getSupportFragmentManager().beginTransaction().replace(R.id.flFrag, mDrawerAdapter.getFragmentAtPos(position)).commit();
                dlDrawer.closeDrawer(lvDrawerItems);
            }
        });
        toolbar.setTitle(mDrawerAdapter.getPageTitle(0));
        getSupportFragmentManager().beginTransaction().replace(R.id.flFrag, mDrawerAdapter.getFragmentAtPos(0)).commit();
        nameDB = openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
        nameDB.execSQL(DROP_TABLE);
        nameDB.execSQL(CREATE_TABLE);
        nameDB.execSQL(CREATE_TABLE_ORDER);
        /*@Override
        public boolean onCreateOptionsMenu(Menu menu_del) {
            getMenuInflater().inflate(R.menu_del.main, menu_del);
            return super.onCreateOptionsMenu(menu_del);
        }*/

    }
    @Override
    public void onDestroy() {

        nameDB.execSQL("drop table if exists "+tablename);
        nameDB.execSQL("drop table if exists "+tableindist);
        super.onDestroy();
    }
}

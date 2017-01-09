package ntust.csie.app.orderingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OrderCheck extends AppCompatActivity {
    //ListView lvorder;
    ImageView imgodMeal;
    TextView txtodShop,txtodName,txtodPrice,txtTimer;
    Button btnDelivery, btnCancel;
    LinearLayout checklayout,finishlayout;

    int time=4;
    int[] imgId = {R.drawable.a11, R.drawable.a22, R.drawable.a32, R.drawable.a41, R.drawable.a52,
                    R.drawable.a61, R.drawable.a71, R.drawable.a91};
    String[] shopName={"奈手卷","韓風小舖","四海遊龍","台灣古早味",
                        "福客鐵板料理","丼太郎","豪享來","雞同ㄚ講燒臘"};
    String[] mealName={"肉鬆飯捲","豆腐火鍋","煎餃","雞排便當","牛排","親子丼","牛肉麵","燒臘便當"};
    String[] price={"50","88","50","65","100","60","70","65"};
    String strshop,strmeal,strprice;
    int iimg;

    //MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check);

        imgodMeal = (ImageView) findViewById(R.id.imgodMeal);
        txtodShop = (TextView) findViewById(R.id.txtodShop);
        txtodName = (TextView) findViewById(R.id.txtodName);
        txtodPrice = (TextView) findViewById(R.id.txtodPrice);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDelivery = (Button) findViewById(R.id.btndelivery);
        checklayout = (LinearLayout) findViewById(R.id.ordercheck);
        finishlayout = (LinearLayout) findViewById(R.id.finish);

        Bundle bundle = getIntent().getExtras();
        final int id = bundle.getInt("id");
        imgodMeal.setImageResource(imgId[id]);
        txtodShop.setText(shopName[id]);
        txtodName.setText(mealName[id]);
        txtodPrice.setText(" $"+price[id]);

        btnCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelivery.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                checklayout.setVisibility(View.GONE);
                finishlayout.setVisibility(View.VISIBLE);
                String statement="insert  into tableorder"+" (restid, foodorder) values('"+shopName[id]+"','"+mealName[id]+"')";
                MainActivity.nameDB.execSQL(statement);
                new CountDownTimer(3000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txtTimer.setText(Long.toString(millisUntilFinished/1000)+"秒後返回首頁");
                    }
                    @Override
                    public void onFinish() {
                        finish();
                    }
                }.start();
            }
        });

        //adapter = new MyAdapter(this);
        //lvorder.setAdapter(adapter);

    }

    /*class MyAdapter extends BaseAdapter {

        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        Bundle bundle = new Bundle();
        final recommendFragment fragment = new recommendFragment();

        @Override
        public int getCount() {return 1;}

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return fragment.getItemId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.order, null);
            ImageView imgMeal = (ImageView) convertView.findViewById(R.id.imgMeal);
            TextView txtodShop = (TextView) convertView.findViewById(R.id.txtodShop);
            TextView txtodName = (TextView) convertView.findViewById(R.id.txtodName);
            TextView txtodPrice = (TextView) convertView.findViewById(R.id.txtodPrice);
            imgMeal.setImageResource(imgId[fragment.getItemId()]);
            txtodShop.setText(fragment.getShop());
            txtodName.setText(fragment.getName());
            txtodPrice.setText(fragment.getPrice());
            return convertView;
        }
    }*/
}



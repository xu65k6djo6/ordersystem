package ntust.csie.app.orderingsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class recommendFragment extends Fragment {
    ImageView mealPhoto;
    Button btnChange, btnOrder;
    TextView txtShop, txtName, txtPrice;



    int[] imgId = {R.drawable.a11, R.drawable.a22, R.drawable.a32, R.drawable.a41, R.drawable.a52,
            R.drawable.a61, R.drawable.a71, R.drawable.a91};
    String[] shopName={"奈手卷","韓風小舖","四海遊龍","台灣古早味",
            "福客鐵板料理","丼太郎","豪享來","雞同ㄚ講燒臘"};
    String[] mealName={"肉鬆飯捲","豆腐火鍋","煎餃","雞排便當","牛排","親子丼","牛肉麵","燒臘便當"};
    String[] price={"50","49","50","65","100","60","70","65"};

    int itemId=0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnChange = (Button) getView().findViewById(R.id.btnChange);
        btnOrder = (Button) getView().findViewById(R.id.btnOrder);
        txtShop = (TextView) getView().findViewById(R.id.txtShop);
        txtName = (TextView) getView().findViewById(R.id.txtName);
        txtPrice = (TextView) getView().findViewById(R.id.txtPrice);
        mealPhoto = (ImageView) getView().findViewById(R.id.mealPhoto);

        itemId=(int)(Math.random()*imgId.length);
        mealPhoto.setImageResource(imgId[itemId]);
        txtShop.setText(shopName[itemId]);
        txtName.setText(mealName[itemId]);
        txtPrice.setText(" $"+price[itemId]);

        btnChange.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                itemId=(int)(Math.random()*imgId.length);
                mealPhoto.setImageResource(imgId[itemId]);
                txtShop.setText(shopName[itemId]);
                txtName.setText(mealName[itemId]);
                txtPrice.setText(" $"+price[itemId]);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Context context = getActivity().getApplicationContext();
                Intent intent = new Intent();
                intent.setClass(getActivity(),OrderCheck.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",itemId);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommendation, container, false);
    }

    //public int getImg(){return this.imgId[itemId];}
    //public String getShop(){return this.shopName[itemId];}
    //public String getName(){return this.mealName[itemId];}
    //public String getPrice(){return this.price[itemId];}



}

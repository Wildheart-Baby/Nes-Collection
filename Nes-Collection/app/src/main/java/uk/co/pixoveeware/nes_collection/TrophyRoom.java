package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.models.GameItems;

public class TrophyRoom extends AppCompatActivity {

    Context context;
    ArrayList<GameItems> nesList;
    GameItems nesListItems;
    String gameimage, sql;
    int coverid, cgames;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //this.context = context;

        readGame();
    }


    public void readGame(){//the readlist function
        ImageView cover1 = (ImageView) findViewById(R.id.imgGold);
        ImageView cover2 = (ImageView) findViewById(R.id.imgSilver);
        ImageView cover3 = (ImageView) findViewById(R.id.imgBronze);


        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        //String sql = "SELECT image FROM eu where owned = 1 and cart = 1 and box = 1 and manual = 1 and price = (select max(price) from eu) limit 3 ";
        sql = "SELECT image FROM eu where owned = 1 and cart = 1 and box = 1 and manual = 1";
        c = db.rawQuery(sql, null);
        c.moveToFirst();
        cgames = c.getCount();
        //sql = "select name from eu where pal_b_owned = 1 and pal_b_cost >0 and pal_b_cost=(select max(pal_b_cost) from eu) limit 1";
        Log.d("pixo", sql);

        if(cgames > 0 ){
            c = db.rawQuery(sql, null);
            c.moveToFirst();//move to the first record
            gameimage = (c.getString(c.getColumnIndex("image")));
            coverid = getResources().getIdentifier(gameimage, "drawable", getPackageName());
            Log.d("pixo", "game1: " + gameimage + " coverid: " + coverid);
            cover1.setVisibility(View.VISIBLE);
            cover1.setImageResource(coverid);
            }

        if (cgames > 1) {//move to the first record
            c.moveToNext();
            gameimage = (c.getString(c.getColumnIndex("image")));
            coverid = getResources().getIdentifier(gameimage, "drawable", getPackageName());
            Log.d("pixo", "game2: " + gameimage + " coverid: " + coverid);
            cover2.setVisibility(View.VISIBLE);
            cover2.setImageResource(coverid);

        }
        if (cgames > 2){
                c.moveToNext();//move to the next record
                gameimage = (c.getString(c.getColumnIndex("image")));
                coverid = getResources().getIdentifier(gameimage, "drawable", getPackageName());
                Log.d("pixo", "game3: " + gameimage + " coverid: " + coverid);
                cover3.setVisibility(View.VISIBLE);
                cover3.setImageResource(coverid);
            }
            c.close();//close the cursor
        db.close();//close the database

    }

}

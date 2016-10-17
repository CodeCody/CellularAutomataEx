package com.example.codyhammond.cellularautomataex;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import static android.R.attr.factor;
import static android.R.attr.id;

public class MainActivity extends AppCompatActivity  {

    private AutomatonView automatonView;
    private PaintThread paintThread;
    private DrawerLayout drawerLayout;
    private ListView categoryListView;
    private Grid cellGrid;
    private Toolbar toolbar;
    private TextView categoryTitle;
    private ImageButton menu_drawer;
    private ImageButton optionsButton;
    private int currentSelection=0;
    private final String[] categories={"1D Cellular Automata","Game Of Life","Fractals"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_drawer=(ImageButton)findViewById(R.id.menu_drawer);
        optionsButton=(ImageButton)findViewById(R.id.options);
        categoryTitle=(TextView)findViewById(R.id.category_title);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        categoryListView=(ListView)findViewById(R.id.category_list);
        categoryListView.setAdapter(new CategoryAdapter(this,R.layout.category_list_item,categories));
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     automatonView.setNewGrid(i);
                drawerLayout.closeDrawer(GravityCompat.START,true);
                categoryTitle.setText(categories[i]);
            }
        });

        categoryTitle.setText(categories[0]);

        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START, true);
                }
                else
                {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        automatonView=(AutomatonView)findViewById(R.id.automataview);

        Display display=getWindowManager().getDefaultDisplay();
        automatonView.init(display);
       // automatonView.setVisibility(View.VISIBLE);

    }


    class CategoryAdapter extends ArrayAdapter<String>
    {
        public CategoryAdapter(Context context,int id,String [] list)
        {
            super(context,id,list);
        }

        @Override @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent)
        {
            if(convertView==null)
            {
                convertView=getLayoutInflater().inflate(R.layout.category_list_item,null);
            }

            TextView textView=(TextView)convertView.findViewById(R.id.category_item_name);
            textView.setText(categories[position]);

            return convertView;
        }

        @Override
        public int getCount()
        {
            return categories.length;
        }
    }
}

package com.example.codyhammond.cellularautomataex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.codyhammond.cellularautomataex.Grid.Grid;
import com.example.codyhammond.cellularautomataex.Grid.OneDCellularAutomataGrid;
import com.example.codyhammond.cellularautomataex.RuleSets.Ruleset;

import static com.example.codyhammond.cellularautomataex.Options.fractalAndChaosOptions;
import static com.example.codyhammond.cellularautomataex.Options.ruleSetNames;

public class MainActivity extends AppCompatActivity  {

    private AutomatonView automatonView;
   // private PaintThread paintThread;
    private DrawerLayout drawerLayout;
    private ListView categoryListView,optionsListView;
    private Grid cellGrid;
    private Toolbar toolbar;
    private TextView categoryTitle,subCategoryTitle;
    private ImageButton menu_drawer;
    private ImageButton optionsButton;
    private int currentSelection=0;
    private final String[] categories={"1D Cellular Automata","Game Of Life","Fractals And Chaos"};

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
        subCategoryTitle=(TextView)findViewById(R.id.sub_category_title);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        categoryListView=(ListView)findViewById(R.id.category_list);
        optionsListView=(ListView)findViewById(R.id.optionsList);

        optionsListView.setAdapter(new OptionsAdapter(this,R.layout.options_list_item, ruleSetNames));

        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                optionsListView.setVisibility(View.GONE);
                if(categoryTitle.getText().equals(categories[0])) {
                    automatonView.setNewRule(ruleSetNames[i]);
                    subCategoryTitle.setText(ruleSetNames[i]);
                }
                else if(categoryTitle.getText().equals(categories[2]))
                {
                    automatonView.setNewFractalChaos(i);
                    subCategoryTitle.setText(fractalAndChaosOptions[i]);
                }
            }
        });

        optionsListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.i("FocusChange",String.valueOf(b));
            }
        });

       // optionsListView.

        categoryListView.setAdapter(new CategoryAdapter(this,R.layout.category_list_item,categories));
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     automatonView.setNewSurfaceMode(i);
                if( i == 0) {
                    optionsListView.setAdapter(new OptionsAdapter(getApplicationContext(), R.layout.options_list_item, Options.ruleSetNames));
                    subCategoryTitle.setText(ruleSetNames[0]);
                }
                else if( i == 1) {
                    optionsListView.setAdapter(new OptionsAdapter(getApplicationContext(), R.layout.options_list_item, Options.gameOfLifeOptions));
                    subCategoryTitle.setVisibility(View.GONE);
                }
                else {
                    optionsListView.setAdapter(new OptionsAdapter(getApplicationContext(), R.layout.options_list_item, Options.fractalAndChaosOptions));
                    subCategoryTitle.setText(fractalAndChaosOptions[0]);
                }
                drawerLayout.closeDrawer(GravityCompat.START,true);
                categoryTitle.setText(categories[i]);
            }
        });

        categoryTitle.setText(categories[0]);
        subCategoryTitle.setText(ruleSetNames[4]);

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

               optionsListView.setVisibility(View.VISIBLE);
            }
        });

        automatonView=(AutomatonView)findViewById(R.id.automataview);


        automatonView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               optionsListView.setVisibility(View.GONE);
                return false;
            }
        });


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

    class OptionsAdapter extends ArrayAdapter<String>
    {
        private String[]list;
        public OptionsAdapter(Context context,int id,String[] list)
        {
            super(context,id,list);
            this.list=list;
        }


        @Override @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent)
        {
            if(convertView==null)
            {
                convertView=getLayoutInflater().inflate(R.layout.options_list_item,null);
            }

            TextView textView=(TextView)convertView.findViewById(R.id.option_name);
            textView.setText(list[position]);

           // RadioButton radioButton=(RadioButton)convertView.findViewById(R.id.selectOption);

            return convertView;
        }

        @Override
        public int getCount()
        {
            return list.length;
        }
    }
}

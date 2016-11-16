package edu.training.droidbountyhunter;

import android.content.Intent;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static DBProvider oDB;
    public static String UDID;

    public Home(){
        oDB = new DBProvider(this);

    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent oW = new Intent();
                oW.setClass(getApplicationContext(), AgregarActivity.class);
                startActivityForResult(oW, 0);
            }
        });*/

        UDID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        int iNCf = oDB.ContarFugitivos();
        if(iNCf <= 0){
            NetServices oNS = new NetServices(new IOnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object feed) {
                    UpdateLists(0);
                }

                @Override
                public void onTaskError(Object feed) {
                    Toast.makeText(getApplicationContext(), "Ocurrio un problema con el WebService!!", Toast.LENGTH_LONG).show();
                }
            });
            oNS.execute("Fugitivos", "");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.menu_agregar:
                Intent oW = new Intent();
                oW.setClass(this, AgregarActivity.class);
                startActivityForResult(oW, 0);
                break;
        }

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }*/

    public void UpdateLists(int index){
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UpdateLists(requestCode);
    }

    public static class ListFragment extends Fragment {
        public static final String ARG_SECTION_TEXT = "section_text";
        public static final String ARG_SECTION_NUMBER = "section_number";
        View iView;
        int iMode;

        public ListFragment(){

        }

        public void UpdateList(){
            String[][] aRef = oDB.ObtenerFugitivos(iMode == 1);
            if(aRef != null){
                //String[] aData = new String[aRef.length];
                ArrayList<String[]> lst = new ArrayList<String[]>();

                for(int i = 0; i < aRef.length; i++){
                    //aData[i] = aRef[i][1];
                    lst.add(aRef[i]);
                }

                ListView tlList = (ListView) iView.findViewById(R.id.bhlist);
                //ArrayAdapter<String> aList = new ArrayAdapter<String>(getActivity(), R.layout.row_list, aData);
                FugitivosAdapter aList = new FugitivosAdapter(getActivity(), lst);

                tlList.setTag(aRef);
                tlList.setAdapter(aList);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
            Bundle aArgs = this.getArguments();

            iView = inflater.inflate(R.layout.list_fragment, containter, false);
            iMode = aArgs.getInt(ListFragment.ARG_SECTION_NUMBER);

            ListView tlList = ((ListView) iView.findViewById(R.id.bhlist));

            UpdateList();

            /*
            String[] aData = new String[6];
            aData[0] = "Armando Olmos";
            aData[1] = "Guillermo Ortega";
            aData[2] = "Carlos Martinez";
            aData[3] = "Moises Rivas";
            aData[4] = "Adrian Ribiera";
            aData[5] = "Victor Medina";

            ArrayAdapter<String> aList = new ArrayAdapter<String>(getActivity(), R.layout.row_list, aData);
            tlList.setAdapter(aList);
            */


            tlList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> aList, View vItem, int iPosition, long l){
                    Intent oW = new Intent( );
                    String [][] aDat = (String[][])aList.getTag();

                    oW.setClass(getActivity(), DetalleActivity.class);
                    oW.putExtra("title", aDat[iPosition][2]);
                    oW.putExtra("mode", iMode);
                    oW.putExtra("id", aDat[iPosition][0]);
                    oW.putExtra("foto", aDat[iPosition][3]);
                    oW.putExtra("lat", aDat[iPosition][4]);
                    oW.putExtra("lon", aDat[iPosition][5]);
                    oW.putExtra("nom", aDat[iPosition][1]);
                    oW.putExtra("fecha", aDat[iPosition][6]);

                    getActivity().startActivityForResult(oW, iMode);
                }
            });

            return  iView;
        }
    }

    public static class AboutFragment extends Fragment {
        public AboutFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View iView = inflater.inflate(R.layout.about_fragment, container, false);

            RatingBar rbApp = ((RatingBar) iView.findViewById(R.id.ratingApp));

            String sRating = "0.0";

            try {
                if(System.getProperty("rating") != null){
                    sRating = System.getProperty("rating");
                }
            }
            catch (Exception ex){

            }

            if(sRating.isEmpty()){
                sRating = "0.0";
            }

            rbApp.setRating(Float.parseFloat(sRating));
            rbApp.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
              @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser){
                  System.setProperty("rating", String.valueOf(rating));
                  ratingBar.setRating(rating);
              }
            });

            return iView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            mFragments = new Fragment[3];
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            if(mFragments[position] == null){
                if(position < 2){
                    mFragments[position] = new ListFragment();
                    Bundle args = new Bundle();

                    args.putInt(ListFragment.ARG_SECTION_NUMBER, position);
                    mFragments[position].setArguments(args);
                }
                else {
                    mFragments[position] = new AboutFragment();
                }
            }

            return mFragments[position];

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_fugitivos).toUpperCase(l);
                case 1:
                    return getString(R.string.title_capturados).toUpperCase(l);
                case 2:
                    return getString(R.string.title_acercade).toUpperCase(l);
            }
            return null;
        }
    }
}

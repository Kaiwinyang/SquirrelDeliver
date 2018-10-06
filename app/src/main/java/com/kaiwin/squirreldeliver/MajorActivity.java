package com.kaiwin.squirreldeliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MajorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CreateDeliverTaskFragment createDeliverTaskFragment;
    NavigationView navigationView;
    View navigationViewHeader;

    private ImageView userIconImageView;
    private TextView usernameTextViewAtHeader, emailTextViewAtHeader;

    //user info relatives
    private String username, phone, email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init_mAuthAndGetUserInfo();

        createDeliverTaskFragment = CreateDeliverTaskFragment.newInstance("", "");

        /*控制侧滑菜单*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override//當DrawerLayout開啟時，強制隱藏輸入法
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Tool.hideInput(getApplicationContext(), drawer);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*设定NavigationView菜单的选择事件*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //加號按鈕
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.major_fragment, createDeliverTaskFragment);
                transaction.commit();

                MenuItem menuItemChecked = navigationView.getCheckedItem();
                if (menuItemChecked != null) menuItemChecked.setChecked(false);
            }
        });


        navigationViewHeader = navigationView.getHeaderView(0);
        userIconImageView = navigationViewHeader.findViewById(R.id.userIconImageView);
        usernameTextViewAtHeader = navigationViewHeader.findViewById(R.id.usernameTextViewAtHeader);
        emailTextViewAtHeader = navigationViewHeader.findViewById(R.id.emailTextViewAtHeader);
        initHeader();
    }


    @Override
    protected void onStart(){
        super.onStart();

    }

    /*后退键*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.major, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_icon) {

        } else if (id == R.id.nav_history_orders) {

        } else if (id == R.id.nav_personal_info) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        /*关闭侧滑菜单*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init_mAuthAndGetUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null)
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        FirebaseUser user = mAuth.getCurrentUser();
        username = user.getDisplayName();
        phone = user.getPhoneNumber();
        email = user.getEmail();
    }

    private void initHeader() {
        // userIconImageView = findViewById(R.id.userIconImageView);
        usernameTextViewAtHeader.setText(username);
        emailTextViewAtHeader.setText(email);

        Log.v("Banana","initHeader");
    }
}

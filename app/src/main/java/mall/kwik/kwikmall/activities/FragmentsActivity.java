package mall.kwik.kwikmall.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import mall.kwik.kwikmall.BaseFragActivity.BaseActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.fragments.AccountFragment;
import mall.kwik.kwikmall.fragments.ExploreFragment;
import mall.kwik.kwikmall.fragments.FragmentWithoutSearchResults;
import mall.kwik.kwikmall.fragments.NearMeFragment;
import mall.kwik.kwikmall.fragments.RestaurantsFragment;
import mall.kwik.kwikmall.fragments.RestaurantsProductsFragment;
import mall.kwik.kwikmall.fragments.ShopsFragments;
import mall.kwik.kwikmall.fragments.ViewCartFragment;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;


public class FragmentsActivity extends BaseActivity {

    private DBHelper database;
    private BottomBar bottomBar;
    public static BottomBarTab nearby;

    private Context context;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        ButterKnife.bind(this);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        findViewId();

        database = new DBHelper(this);

        int counter = database.getProductsCount();

        nearby = bottomBar.getTabWithId(R.id.tab_cart);

        if (database.getProductsCount() != 0) {

            nearby.setBadgeCount(counter);

        }


        if (savedInstanceState == null) {

            Fragment fragment = null;
            fragment = new NearMeFragment();

            if (fragment != null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment, "nearMeFragment").commit();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);


                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }

            }

        }


    }

    void myBackPressed(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }



    public void replace(){

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new RestaurantsProductsFragment(),"RestaurantsProductsFragment")
                .addToBackStack("RestaurantsProductsFragment")
                .commit();



    }


    public void replace2(){

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new RestaurantsProductsFragment(),"RestaurantsProductsFragment")
                .addToBackStack("RestaurantsProductsFragment")
                .commit();


    }



        @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void findViewId() {


        bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {

                switch (tabId) {

                    case R.id.tab_nearyby:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new NearMeFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, fragment, "nearMefragment")
                                .commit();


                        break;
                    case R.id.tab_search:

                        Fragment exploreFragment = new ExploreFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFrame, exploreFragment, "exploreFragment");
                        fragmentTransaction.addToBackStack("exploreFragment");
                        fragmentTransaction.commit();


                        overridePendingTransition(R.anim.enter, R.anim.exit);

                        break;

                    case R.id.tab_cart:


                        Fragment viewCartFragment = new ViewCartFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.mainFrame, viewCartFragment, "viewCartFragment");
                        fragmentTransaction2.addToBackStack("viewCartFragment");
                        fragmentTransaction2.commit();

                        overridePendingTransition(R.anim.enter, R.anim.exit);


                        break;
                    case R.id.tab_account:

                        Fragment accountFrag = new AccountFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.mainFrame, accountFrag, "accountFrag");
                        fragmentTransaction3.addToBackStack("accountFrag");
                        fragmentTransaction3.commit();

                        break;

                }


            }
        });

    }


    @Override
    public void onBackPressed() {


        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();

        } else if (getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        } else {
            getSupportFragmentManager().popBackStack();
        }


    }
}

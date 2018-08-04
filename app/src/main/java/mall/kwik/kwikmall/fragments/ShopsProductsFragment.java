package mall.kwik.kwikmall.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mall.kwik.kwikmall.R;


public class ShopsProductsFragment extends Fragment {


    private View view;

    Bundle bundle;
    private String itemname, nameOfHotel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shops_details, container, false);

        System.out.println("RestaurantsProductsFragment.onCreateView - - - oncreateViewtest ");

        bundle = getArguments();

        if (bundle != null) {
            itemname = bundle.getString("itemname");
            nameOfHotel = bundle.getString("nameOfHotel");
            System.out.println("RestaurantsProductsFragment.onCreateView - - - Test1 " + itemname);
            System.out.println("RestaurantsProductsFragment.onCreateView - - - Test2 " + nameOfHotel);

        }


        if (itemname == null) {

            System.out.println("RestaurantsProductsFragment.onCreateView Shahzeb1");
            Bundle args = new Bundle();
            args.putString("itemname", itemname);
            FragmentSearchResults fragmentSearch = new FragmentSearchResults();
            fragmentSearch.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.mainFrame, fragmentSearch, "fragmentSearch").commit();

        }
        return view;

    }


}

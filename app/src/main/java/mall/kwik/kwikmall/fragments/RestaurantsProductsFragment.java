package mall.kwik.kwikmall.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.activities.FragmentsActivity;

/**
 * Created by dharamveer on 1/12/17.
 */

public class RestaurantsProductsFragment extends Fragment{


    private View view;

    Bundle bundle;
    private String itemname,nameOfHotel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurants_details, container, false);



        bundle = getArguments();

        if(bundle != null)
        {

            itemname = bundle.getString("itemname");
            nameOfHotel = bundle.getString("nameOfHotel");

        }


        if(itemname!=null){

            Bundle args = new Bundle();
            args.putString("itemname",itemname);
            FragmentSearchResults fragmentSearch = new FragmentSearchResults();
            fragmentSearch.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.mainFrame, fragmentSearch,"fragmentSearch").commit();

        }
        else {

            FragmentWithoutSearchResults fragmentWithoutSearch = new FragmentWithoutSearchResults();
            getFragmentManager().beginTransaction().replace(R.id.mainFrame, fragmentWithoutSearch,"fragmentWithoutSearch").commit();


        }


        return view;

    }


}

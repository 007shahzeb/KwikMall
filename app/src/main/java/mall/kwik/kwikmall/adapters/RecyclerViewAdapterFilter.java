package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import mall.kwik.kwikmall.apiresponse.FilterListResponse.FilterListPayload;
import mall.kwik.kwikmall.R;

import static mall.kwik.kwikmall.activities.FilterActivity.footerFilter;
import static mall.kwik.kwikmall.activities.FilterActivity.tvClear;

/**
 * Created by dharamveer on 30/12/17.
 */

public class RecyclerViewAdapterFilter extends RecyclerView.Adapter<RecyclerViewAdapterFilter.MyHolderRight>
{

    List<FilterListPayload> filterListPayloadList = Collections.emptyList();
    Context context;
    View view;
    public  boolean isUnSelectedAll;
    private RecyclerViewAdapterFilter.ItemClickListenerCheck itemClickListenerCheck;
    private HashMap<Integer, Boolean> ischecked = new HashMap<>();

    public void unselectAll(){
        Log.e("onClickUnSelectAll","no");
        isUnSelectedAll = false;
        notifyDataSetChanged();
    }


    public interface ItemClickListenerCheck{

        void itemClick(int catId);
    }


    public void setItemClickListenerCheck(RecyclerViewAdapterFilter.ItemClickListenerCheck itemClickListenerCheck){

        this.itemClickListenerCheck = itemClickListenerCheck;

    }

    public RecyclerViewAdapterFilter(List<FilterListPayload> filterListPayloads, Context context) {
        this.filterListPayloadList = filterListPayloads;
        this.context = context;

    }

    @Override
    public RecyclerViewAdapterFilter.MyHolderRight onCreateViewHolder(ViewGroup parent, int viewType) {


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_fragment, parent, false);

        return new MyHolderRight(view);


    }



    @Override
    public void onBindViewHolder(final RecyclerViewAdapterFilter.MyHolderRight holder, final int position) {


        final FilterListPayload filterListPayload = filterListPayloadList.get(position);


        if(filterListPayload!=null){


            holder.checkboxRecyclerRight.setText(filterListPayload.getName());


            if(isUnSelectedAll){

                holder.checkboxRecyclerRight.setChecked(true);
            }
            else {

                holder.checkboxRecyclerRight.setChecked(false);
            }

          /*  if(ischecked.containsKey(position)){

                holder.checkboxRecyclerRight.setChecked(ischecked.get(position));

            }else {
                holder.checkboxRecyclerRight.setChecked(false);

            }*/


            holder.checkboxRecyclerRight.setOnCheckedChangeListener(null);


            holder.checkboxRecyclerRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if(isChecked){

                        holder.checkboxRecyclerRight.setChecked(isChecked);

                        ischecked.put(position, isChecked);

                        itemClickListenerCheck.itemClick(filterListPayload.getId());

                        footerFilter.setBackgroundColor(Color.parseColor("#00d048")); //darkgreen
                        tvClear.setTextColor(Color.parseColor("#FF6347")); //darkorange

                        footerFilter.setEnabled(true);

                    }
                    else {

                        ischecked.put(position, isChecked);

                        holder.checkboxRecyclerRight.setChecked(isChecked);

                        footerFilter.setBackgroundColor(Color.parseColor("#B2D0B2")); //dimgreen

                        footerFilter.setEnabled(false);
                    }


                }
            });


        }



    }






    @Override
    public int getItemCount() {
        return filterListPayloadList.size();

    }

    public class MyHolderRight extends RecyclerView.ViewHolder {


        public CheckBox checkboxRecyclerRight;

        public MyHolderRight(View itemView) {
            super(itemView);

            checkboxRecyclerRight = itemView.findViewById(R.id.checkboxRecyclerRight);





        }
    }
}


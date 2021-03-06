package com.test.baseshop.fragment_menu;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.baseshop.R;

import java.util.Objects;

public class fragment_menu extends Fragment implements Interfaces.View{

    private Interfaces.Presenter menu_presenter;

    private RecyclerViewAdapter adapter;
    private boolean isLoaded = false;
    private View view;

    public fragment_menu() {
    }

    public static fragment_menu newInstance() {
        return new fragment_menu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!isLoaded) view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!isLoaded) {
            menu_presenter.getSections();
            menu_presenter.getData(fragment_menu_presenter.ALL);
            isLoaded = true;
        }
//        menu_presenter.getData(fragment_menu_presenter.ALL);
//        menu_presenter.getData(getContext(), com.test.baseshop.fragment_menu.fragment_menu_presenter.ALL);
    }

    private void initPresenter(){
        this.menu_presenter = new fragment_menu_presenter(this);
    }

    @Override
    public void setAdapter(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
        View parent = getView();
        if(parent != null) {
            RecyclerView rv = parent.findViewById(R.id.menu_recyclerview);
            rv.setHasFixedSize(true);
            rv.setNestedScrollingEnabled(false);
            rv.setItemViewCacheSize(20);
            rv.setDrawingCacheEnabled(true);
            rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setOnScrollChangeListener(getOnScrollListenerForRv());
            rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
            rv.setAdapter(adapter);
        }
    }

    @Override
    public void updateRecyclerView() {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void clearAdapter() {
        if(adapter != null) adapter.clearAll();
    }

    @Override
    public void setSections(int[] sections_codes) {
        View.OnClickListener ocl = getOclForSectionItem();
        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout ll_sections = Objects.requireNonNull(getView()).findViewById(R.id.fragment_menu_ll_section);
        for(int section_code:sections_codes){
            View section = inflater.inflate(R.layout.fragment_menu_section_item,ll_sections,false);
            TextView section_view = section.findViewById(R.id.fragment_menu_section_item);
            section_view.setText(menu_presenter.getTitleOfSectionByCode(section_code));
            section.setTag(section_code);
            section.setOnClickListener(ocl);
            ll_sections.addView(section);
//            if(section_code == fragment_menu_presenter.ALL) {
//                menu_presenter.OnSectionItemClick(section_view);
//                menu_presenter.getData(section_code);
//            }
        }
    }

    @Override
    public void showMinusIconAndNumberOfItemForOrder(int position) {
        RecyclerView rv = Objects.requireNonNull(getView()).findViewById(R.id.menu_recyclerview);
        View v = Objects.requireNonNull(rv.getLayoutManager()).findViewByPosition(position);
        ImageView icon_minus = Objects.requireNonNull(v).findViewById(R.id.fragment_menu_rv_item_icon_minus);
        icon_minus.setVisibility(View.VISIBLE);
        TextView count_of_item_for_order = v.findViewById(R.id.fragment_menu_rv_item_count_of_item);
        count_of_item_for_order.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMinusIconAndNumberOfItemForOrder(int position) {
        RecyclerView rv = Objects.requireNonNull(getView()).findViewById(R.id.menu_recyclerview);
        View v = Objects.requireNonNull(rv.getLayoutManager()).findViewByPosition(position);
        ImageView icon_minus = Objects.requireNonNull(v).findViewById(R.id.fragment_menu_rv_item_icon_minus);
        icon_minus.setVisibility(View.INVISIBLE);
        TextView count_of_item_for_order = v.findViewById(R.id.fragment_menu_rv_item_count_of_item);
        count_of_item_for_order.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setNumberOfItemForOrder(int position, int number_of_item_for_order) {
        RecyclerView rv = Objects.requireNonNull(getView()).findViewById(R.id.menu_recyclerview);
        View v = Objects.requireNonNull(rv.getLayoutManager()).findViewByPosition(position);
        TextView count_of_item_for_order = Objects.requireNonNull(v).findViewById(R.id.fragment_menu_rv_item_count_of_item);
        count_of_item_for_order.setText(String.valueOf(number_of_item_for_order));
        count_of_item_for_order.setTag(number_of_item_for_order);
    }

    @Override
    public void showProgressBar() {
        View parent = getView();
        if(parent != null) {
            ProgressBar progressBar = parent.findViewById(R.id.fragment_menu_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        View parent = getView();
        if(parent != null) {
            ProgressBar progressBar = parent.findViewById(R.id.fragment_menu_progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener getOclForSectionItem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView rv = getView().findViewById(R.id.menu_recyclerview);
                RecyclerViewAdapter adapter = (RecyclerViewAdapter) rv.getAdapter();
                horizontalScroolToSection(v);
                for(int i = 0;i<adapter.getItemCount();i++){
                    if((adapter.ifExistsSectionTitleAt(i))&&(adapter.getSectionIntAt(i)==((Integer) v.getTag()))){
                        ((LinearLayoutManager) rv.getLayoutManager()).scrollToPositionWithOffset(i,20);
//                        break;
                    }
                }
                menu_presenter.OnSectionItemClick(v);
//                menu_presenter.getData((Integer) v.getTag());
            }
        };
    }

    private View.OnScrollChangeListener getOnScrollListenerForRv(){
        return new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) ((RecyclerView) v).getLayoutManager();
                if (linearLayoutManager != null) {
                    int i = linearLayoutManager.findFirstVisibleItemPosition();
                    RecyclerViewAdapter adapter = (RecyclerViewAdapter) ((RecyclerView) v).getAdapter();
                    if(adapter.ifExistsSectionTitleAt(i)) {
                        int section_view_int = adapter.getSectionIntAt(i);
                        View view_parent = getView();
                        if (view != null) {
                            View section_view = view_parent.<HorizontalScrollView>findViewById(R.id.fragment_menu_sections_horizontal_scrool_view).findViewWithTag(section_view_int);
                            horizontalScroolToSection(section_view);
                            menu_presenter.OnSectionItemClick(section_view);
                        }
                    }
                }
            }
        };
    }


    private void horizontalScroolToSection(View v){
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        HorizontalScrollView hr = (Objects.requireNonNull(getView()).findViewById(R.id.fragment_menu_sections_horizontal_scrool_view));
        float offset = (float) (size.x/2.26);
        hr.smoothScrollTo((int) (v.getLeft() - offset),0);
    }

}

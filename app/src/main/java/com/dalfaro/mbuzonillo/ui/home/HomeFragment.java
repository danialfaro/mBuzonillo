package com.dalfaro.mbuzonillo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.dalfaro.mbuzonillo.MainActivity;
import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.databinding.FragmentHomeBinding;
import com.dalfaro.mbuzonillo.ui.tabs.Tab1;
import com.dalfaro.mbuzonillo.ui.tabs.Tab2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private final String[] tabsTitles = new String[]{"Sensores","Historial"};

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Tabs
        ViewPager2 viewPager = root.findViewById(R.id.viewpager);
        viewPager.setAdapter(new HomeFragment.MiPagerAdapter(this));
        TabLayout tabs = root.findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position){
                        tab.setText(tabsTitles[position]);
                    }
                }
        ).attach();

        return root;
    }

    //Tabs Adapter
    public class MiPagerAdapter extends FragmentStateAdapter {

        public MiPagerAdapter(Fragment activity){
            super(activity);
        }
        @Override
        public int getItemCount() {
            return 2;
        }
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new Tab1();
                case 1: return new Tab2();
            }
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
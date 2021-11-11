package com.dalfaro.mbuzonillo.ui.home;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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

    ImageView imageViewBuzon;

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

        imageViewBuzon = root.findViewById(R.id.imageViewBuzon);
        imageViewBuzon.post(new Runnable() {
            @Override
            public void run() {
                startImageAnimation();
            }
        });

        return root;
    }

    private void startImageAnimation() {
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(
                //ObjectAnimator.ofFloat(imageViewBuzon, "translationX",-(imageViewBuzon.getWidth()), 0),
                ObjectAnimator.ofFloat(imageViewBuzon, "alpha", 0, 1),
                ObjectAnimator.ofFloat(imageViewBuzon, "scaleX", 0, 1),
        ObjectAnimator.ofFloat(imageViewBuzon, "scaleY", 0, 1)
        );


        animSet.setDuration(600);
        animSet.start();
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
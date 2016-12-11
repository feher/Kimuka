package net.feheren_fekete.kimuka;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.feheren_fekete.kimuka.availabilitylist.AvailabilityListFragment;


public class PagerFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public PagerFragment() {
    }

    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        mTabLayout = getMainActivity().getTabLayout();
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout.setupWithViewPager(mViewPager);

        PagerAdapter adapter = new ViewStatePagerAdapter(getMainActivity().getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private MainActivity getMainActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            return (MainActivity) activity;
        }
        // TODO: Handle error.
        throw new RuntimeException();
    }

//    private class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    private class ViewStatePagerAdapter extends FragmentStatePagerAdapter {

        public ViewStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AvailabilityListFragment.newInstance(null);
                case 1:
                    return AvailabilityListFragment.newInstance(null);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.availability_list_title_all);
                case 1:
                    return getResources().getString(R.string.availability_list_title_my);
                default:
                    return super.getPageTitle(position);
            }
        }
    }

}

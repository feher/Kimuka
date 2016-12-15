package net.feheren_fekete.kimuka;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.feheren_fekete.kimuka.availabilitylist.AvailabilityListFragment;
import net.feheren_fekete.kimuka.model.Availability;
import net.feheren_fekete.kimuka.model.Filter;
import net.feheren_fekete.kimuka.model.Request;
import net.feheren_fekete.kimuka.requestlist.RequestListFragment;

import java.util.ArrayList;
import java.util.List;


public class PagerFragment extends BaseFragment {

    private AppPreferences mAppPreferences;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
//    private ViewStatePagerAdapter mPagerAdapter;
    private ViewPagerAdapter mPagerAdapter2;

    public PagerFragment() {
    }

    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAppPreferences = new AppPreferences(getContext());

        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        mTabLayout = getMainActivity().getTabLayout();
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout.setupWithViewPager(mViewPager);

        //mPagerAdapter = new ViewStatePagerAdapter(getMainActivity().getSupportFragmentManager());
        mPagerAdapter2 = new ViewPagerAdapter(getChildFragmentManager());

        mPagerAdapter2.addFragment(
                AvailabilityListFragment.newInstance(mAppPreferences.getActiveFilter()),
                getResources().getString(R.string.availability_list_title_all));

        Request receivedRequestFilter = new Request();
        receivedRequestFilter.setReceiverKey(getUser().getKey());
        mPagerAdapter2.addFragment(
                RequestListFragment.newInstance(receivedRequestFilter),
                getResources().getString(R.string.request_list_title_received));

        Request sentRequestFilter = new Request();
        sentRequestFilter.setSenderKey(getUser().getKey());
        mPagerAdapter2.addFragment(
                RequestListFragment.newInstance(sentRequestFilter),
                getResources().getString(R.string.request_list_title_sent));

        mViewPager.setAdapter(mPagerAdapter2);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pager_menu, menu);

        menu.findItem(R.id.action_filter).setVisible(mViewPager.getCurrentItem() == 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_filter: {
                int fragmentPosition = mViewPager.getCurrentItem();
                AvailabilityListFragment fragment = (AvailabilityListFragment) mPagerAdapter2.getItem(fragmentPosition);
                fragment.filter();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0: {
                    getMainActivity().getFab().setVisibility(View.VISIBLE);
                    break;
                }
//                case 1: {
//                    getMainActivity().getFab().setVisibility(View.VISIBLE);
//                    break;
//                }
                case 1: {
                    getMainActivity().getFab().setVisibility(View.GONE);
                    break;
                }
                case 2: {
                    getMainActivity().getFab().setVisibility(View.GONE);
                    break;
                }
                default: {
                    break;
                }
            }
            getMainActivity().invalidateOptionsMenu();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    private class ViewStatePagerAdapter extends FragmentStatePagerAdapter {
//
//        public ViewStatePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0: {
//                    return AvailabilityListFragment.newInstance(mAppPreferences.getActiveFilter());
//                }
////                case 1: {
////                    Filter filter = new Filter();
////                    filter.setUserKey(getUser().getKey());
////                    return AvailabilityListFragment.newInstance(filter.toJson());
////                }
//                case 1: {
//                    Request filter = new Request();
//                    filter.setReceiverKey(getUser().getKey());
//                    return RequestListFragment.newInstance(filter);
//                }
//                case 2: {
//                    Request filter = new Request();
//                    filter.setSenderKey(getUser().getKey());
//                    return RequestListFragment.newInstance(filter);
//                }
//                default: {
//                    return null;
//                }
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
////        @Override
////        public int getItemPosition(Object object) {
////            return POSITION_NONE;
////        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return getResources().getString(R.string.availability_list_title_all);
////                case 1:
////                    return getResources().getString(R.string.availability_list_title_my);
//                case 1:
//                    return getResources().getString(R.string.request_list_title_received);
//                case 2:
//                    return getResources().getString(R.string.request_list_title_sent);
//                default:
//                    return super.getPageTitle(position);
//            }
//        }
//    }

}

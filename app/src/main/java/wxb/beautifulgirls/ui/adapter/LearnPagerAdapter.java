package wxb.beautifulgirls.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Date;

import wxb.beautifulgirls.constant.Constants;
import wxb.beautifulgirls.ui.fragment.GankFragment;

/**
 * Created by 黑月 on 2017/3/20.
 */

public class LearnPagerAdapter extends FragmentPagerAdapter {

    private Date mDate;

    public LearnPagerAdapter(FragmentManager fm, Date date) {
        super(fm);
        this.mDate = date;
    }

    @Override
    public Fragment getItem(int position) {
        Calendar calendar = getCurCalendar(position);
        return GankFragment.newInstance(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getCount() {
        return Constants.FRAGMENT_SIZE;
    }


    private Calendar getCurCalendar(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        calendar.add(Calendar.DATE, -position);
        return calendar;
    }
}

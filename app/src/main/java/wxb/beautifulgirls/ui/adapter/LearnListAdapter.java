package wxb.beautifulgirls.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wxb.beautifulgirls.R;
import wxb.beautifulgirls.data.entity.Gank;
import wxb.beautifulgirls.ui.WebActivity;

/**
 * Created by 黑月 on 2017/3/20.
 */

public class LearnListAdapter extends RecyclerView.Adapter<LearnListAdapter.MyHolder> {


    private List<Gank> mGankDatas = new ArrayList<>();
    private Context mContext;

    public LearnListAdapter(Context context) {
        this.mContext = context;

    }

    public void addItems(List<Gank> gankDatas) {
        mGankDatas.clear();
        mGankDatas.addAll(gankDatas);
        gankDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gank, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Gank gank = mGankDatas.get(position);
        if (position == 0) {
            showCategory(holder);
        } else {
            if (gank.getType().equals(mGankDatas.get(position - 1).getType())) {
                hideCategory(holder);
            } else {
                showCategory(holder);
            }
        }

        holder.mTvHead.setText(gank.getType());
        holder.mTvContent.setText(gank.getDesc());
        holder.mTvContent.setOnClickListener(view -> {
            Intent intent = WebActivity.newIntent(mContext, mGankDatas.get(position).getUrl(), mGankDatas.get(position).getDesc());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mGankDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView mTvHead;
        TextView mTvContent;


        public MyHolder(View itemView) {
            super(itemView);
            mTvHead = (TextView) itemView.findViewById(R.id.category);
            mTvContent = (TextView) itemView.findViewById(R.id.title);

        }
    }

    private void showCategory(MyHolder holder) {
        if (!isVisibleOf(holder.mTvHead)) holder.mTvHead.setVisibility(View.VISIBLE);
    }


    private void hideCategory(MyHolder holder) {
        if (isVisibleOf(holder.mTvHead)) holder.mTvHead.setVisibility(View.GONE);
    }

    /**
     * view.isShown() is a kidding...
     */
    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

}

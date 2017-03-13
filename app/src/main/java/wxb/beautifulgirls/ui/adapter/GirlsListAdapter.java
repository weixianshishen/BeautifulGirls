package wxb.beautifulgirls.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.data.entity.Girl;
import wxb.beautifulgirls.ui.GirlActivity;

/**
 * Created by 黑月 on 2017/3/10.
 */

public class GirlsListAdapter extends RecyclerView.Adapter<GirlsListAdapter.GirlsHolder> {
    private List<Girl> mGirlList = new ArrayList<>();
    private Context mContext;

    public GirlsListAdapter(Context context) {
        mContext = context;
    }

    public void addItems(List<Girl> datas) {
        mGirlList.addAll(datas);
        datas.clear();
        notifyDataSetChanged();
    }

    public void cleanItems() {
        mGirlList.clear();
    }


    @Override
    public GirlsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_girls, parent, false);
        GirlsHolder holder = new GirlsHolder(contentView);
        return holder;
    }

    @Override
    public void onBindViewHolder(GirlsHolder holder, int position) {
        Glide.with(mContext)
                .load(mGirlList.get(position).getUrl())
                .centerCrop()
                .into(holder.mImg);
        holder.mTitle.setText(mGirlList.get(position).getDesc());
        holder.mImg.setOnClickListener(view -> clickImg(position));
        holder.mTitle.setOnClickListener(view -> clickTitle(position));
    }

    @Override
    public int getItemCount() {
        return mGirlList.size();
    }

    class GirlsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_girls)
        ImageView mImg;
        @BindView(R.id.tv_title)
        TextView mTitle;

        public GirlsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void clickImg(int position) {
        Intent intent = new Intent(mContext, GirlActivity.class);
        intent.putExtra("imgUrl", mGirlList.get(position).getUrl());
        intent.putExtra("imgTitle", mGirlList.get(position).getDesc());
        mContext.startActivity(intent);
    }

    private void clickTitle(int position) {

    }

}

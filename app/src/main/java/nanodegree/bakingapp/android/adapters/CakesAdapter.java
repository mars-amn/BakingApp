package nanodegree.bakingapp.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.models.Cake;
import nanodegree.bakingapp.android.utils.GlideApp;


public class CakesAdapter extends RecyclerView.Adapter<CakesAdapter.CakesListViewHolder> {

    private Context mContext;
    private List<Cake> mCakeList;
    private CakeListener mOnCakeClickListener;

    public CakesAdapter(Context context, List<Cake> cakes, CakeListener cakeListener) {
        this.mContext = context;
        this.mCakeList = cakes;
        this.mOnCakeClickListener = cakeListener;
    }

    @NonNull
    @Override
    public CakesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cakesListView = LayoutInflater.from(mContext).inflate(R.layout.cakes_list_item, parent, false);

        final CakesListViewHolder cakesListViewHolder = new CakesListViewHolder(cakesListView);
        cakesListViewHolder.cakeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCakeClickListener.onCakeLickListener(cakesListViewHolder.getAdapterPosition());
            }
        });

        return cakesListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CakesListViewHolder holder, int position) {

        String cakeName = mCakeList.get(position).getName();
        holder.cakeName.setText(cakeName);
        String cakeImage = mCakeList.get(position).getImage();

        GlideApp.with(mContext)
                .load(cakeImage)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(holder.cakeImage);
    }

    @Override
    public int getItemCount() {
        if (mCakeList == null) {
            return 0;
        } else {
            return mCakeList.size();
        }
    }

    public void updateCakes(List<Cake> newCakes) {
        if (newCakes != null) {
            mCakeList = newCakes;
            notifyDataSetChanged();
        }
    }

    public interface CakeListener {
        void onCakeLickListener(int cakeIndex);
    }


    public static class CakesListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cakeListImage)
        ImageView cakeImage;
        @BindView(R.id.cakeListCardView)
        CardView cakeCard;
        @BindView(R.id.cakeListText)
        TextView cakeName;

        public CakesListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
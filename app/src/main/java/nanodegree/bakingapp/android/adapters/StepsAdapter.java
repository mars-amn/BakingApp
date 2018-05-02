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

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.models.Steps;
import nanodegree.bakingapp.android.utils.GlideApp;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private static final String TAG = StepsAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Steps> mStepsList;
    private final onStepClick onStepClickListener;

    public StepsAdapter(Context mContext, List<Steps> mStepsList, onStepClick listener) {
        this.mContext = mContext;
        this.mStepsList = mStepsList;
        this.onStepClickListener = listener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View stepsListView = LayoutInflater.from(mContext).inflate(R.layout.steps_list_item, parent, false);
        final StepsViewHolder stepsViewHolder = new StepsViewHolder(stepsListView, viewType);

        stepsViewHolder.stepsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStepClickListener.onStepClickListener(mStepsList.get(stepsViewHolder.getAdapterPosition()));
            }
        });
        return stepsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        String url = mStepsList.get(position).getVideoURL();

        if (url.equals("")) {
            holder.mTimeLine.setMarker(mContext.getResources().getDrawable(R.drawable.ic_clipboard_text));

        } else {
            holder.mTimeLine.setMarker(mContext.getResources().getDrawable(R.drawable.ic_video));
        }

        String thumbnailUrl = mStepsList.get(position).getThumbnailURL();
        if (thumbnailUrl.equals("")) {
            holder.recipeThumbnailImage.setVisibility(View.GONE);
        } else {
            GlideApp.with(mContext)
                    .asBitmap()
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.recipeThumbnailImage);
        }
        String shortDescription = mStepsList.get(position).getShortDescription();
        String description = mStepsList.get(position).getDescription();
        holder.shortDescriptionTextView.setText(shortDescription);
        holder.descriptionTextView.setText(description);

    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        if (mStepsList == null) {
            return 0;
        } else {
            return mStepsList.size();
        }

    }

    public interface onStepClick {
        void onStepClickListener(Steps step);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipeThumbnailImageView)
        ImageView recipeThumbnailImage;

        @BindView(R.id.stepShortDescriptionTextView)
        TextView shortDescriptionTextView;

        @BindView(R.id.stepsCardView)
        CardView stepsCardView;

        @BindView(R.id.stepDescriptionTextView)
        TextView descriptionTextView;

        @BindView(R.id.time_marker)
        TimelineView mTimeLine;

        public StepsViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTimeLine.initLine(viewType);
        }
    }
}

package nanodegree.bakingapp.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.bakingapp.android.R;
import nanodegree.bakingapp.android.models.Ingredient;
import nanodegree.bakingapp.android.utils.GlideApp;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsListViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredientList;

    public IngredientsAdapter(Context mContext, List<Ingredient> mIngredientList) {
        this.mContext = mContext;
        this.mIngredientList = mIngredientList;
    }

    @NonNull
    @Override
    public IngredientsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingredientsListView = LayoutInflater.from(mContext).inflate(R.layout.ingredients_list_item, parent, false);
        return new IngredientsListViewHolder(ingredientsListView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsListViewHolder holder, int position) {
        String ingredientName = mIngredientList.get(position).getIngredient();
        double quantity = mIngredientList.get(position).getQuantity();
        DecimalFormat quantityFormatter = new DecimalFormat("0.#");
        String quantityValue = quantityFormatter.format(quantity);
        String measure = mIngredientList.get(position).getMeasure();

        int measureIcon = getAppropriateIcon(measure);


        holder.ingredientText.setText(ingredientName);
        holder.ingredientQuantityText.setText(quantityValue);
        GlideApp.with(mContext)
                .load(measureIcon)
                .into(holder.ingredientIcon);
    }


    private int getAppropriateIcon(String unit) {
        int measureIcon = R.drawable.ic_launcher;
        switch (unit) {
            case "G":
                measureIcon = R.drawable.ic_gram;
                break;
            case "TSP":
                measureIcon = R.drawable.ic_tea_spoon;
                break;
            case "TBLSP":
                measureIcon = R.drawable.ic_table_spoon;
                break;
            case "OZ":
                measureIcon = R.drawable.ic_ounce_scale;
                break;
            case "CUP":
                measureIcon = R.drawable.ic_cup;
                break;
            case "K":
                measureIcon = R.drawable.ic_kilogram;
                break;
            case "UNIT":
                measureIcon = R.drawable.ic_apple;
                break;
        }
        return measureIcon;
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) {
            return 0;
        } else {
            return mIngredientList.size();
        }
    }

    public class IngredientsListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientsMeasuringImageView)
        ImageView ingredientIcon;

        @BindView(R.id.ingredientsTextView)
        TextView ingredientText;

        @BindView(R.id.ingredientsQuantityTextView)
        TextView ingredientQuantityText;

        public IngredientsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

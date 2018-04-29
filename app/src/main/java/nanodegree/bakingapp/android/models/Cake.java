package nanodegree.bakingapp.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class Cake implements Parcelable {

    public final static Parcelable.Creator<Cake> CREATOR = new Creator<Cake>() {


        @SuppressWarnings({"unchecked"})
        public Cake createFromParcel(Parcel in) {
            return new Cake(in);
        }

        public Cake[] newArray(int size) {
            return (new Cake[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = new ArrayList<>();
    @SerializedName("steps")
    @Expose
    private List<Steps> steps = new ArrayList<>();
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    @SuppressWarnings("WeakerAccess")
    protected Cake(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.ingredients, (nanodegree.bakingapp.android.models.Ingredient.class.getClassLoader()));
        in.readList(this.steps, (nanodegree.bakingapp.android.models.Steps.class.getClassLoader()));
        this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Cake() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeValue(id);
        parcel.writeValue(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeValue(servings);
        parcel.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}

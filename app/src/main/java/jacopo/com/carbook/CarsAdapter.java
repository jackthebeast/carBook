package jacopo.com.carbook;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import jacopo.com.carbook.databinding.ItemCarBinding;
import jacopo.com.carbook.databinding.ItemCarBinding;
import jacopo.com.carbook.model.Car;

/**
 * Created by jacop on 20/11/2017.
 */

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarViewHolder> {

    private List<Car> cars;

    public static class CarViewHolder extends RecyclerView.ViewHolder {

        final ItemCarBinding binding;

        public CarViewHolder(ItemCarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @BindingAdapter({"photoSource"})
        public static void loadPhoto(ImageView imageView, String url) {
            if(url != null)
                Glide.with(imageView.getContext()).load(url).into(imageView);
        }

    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCarBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_car,
                        parent, false);


        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        holder.binding.setCar(cars.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return cars == null ? 0 : cars.size();
    }

    public void setCars(List<Car> cars){
        this.cars = cars;
        notifyDataSetChanged();
    }

}



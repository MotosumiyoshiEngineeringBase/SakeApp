package base.engineering.motosumiyoshi.sakeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.model.Product;

public class ProductAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private int layoutID;

    private List<Product> productList;

    static class ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView priceView;
    }

    public ProductAdapter(
            Context context,
            int itemLayoutId,
            List<Product> productList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.layoutID = itemLayoutId;
        this.productList = productList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.item_image);
            holder.nameView = convertView.findViewById(R.id.item_name);
            holder.priceView = convertView.findViewById(R.id.item_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = productList.get(position);
        Picasso.get().load(product.getImageURL()).into(holder.imageView);
        holder.nameView.setText(product.getItemName());
        holder.priceView.setText(product.getItemPrice() + context.getString(R.string.yen));
        return convertView;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

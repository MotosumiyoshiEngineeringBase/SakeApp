package base.engineering.motosumiyoshi.sakeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import base.engineering.motosumiyoshi.sakeapp.chatkit.commons.ImageLoader;
import base.engineering.motosumiyoshi.sakeapp.chatkit.dialogs.DialogsListAdapter;
import base.engineering.motosumiyoshi.sakeapp.utility.AppUtils;
import base.engineering.motosumiyoshi.sakeapp.chatkit.commons.models.Dialog;

/*
 * Created by troy379 on 05.04.17.
 */
public abstract class AbstractDialogsActivity extends AppCompatActivity
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        };
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        AppUtils.showToast(
                this,
                dialog.getDialogName(),
                false);
    }
}

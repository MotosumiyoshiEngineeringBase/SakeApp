package base.engineering.motosumiyoshi.sakeapp.viewhelper;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public class BottomNavigationViewHelper {
    /**
     * BottomNavigationViewのアイテムのサイズの調整、アイコンサイズ調整、タイトルの削除
     * @param view
     */
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                //アイテムの幅調整
                BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) menuView.getChildAt(i);
                bottomNavigationItemView.setShiftingMode(false);
                bottomNavigationItemView.setChecked(false);

                final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
                final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
                final DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                iconView.setLayoutParams(layoutParams);
            }
        } catch (NoSuchFieldException e) {
            //ignore
        } catch (IllegalAccessException e) {
            //ignore
        }

    }
}

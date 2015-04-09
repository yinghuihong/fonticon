package com.shamanland.fonticon.example;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shamanland.fonticon.FontIconButton;
import com.shamanland.fonticon.FontIconDrawable;
import com.shamanland.fonticon.FontIconTextView;
import com.shamanland.fonticon.FontIconToggleButton;

public class CompoundIconsFragment extends ContentFragment {
    private static final int[] sIcons = {R.string.ic_android, R.string.ic_camera, R.string.ic_compound};
    private static final int[] sColors = {Color.GREEN, 0xffff8000, Color.MAGENTA
    };

    protected FontIconDrawable mIcon;
    protected int mColorIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View result = inflater.inflate(R.layout.f_compound, container, false);

        final FontIconTextView tv = (FontIconTextView) result.findViewById(R.id.example_text);

        final Drawable[] drawables;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // left, top, right, bottom
            drawables = tv.getCompoundDrawables();
        } else {
            // start, top, end, bottom
            drawables = tv.getCompoundDrawablesRelative();
        }

        mIcon = (FontIconDrawable) drawables[0];

        if (state != null) {
            mIcon.onRestoreInstanceState(state.getParcelable("icon"));

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv.updateCompoundDrawables();
            } else {
                tv.updateCompoundDrawablesRelative();
            }

            mColorIndex = state.getInt("color.index");
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorIndex = (mColorIndex + 1) % sIcons.length;

                mIcon.setText(getString(sIcons[mColorIndex]));
                mIcon.setTextColor(sColors[mColorIndex]);

                // use FontIconTextView method
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.updateCompoundDrawables();
                } else {
                    tv.updateCompoundDrawablesRelative();
                }

                // or CompoundDrawables.update(TextView) if you don't want to cast FontIconTextView

                // or manually
                // tv.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        });
        test(result);
        return result;
    }

    void test(View view) {
        final FontIconButton button = (FontIconButton) view.findViewById(R.id.yes);
        FontIconDrawable drawable = FontIconDrawable.inflate(getContext(), R.xml.ic_button_no);
        drawable.setText(getString(R.string.ic_github));
        drawable.setTextColorStateList(StateListUtil.createColorStateList(0xff00cfff, 0xffffcf00, 0xffffcf00, 0xff00cfff));
        button.setText("ddd");
        button.setTextColor(StateListUtil.createColorStateList(0xff00cfff, 0xffffcf00, 0xffffcf00, 0xff00cfff));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            button.setCompoundDrawables(null, drawable, null, null);
        } else {
            button.setCompoundDrawablesRelative(null, drawable, null, null);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setSelected(!button.isSelected());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        state.putParcelable("icon", mIcon != null ? mIcon.onSaveInstanceState() : null);
        state.putInt("color.index", mColorIndex);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.compound, menu);

        menu.findItem(R.id.action_like).setIcon(FontIconDrawable.inflate(getContext(), R.xml.ic_ab_like));
        menu.findItem(R.id.action_group).setIcon(FontIconDrawable.inflate(getContext(), R.xml.ic_ab_group));
    }
}

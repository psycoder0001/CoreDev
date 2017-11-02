package com.deepdroid.coredev.devdialog.uifordevdialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.deepdroid.coredev.R;
import com.deepdroid.coredev.customview.CoreDevListWithoutScroll;
import com.deepdroid.coredev.devdialog.DevelopmentDialogListener;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class CustomDevelopmentList extends CoreDevListWithoutScroll {

    private DevelopmentDialogListener devDialogListener;

    public CustomDevelopmentList(Context context, DevelopmentDialogListener devDialogListener) {
        super(context);
        this.devDialogListener = devDialogListener;
    }

    @Override
    protected void setDataAt(int index, View convertView, Object viewData) {
        CustomCheckViewHolder viewHolder;
        if (convertView.getTag() == null) {
            viewHolder = new CustomCheckViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomCheckViewHolder) convertView.getTag();
        }
        viewHolder.setData(viewData);
    }

    @Override
    protected void onItemClick(View view, int index) {
        if (view.getTag() != null && view.getTag() instanceof CustomCheckViewHolder) {
            Class dataType = ((CustomCheckViewHolder) view.getTag()).dataType;
            if (dataType == null || devDialogListener == null) {
                return;
            }
            if (dataType == CustomDevelopmentButtonItem.class) {
                // Button clicked.
                devDialogListener.onCustomButtonClicked(index);
            } else if (dataType == CustomDevelopmentCheckItem.class) {
                // Check changed.
                ((CustomCheckViewHolder) view.getTag()).check.performClick();
                devDialogListener.onCustomCheckChanged(index, ((CustomCheckViewHolder) view.getTag()).check.isChecked());
            }
        }
    }

    private class CustomCheckViewHolder {
        private View root;
        private CheckBox check;
        private TextView text;
        private Class dataType;

        private CustomCheckViewHolder(View convertView) {
            root = convertView.findViewById(R.id.development_item_custom_root);
            check = convertView.findViewById(R.id.development_item_custom_check_check);
            text = convertView.findViewById(R.id.development_item_custom_check_text);
        }

        public void setData(Object data) {
            if (data == null) {
                return;
            }
            if (data instanceof CustomDevelopmentButtonItem) {
                dataType = CustomDevelopmentButtonItem.class;
                root.setBackgroundResource(R.drawable.dev_bg_button);
                check.setVisibility(View.INVISIBLE);
                text.setText(((CustomDevelopmentButtonItem) data).text);
            } else if (data instanceof CustomDevelopmentCheckItem) {
                dataType = CustomDevelopmentCheckItem.class;
                check.setChecked(((CustomDevelopmentCheckItem) data).isChecked);
                text.setText(((CustomDevelopmentCheckItem) data).text);
            }
        }
    }
}
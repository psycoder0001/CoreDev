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
        CustomDevelopmentItemViewHolder viewHolder;
        if (convertView.getTag() == null) {
            viewHolder = new CustomDevelopmentItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomDevelopmentItemViewHolder) convertView.getTag();
        }
        viewHolder.setData(viewData);
    }

    @Override
    protected void onItemClick(View view, int index) {
        if (view.getTag() == null || devDialogListener == null || !(view.getTag() instanceof CustomDevelopmentItemViewHolder)) {
            return;
        }
        CustomDevelopmentItemViewHolder holder = ((CustomDevelopmentItemViewHolder) view.getTag());
        if (holder.data == null) {
            return;
        }
        if (holder.data instanceof CustomDevelopmentCheckItem) {
            // Check changed.
            holder.switchCheck();
            devDialogListener.onCustomCheckChanged((CustomDevelopmentCheckItem) holder.data);
            holder.applyDataChanges();
        } else if (holder.data instanceof CustomDevelopmentButtonItem) {
            // Button clicked.
            devDialogListener.onCustomButtonClicked((CustomDevelopmentButtonItem) holder.data);
            holder.applyDataChanges();
        }
    }

    private class CustomDevelopmentItemViewHolder {
        private final int padding;
        private View root;
        private CheckBox check;
        private TextView text;
        private Object data;

        private CustomDevelopmentItemViewHolder(View convertView) {
            root = convertView.findViewById(R.id.development_item_custom_root);
            check = convertView.findViewById(R.id.development_item_custom_check_check);
            text = convertView.findViewById(R.id.development_item_custom_check_text);

            padding = root.getPaddingTop() * 2;
        }

        public void setData(Object object) {
            if (object == null) {
                return;
            }
            this.data = object;
            if (data instanceof CustomDevelopmentButtonItem) {
                root.setBackgroundResource(R.drawable.dev_bg_button);
                root.setPadding(0, padding, 0, padding);
                check.setVisibility(View.INVISIBLE);
                text.setText(((CustomDevelopmentButtonItem) data).text);
            } else if (data instanceof CustomDevelopmentCheckItem) {
                check.setChecked(((CustomDevelopmentCheckItem) data).isChecked);
                text.setText(((CustomDevelopmentCheckItem) data).text);
            }
        }

        private void switchCheck() {
            if (data instanceof CustomDevelopmentCheckItem) {
                check.performClick();
                ((CustomDevelopmentCheckItem) data).isChecked = check.isChecked();
            }
        }

        private void applyDataChanges() {
            if (data instanceof CustomDevelopmentCheckItem) {
                check.setChecked(((CustomDevelopmentCheckItem) data).isChecked);
                text.setText(((CustomDevelopmentCheckItem) data).text);
            } else if (data instanceof CustomDevelopmentButtonItem) {
                text.setText(((CustomDevelopmentButtonItem) data).text);
            }
        }
    }
}
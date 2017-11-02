package com.deepdroid.coredev.devdialog.serviceurlselection;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.deepdroid.coredev.R;
import com.deepdroid.coredev.customview.CoreDevListWithoutScroll;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class SelectableServiceUrlList extends CoreDevListWithoutScroll {
    private SelectableServiceUrlListListener selectableServiceUrlListListener;

    public SelectableServiceUrlList(Context context, SelectableServiceUrlListListener selectableServiceUrlListListener) {
        super(context);
        this.selectableServiceUrlListListener = selectableServiceUrlListListener;
    }

    @Override
    protected void setDataAt(int index, View convertView, Object viewData) {
        ViewHolder viewHolder = getViewHolderAt(index);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        if (viewData instanceof SelectableServiceUrlItem) {
            viewHolder.setData((SelectableServiceUrlItem) viewData, index);
        }
    }

    @Override
    protected void onItemClick(View view, int index) {
        // Nothing happens
    }

    public String geCustomUrlValueAt(int index) {
        ViewHolder viewHolder = getViewHolderAt(index);
        if (viewHolder == null) {
            return "";
        }
        return viewHolder.urlTv.getText().toString();
    }

    private class ViewHolder {
        private TextView titleTv;
        private TextView urlTv;
        private View changeUrlV;

        private ViewHolder(View root) {
            titleTv = root.findViewById(R.id.development_service_link_title);
            urlTv = root.findViewById(R.id.development_service_link_value);
            changeUrlV = root.findViewById(R.id.development_service_link_change);
        }

        private void setData(SelectableServiceUrlItem data, final int index) {
            titleTv.setText(data.title);
            urlTv.setText(data.getSelectedUrl());
            changeUrlV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectableServiceUrlListListener != null) {
                        selectableServiceUrlListListener.onUrlChangeRequestedFor(index);
                    }
                }
            });
        }
    }

    public interface SelectableServiceUrlListListener {
        void onUrlChangeRequestedFor(int index);
    }
}

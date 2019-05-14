package com.wezpigulke.widget;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViewsService;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetAdapter(this);
    }
}

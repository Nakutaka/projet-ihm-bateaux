package com.example.project.main;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.List;

public class ReportItemizedOverlay extends ItemizedIconOverlay {
    Context context;

    public ReportItemizedOverlay(List pList, Drawable pDefaultMarker, OnItemGestureListener pOnItemGestureListener, Context pContext) {
        super(pList, pDefaultMarker, pOnItemGestureListener, pContext);
        context = pContext;
    }

    public void setFocusItemsOnTap(boolean b) {
        super.setDrawFocusedItem(b);
    }


    @Override
    protected boolean onTap(int index) {
        // TODO Auto-generated method stub
        return true;
    }
}

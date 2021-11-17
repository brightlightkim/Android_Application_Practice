package com.example.familymap.data;

import android.graphics.Color;

public class MapColor extends Color {

    private float color;

    // ========================== Constructor ========================================
    public MapColor(String eventType)
    {
        color = Math.abs(eventType.hashCode() % 360);
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public float getColor()
    {
        return color;
    }
}
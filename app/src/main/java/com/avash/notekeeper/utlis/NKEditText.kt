package com.avash.notekeeper.utlis

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class NKEditText(context: Context, attr: AttributeSet):AppCompatEditText(context, attr) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }



}
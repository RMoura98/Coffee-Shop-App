package com.feup.cmov.acme_client.utils

import android.view.ViewGroup
import com.feup.cmov.acme_client.MainActivity
import com.google.android.material.snackbar.Snackbar

object ShowFeedback {
    fun makeSnackbar(text: String, duration: Int = Snackbar.LENGTH_LONG){
        val contentViewGroup : ViewGroup = MainActivity.getActivity().findViewById(android.R.id.content) as ViewGroup;
        Snackbar.make(contentViewGroup.getChildAt(0), text, duration).show();
    }
}
package com.feup.cmov.acme_client.utils

import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.MainActivity

object Measurements {
    /**
     * Converts dp (density pixels) to px (pixels).
     */
    fun convertDptoPx(dp: Long): Long {
        val scale: Float = MainActivity.getActivity().resources.displayMetrics.density
        val padding_in_px = (dp * scale + 0.5f).toLong()
        return padding_in_px
    }
}
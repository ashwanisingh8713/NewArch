package com.github.kotvertolet.youtubejextractor.models.newModels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class AnnotationsItem : Parcelable, Serializable {
    @SerializedName("playerAnnotationsExpandedRenderer")
    var playerAnnotationsExpandedRenderer: PlayerAnnotationsExpandedRenderer? = null
    override fun toString(): String {
        return "AnnotationsItem{" +
                "playerAnnotationsExpandedRenderer = '" + playerAnnotationsExpandedRenderer + '\'' +
                "}"
    }
}
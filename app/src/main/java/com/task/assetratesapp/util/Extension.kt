package com.task.assetratesapp.util

import com.task.assetratesapp.domain.core.model.Asset
import com.task.assetratesapp.domain.core.model.AssetPresentation

class Extension {

    fun Asset.toPresentation(): AssetPresentation {
        // For example, we format the rate to 4 decimal places.
        val formattedRate = String.format("%.4f", rate)
        return AssetPresentation(
            code = code,
            displayRate = formattedRate
        )
    }
}
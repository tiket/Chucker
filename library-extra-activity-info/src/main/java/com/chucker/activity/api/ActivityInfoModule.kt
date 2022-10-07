package com.chucker.activity.api

import android.app.Activity
import android.app.Application
import android.content.Context
import com.chucker.activity.internal.ActivityInfoActivity
import com.chucker.activity.internal.ActivityTrackerCallback
import com.chucker.activity.internal.NotificationHelper
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class ActivityInfoModule(
    app: Application,
    enableNotification: Boolean = true,
    additionalInfo: ((Activity) -> Map<String, String>?)? = null
) : ChuckerExtraModule {

    private val tracker: ActivityTrackerCallback = ActivityTrackerCallback(
        app = app,
        notificationHelper = if (enableNotification) NotificationHelper(app) else null,
        additionalInfo = additionalInfo
    )

    override val moduleName: String = "Activity Info"

    override fun onNavigateToModule(context: Context) {
        val info = tracker.currentInfo ?: return
        ActivityInfoActivity.start(context, info)
    }
}
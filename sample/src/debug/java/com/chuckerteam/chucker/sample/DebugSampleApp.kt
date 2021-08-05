package com.chuckerteam.chucker.sample

import android.app.Application
import com.chucker.featureflag.api.FeatureFlagModule
import com.chucker.featureflag.api.store.ObservedFeatureFlagStore
import com.chucker.featureflag.api.store.PersistentFeatureFlagStore
import com.chuckerteam.chucker.api.extramodule.ExtraModuleRegistry

class DebugSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        ExtraModuleRegistry.addExtraModule(
            FeatureFlagModule(
                passedStore = ObservedFeatureFlagStore(
                    PersistentFeatureFlagStore(applicationContext)
                ) { featureName, isEnabled ->
                    SampleFeatureFlag.valueOf(featureName).isEnabled = isEnabled
                },
                initialFlags = { store ->
                    SampleFeatureFlag.values().forEach {
                        store.set(it.name, false)
                    }
                },
                onLoad = { store ->
                    store.getAll()
                        .forEach {
                            SampleFeatureFlag.valueOf(it.first).isEnabled = it.second
                        }
                }
            )
        )
    }
}
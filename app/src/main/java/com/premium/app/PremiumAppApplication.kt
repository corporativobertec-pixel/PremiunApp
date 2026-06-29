package com.premium.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PremiumAppApplication : Application() {
    // This class is needed for Hilt to generate the necessary code
}

package com.nacai.baseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.nacai.base_lib.extens.onClick
import com.nacai.base_lib.provider.ProviderManager
import com.nacai.common.provider.MainProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).onClick {
            ProviderManager.get<MainProvider>()?.show("MainActivity Call")
        }
    }
}
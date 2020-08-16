package com.example.hellokitty

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.view.animation.Animation
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        val flipAnimation = FlipAnimation(coin)

        coin.setOnClickListener {
            flipAnimation.repeatCount = Random.nextInt(10,20)
            coin.startAnimation(flipAnimation)
        }
    }
}

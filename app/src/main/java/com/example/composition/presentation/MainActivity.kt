package com.example.composition.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.composition.R

class MainActivity : AppCompatActivity(), WelcomeFragment.OnButtonUnderstandClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onButtonUnderstandClick() {
        Toast.makeText(this, "Хорошо !", Toast.LENGTH_SHORT).show()
    }
}
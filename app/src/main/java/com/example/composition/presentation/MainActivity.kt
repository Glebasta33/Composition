package com.example.composition.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R

class MainActivity : AppCompatActivity(), GameFragment.ShowCongratulations {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showWin() {
        Toast.makeText(application, "Победа!", Toast.LENGTH_LONG).show()
    }

    override fun showLose() {
        Toast.makeText(application, "Проигрыш", Toast.LENGTH_LONG).show()
    }
}
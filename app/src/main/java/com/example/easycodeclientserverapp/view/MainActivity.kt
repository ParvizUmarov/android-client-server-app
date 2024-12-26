package com.example.easycodeclientserverapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.easycodeclientserverapp.App
import com.example.easycodeclientserverapp.data.callback.DataCallback
import com.example.easycodeclientserverapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = (application as App).viewModel

        val button = binding.button
        val textView = binding.textView
        val progressBar = binding.progressBar
        val checkBox = binding.checkbox
        val iconView = binding.favoriteButton

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeJokeStatus()
        }

        progressBar.visibility = View.INVISIBLE

        button.setOnClickListener{
            button.isEnabled = false
            progressBar.visibility = View.VISIBLE
            viewModel.getJoke()
        }

        viewModel.init(object: DataCallback {

            override fun provideText(text: String) = runOnUiThread() {
                button.isEnabled = true
                progressBar.visibility = View.INVISIBLE
                textView.text = text
            }

            override fun provideIconRes(id: Int) = iconView.setImageResource(id)
        })
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }
}
package com.farad.entertainment.gemini.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.farad.entertainment.gemini.R
import com.farad.entertainment.gemini.api.network.ApiResponse
import com.farad.entertainment.gemini.ui.viewModel.GeminiViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<GeminiViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLoading = findViewById<LinearLayout>(R.id.linear_loading)
        val edtGemini = findViewById<AppCompatTextView>(R.id.edt_gemini)
        val button = findViewById<AppCompatButton>(R.id.action_go)
        val txtDesc = findViewById<AppCompatTextView>(R.id.txt_desc)
        val textView = findViewById<AppCompatTextView>(R.id.txt_response)

        button.setOnClickListener {
            if (edtGemini.text.isNullOrEmpty())
                return@setOnClickListener
            val algorithm = "Please analyze the following English\n" +
                    "\n" +
                    "Word: ${edtGemini.text}\n" +
                    "\n" +
                    "1. What is the part of speech? (e.g. noun, verb, adjective, etc.)\n" +
                    "2. Give one simple English sentence using this word.\n" +
                    "3. Translate that sentence into Persian (Farsi)." +
                    "Just remove the extra sentences\n" +
                    "It's very simple, just leave what I want."
            viewModel.geminiResponse(algorithm)
        }

        lifecycleScope.launch {
            viewModel.progress.collect {
                linearLoading.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.words.collect{
                val data = it.first()
                edtGemini.text = data.word
                txtDesc.text = "pronun : ${data.pronun}"
                    .plus("\n")
                    .plus("example : ${data.example}")
                    .plus("\n")
                    .plus("example2 : ${data.example2}")
                    .plus("\n")
            }
        }

        lifecycleScope.launch {
            viewModel.geminiResponse.collect {
                if (it is ApiResponse.Success) {
                    textView.text = it.body?.candidates?.first()?.content?.parts?.first()?.text
                }

                if(it is ApiResponse.ExpireToken){
                    textView.text = "HTTP_FORBIDDEN  403"
                }
            }
        }
    }
}
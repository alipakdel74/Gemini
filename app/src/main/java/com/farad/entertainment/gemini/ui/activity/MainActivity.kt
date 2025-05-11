package com.farad.entertainment.gemini.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farad.entertainment.gemini.R
import com.farad.entertainment.gemini.api.model.WordEntry
import com.farad.entertainment.gemini.api.network.ApiResponse
import com.farad.entertainment.gemini.ui.adapter.MyAdapter
import com.farad.entertainment.gemini.ui.viewModel.GeminiViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<GeminiViewModel>()

    private val edtGemini by lazy { findViewById<AppCompatTextView>(R.id.edt_gemini) }
    private val txtDesc by lazy { findViewById<AppCompatTextView>(R.id.txt_desc) }

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLoading = findViewById<LinearLayout>(R.id.linear_loading)
        val button = findViewById<AppCompatButton>(R.id.action_go)
        val actionSelect = findViewById<AppCompatButton>(R.id.action_select)
        val textView = findViewById<AppCompatTextView>(R.id.txt_response)

        actionSelect.setOnClickListener {
            alertDialog?.show()
        }

        button.setOnClickListener {
            if (edtGemini.text.isNullOrEmpty())
                return@setOnClickListener
            val algorithm = "Please analyze the following English\n" +
                    "\n" +
                    "Word: ${edtGemini.text}\n" +
                    "mean: persian(farsi)\n" +
                    "Determine what types it has. : (e.g. noun, verb, adjective, etc.)\n" +
                    "Example: For each (e.g. noun, verb, adjective, etc.), write an example with the Persian meaning.\n\n" +
                    "mean: Translate that sentence into Persian (Farsi).\n" +
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
            viewModel.words.collect {
                showRecyclerDialog(it)
            }
        }

        lifecycleScope.launch {
            viewModel.geminiResponse.collect {
                if (it is ApiResponse.Success) {
                    textView.text = it.body?.candidates?.first()?.content?.parts?.first()?.text
                        ?.replace("*", "")?.trim()
                }

                if (it is ApiResponse.ExpireToken) {
                    textView.text = "HTTP_FORBIDDEN  403"
                }
            }
        }
    }

    private fun showRecyclerDialog(items: List<WordEntry>) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_recycler, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(items) { selectedItem ->
            edtGemini.text = selectedItem.word
            txtDesc.text = "pronun : ${selectedItem.pronun}"
                .plus("\n")
                .plus("example : ${selectedItem.example}")
                .plus("\n")
                .plus("example2 : ${selectedItem.example2}")
                .plus("\n")
            alertDialog?.dismiss()
        }

        alertDialog = AlertDialog.Builder(this)
            .setTitle("Select Item")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()
    }
}
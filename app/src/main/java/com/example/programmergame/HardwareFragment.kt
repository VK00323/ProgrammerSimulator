package com.example.programmergame

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HardwareFragment : Fragment() {

    companion object {
        fun newInstance() = HardwareFragment()
    }

    private lateinit var viewModel: HardwareViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Hello(name = "Hardware")
            }
        }
    }

    @Composable
    fun Hello(name: String) {
        Text(
            text = "This is $name fragment",
            modifier = Modifier.padding(32.dp),
            fontSize = 32.sp,
        )
    }

    @Preview(
        showBackground = true,
        fontScale = 2f,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        showSystemUi = true,
    )
    @Composable
    fun HelloPreview(){
        Hello("Hardware")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[HardwareViewModel::class.java]
        // TODO: Use the ViewModel
    }

}
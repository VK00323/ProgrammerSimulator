package com.example.programmergame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragmentTest: BottomSheetDialogFragment() {


//    private val binding by viewBinding(FragmentBottomSheetTestBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_test,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.openFragmentButton)
        button.setOnClickListener{
//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.nav_host_fragment_content_main, FullScreenTestFragment())
//                ?.commit()
            findNavController().navigate(
                R.id.action_navigation_bottomSheetFragmentTest2_to_fullScreenTestFragment,
                Bundle(),
                navOption()
            )

        }
    }

    private fun navOption() = NavOptions.Builder().setRestoreState(true).setLaunchSingleTop(true).build()
}
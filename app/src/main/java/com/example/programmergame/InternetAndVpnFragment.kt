package com.example.programmergame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class InternetAndVpnFragment : Fragment() {

    companion object {
        fun newInstance() = InternetAndVpnFragment()
    }

    private lateinit var viewModel: InternetAndVpnViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_internet_and_vpn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[InternetAndVpnViewModel::class.java]
        // TODO: Use the ViewModel
    }

}
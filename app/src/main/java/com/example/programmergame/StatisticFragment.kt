package com.example.programmergame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.programmergame.database.AppDatabase
import com.example.programmergame.databinding.FragmentPlayGameBinding
import javax.inject.Inject

class StatisticFragment : Fragment() {

    private var _binding: FragmentPlayGameBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: GameViewModel
    var params: TestParams? = null


    @Inject
    lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayGameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (requireActivity().application as App).appComponent.inject(this)
//         params = arguments?.getSerializable("navigation data") as TestParams

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        lifecycleScope.launchWhenResumed {
            viewModel.allGameValue()
                .observe(viewLifecycleOwner) {
                    binding.textViewMoneyRub.text = it?.toString() ?: "100"
                }
        }

        binding.buttonProgram.setOnClickListener {
            viewModel.download()
        }

        val args: StatisticFragmentArgs by navArgs()
        val rt = args.params
        binding.textViewInfo.text = rt?.first + rt?.second

    }
}
//        viewModel.allGameValue().observe(this, { gameValue ->
//            buttonProgram.setOnClickListener {
//                viewModel.download()
//                textViewMoneyRub.text = gameValue.moneyRub?.toString()
//                Log.d("TESTButton", viewModel.allGameValue().value.toString())
//            }
//        })

//    suspend fun initView() {
//        coroutineScope {
//            launch {
//
//            }
//        }
//    }
//
//        val moneyObserver =  Observer<List<GameValue>> {newMoney->
//            textViewMoneyRub.text = newMoney.map { it.moneyRub }.toString()
//            Log.d("TESTMoneyObserver", newMoney.toString())
//        }
//        viewModel.allGameValue().observe(this, moneyObserver)
//        buttonProgram.setOnClickListener {
//            viewModel.download()
//            Log.d("TESTButton", viewModel.allGameValue().value.toString())
//        }
//        db.gameDao().getAllValue().observe(this, {
//
//            Log.d("TEST ", it.toString())
//        }
//        )

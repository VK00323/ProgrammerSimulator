package com.example.programmergame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.programmergame.databinding.FragmentHomeBinding
import com.parse.ParseObject
import java.io.Serializable

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ladButton.setOnClickListener {
//            saveNewPlayer()
        }

        binding.startButton.setOnClickListener {
            //Todo test data
//            val bundle = Bundle().apply { putSerializable("navigation data", TestParams()) }
            val actions = HomeFragmentDirections.actionFirstFragmentToSecondFragment(TestParams())
            findNavController().navigate(
                actions
            )
        }
    }

    private fun saveNewPlayer() {
        val  soccerPlayer = ParseObject("SoccerPlayer");
        soccerPlayer.put("playerName", "A. Wed");
        soccerPlayer.put("yearOfBirth", 1997);
        soccerPlayer.put("emailContact", "a.wed@email.io");
        soccerPlayer.put("attributes", listOf("fast", "good conditioning"));
        soccerPlayer.saveInBackground();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//Todo test data
data class TestParams(
    val first: String = "String",
    val second: Int = 10,

    ) : Serializable

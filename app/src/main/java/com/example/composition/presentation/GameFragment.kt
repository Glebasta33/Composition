package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.*
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException

class  GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var level: Level

    private val viewModelFactory by lazy {
        GameViewModelFactory(level, requireActivity().application)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptionsArray by lazy {
        with(binding){
            listOf(tvOption1, tvOption2, tvOption3, tvOption4, tvOption5, tvOption6)
        }
    }

    private lateinit var showCongratulations: ShowCongratulations

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ShowCongratulations) {
            showCongratulations = context
        } else {
            throw RuntimeException("$context isn`t implement ShowCongratulations")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setOptionsListeners()

    }

    private fun observeViewModel() {
        with(binding) {
            viewModel.questions.observe(viewLifecycleOwner) {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                for ((i, tv) in tvOptionsArray.withIndex()) {
                    tv.text = it.options[i].toString()
                }
            }

            viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
                progressBar.setProgress(it, true)
            }

            viewModel.enoughCount.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                tvAnswersProgress.setTextColor(color)
            }

            viewModel.enoughPercent.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                progressBar.progressTintList = ColorStateList.valueOf(color)
            }

            viewModel.minPercent.observe(viewLifecycleOwner) {
                progressBar.secondaryProgress = it
            }

            viewModel.progressAnswers.observe(viewLifecycleOwner) {
                tvAnswersProgress.text = it
            }

            viewModel.formattedTime.observe(viewLifecycleOwner) {
                tvTimer.text = it
            }

            viewModel.gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }
        }
    }

    private fun getColorByState(isEnough: Boolean): Int {
        val colorResId = if (isEnough) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun setOptionsListeners() {
        for (tv in tvOptionsArray) {
            tv.setOnClickListener {
                val numberInAnswer = tv.text.toString().toInt()
                viewModel.chooseAnswer(numberInAnswer)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        if (gameResult.winner) {
            showCongratulations.showWin()
        } else {
            showCongratulations.showLose()
        }
        val args = Bundle().apply {
            putParcelable(GameFinishedFragment.KEY_GAME_RESULT, gameResult)
        }
        findNavController().navigate(R.id.action_gameFragment_to_gameFinishedFragment, args)
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
//            .addToBackStack(null)
//            .commit()
    }

    interface ShowCongratulations {
        fun showWin()
        fun showLose()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let { level = it }
    }

    companion object {

        const val NAME = "GameFragment"
        const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }

    }

}
package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")


    private val args by navArgs<GameFinishedFragmentArgs>()
    private lateinit var gameResult: GameResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameResult = args.gameResult
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        setupButtonClickListener()
    }

    private fun bindViews() {
        val requiredAnswers = gameResult.gameSettings.minCountOfRightAnswers
        val scoreAnswers = gameResult.countOfRightAnswers
        val requiredPercentage = gameResult.gameSettings.minPercentOfRightAnswers
        val scorePercentage = calculatePercentOfRightAnswers()

        with(binding) {
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                requiredAnswers
            )
            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                scoreAnswers
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                requiredPercentage
            )
            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                scorePercentage
            )
            emojiResult.setImageResource(getSmileResId())
        }
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.smile
        } else {
            R.drawable.sad_smile
        }
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return if (gameResult.countOfQuestions == 0) {
            0
        } else {
            ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun setupButtonClickListener() {
        binding.buttonRetry.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_GAME_RESULT = "GameResult"
    }

}
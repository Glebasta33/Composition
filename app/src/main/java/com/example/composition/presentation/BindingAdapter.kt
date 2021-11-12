package com.example.composition.presentation

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        count
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percent: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        percent
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        getScorePercentage(gameResult)
    )
}

private fun getScorePercentage(gameResult: GameResult): Int {
    return if (gameResult.countOfQuestions == 0) {
        0
    } else {
        ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("imageSmileResult")
fun bingSmileResult(imageView: ImageView, gameResult: GameResult) {
    imageView.setImageResource(getSmileResId(gameResult))
}

private fun getSmileResId(gameResult: GameResult): Int {
    return if (gameResult.winner) {
        R.drawable.smile
    } else {
        R.drawable.sad_smile
    }
}

@BindingAdapter("percentOfProgress")
fun setProgressParameters(progressBar: ProgressBar, percentOfProgress: Int) {
    progressBar.setProgress(percentOfProgress, true)
}

@BindingAdapter("progressTint")
fun setProgressTint(progressBar: ProgressBar, isEnough: Boolean) {
    val color = getColorByState(progressBar, isEnough)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("textProgressTint")
fun setTextProgressTint(tvAnswersProgress: TextView, isEnough: Boolean) {
    val color = getColorByState(tvAnswersProgress, isEnough)
    tvAnswersProgress.setTextColor(color)
}

private fun getColorByState(view: View, isEnough: Boolean): Int {
    val colorResId = if (isEnough) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(view.context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClick")
fun bindOnOptionClickListener(textView: TextView, optionClickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        optionClickListener.onOptionClick(textView.text.toString().toInt())
    }
}
package sk.fri.uniza.photowalk.Map

import android.os.CountDownTimer
import java.util.Timer

class Timer(onFinishListener: OnFinishListener) {
    private val countDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            onFinishListener.onTimerFinish()
        }
    }

    fun startTimer() {
        countDownTimer.start()
    }

    fun stopTimer() {
        countDownTimer.cancel()
    }

    interface OnFinishListener{
        fun onTimerFinish()
    }
}
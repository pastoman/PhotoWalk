package sk.fri.uniza.photowalk.Map

import android.os.CountDownTimer
import java.util.Timer

/**
 * Trieda, ktora predstavuje minutovy odpocet a sluzi na aktualizaciu mapy
 *
 * @constructor
 * vytvori instanciu CountDownTimeru a inicializuje jeho nmetody
 *
 * @param onFinishListener listener, ktory zavola metodu onTimerFinish() po uplynuti casu
 */
class Timer(onFinishListener: OnFinishListener) {
    private val countDownTimer = object : CountDownTimer(60000, 1000) {
        /**
         * metoda nie je pouzivana
         *
         * @param millisUntilFinished cas do konca casovaca
         */
        override fun onTick(millisUntilFinished: Long) {
        }

        /**
         * metoda sa zavola na konci  casovaca
         *
         */
        override fun onFinish() {
            onFinishListener.onTimerFinish()
        }
    }

    /**
     * spustenie casovaca
     *
     */
    fun startTimer() {
        countDownTimer.start()
    }

    /**
     * zastavenie casovaca
     *
     */
    fun stopTimer() {
        countDownTimer.cancel()
    }

    /**
     * interface, ktory sluzi ako listener na metodu onTimerFinish()
     *
     */
    interface OnFinishListener{
        fun onTimerFinish()
    }
}
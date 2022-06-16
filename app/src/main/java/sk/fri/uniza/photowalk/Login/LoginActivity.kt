package sk.fri.uniza.photowalk.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.ActivityLoginBinding

/**
 * Aktivita sluzi na prihlasovanie a registrovanie pouzivatelov
 *
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    /**
     * metoda sa zavola pri vytvoreni aktivity, sluzi na inicializaciu tried potrebnych na
     * fungovanie aktivity
     *
     * @param savedInstanceState ulozeny predchadzajuci stav pri behu aplikacie
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()

    }


}
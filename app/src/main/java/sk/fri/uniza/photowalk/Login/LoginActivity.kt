package sk.fri.uniza.photowalk.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,
            R.layout.activity_login
        )
        supportActionBar?.hide()
        constraintLayout = binding.constraintLayout

    }


}
package sk.fri.uniza.photowalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        supportActionBar?.hide()
        constraintLayout = binding.constraintLayout

    }


}
package sk.fri.uniza.photowalk

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import sk.fri.uniza.photowalk.databinding.LoginFragmentBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val myIntent = Intent(it.context, MapsActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContentView(R.layout.login_fragment)
    }

}
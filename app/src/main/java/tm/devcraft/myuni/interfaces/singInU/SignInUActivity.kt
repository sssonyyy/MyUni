package tm.devcraft.myuni.interfaces.singInU

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in_uactivity.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.selectAction.SelectActionAuthActivity
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity


class SignInUActivity : AppCompatActivity(R.layout.activity_sign_in_uactivity) {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListeners()

        auth = FirebaseAuth.getInstance()
        //auth.signOut()

    }
    private fun signInUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                println(task.result.toString())
                val intent = Intent(this, SelectBottomNavActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                println(task.exception?.message)
                Toast.makeText(this, "Ошибка входа.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initViews() {
        hintDisable()
    }

    private fun initListeners() {
        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(applicationContext, R.anim.other_btn)
            it.startAnimation(animScale)
            startActivity(Intent(this, SelectActionAuthActivity::class.java))
            finish()
        }
        btn_remember_pass.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            startActivity(Intent(this, RestorePassActivity::class.java))
            finish()
        }
        /*btn_signin.setOnClickListener {
            startActivity(Intent(this, SelectBottomNavActivity::class.java))
        }*/

        btn_signin.setOnClickListener {
            if (input_login.text.toString().trim().isNotEmpty() && enter_password.text.toString().trim().isNotEmpty()){
                println(input_login.text?.trim().toString())
                println(enter_password.text?.trim().toString())
                signInUser(input_login.text.toString().trim(), enter_password.text.toString().trim())
                //finish()
            }else{
                Toast.makeText(this, "Введите данные.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SelectActionAuthActivity::class.java))
        finish()
    }

    private fun hintDisable() {
        input_login_cont.isHintEnabled = false
        input_pass_cont.isHintEnabled = false
    }
}
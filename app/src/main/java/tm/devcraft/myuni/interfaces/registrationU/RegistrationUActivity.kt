package tm.devcraft.myuni.interfaces.registrationU

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_registration_uactivity.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.RegUserModel

class RegistrationUActivity : AppCompatActivity(R.layout.activity_registration_uactivity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListeners()
        initViews()


        btn_signin.setOnClickListener {
            if (email_ed.text?.trim().toString().isNotEmpty() && password_ed.text?.trim().toString().isNotEmpty() ){
                 if ((email_ed.text.toString().contains( Regex("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                             "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) != true) &&( email_ed.text.toString().contains(Regex("^((\\+7|7|8)+([0-9]){10})\$"))!= true)){
                    Toast.makeText(this, "Введите корректный e - mail или номер телефона.", Toast.LENGTH_LONG).show()
                }

                else if(!ch_confirm_all.isChecked){
                    Toast.makeText(this, "Вы не подтвердили соглашение.", Toast.LENGTH_LONG).show()
                }
               else if (password_ed.text.toString().isDigitsOnly()){
                    Toast.makeText(this, "Пароль не должен содержать только цифры, пожалуйста, используйте дополнительные символы(A-Z ! * & )", Toast.LENGTH_LONG).show()
                }
                else if (password_ed.text.toString().length < 8){
                    Toast.makeText(this, "Пароль должен быть не меньше восьми символов.", Toast.LENGTH_LONG).show()
                    }
                else if (password_ed.text.toString() != ret_password_ed.text.toString()){
                    Toast.makeText(this, "Пароли не совпадают.", Toast.LENGTH_LONG).show()
                }else{
                    saveUserData(email_ed.text.toString().trim(), password_ed.text.toString().trim())
                }
            }else{
                Toast.makeText(this, "Введите данные.", Toast.LENGTH_LONG).show()
            }

        }

    }
    private fun saveUserData(email: String, password: String){
        Paper.book().write("userDataReg",RegUserModel(email, password, RegUserModel.NextStepRegModel()))
        val intent = Intent(this, NextStepRegActivity::class.java)
        startActivity(intent)
        //finish()
    }

    private fun initViews() {
        hintDisable()
    }

    private fun hintDisable() {
        input_login_cont.isHintEnabled = false
        input_pass_confirm_cont.isHintEnabled = false
        input_pass_cont.isHintEnabled = false
    }

    private fun initListeners() {
        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(applicationContext, R.anim.other_btn)
            it.startAnimation(animScale)
            //startActivity(Intent(this, SelectTypeAccActivity::class.java))
            finish()
        }
        btn_privacy_policy.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SelectTypeAccActivity::class.java))
    }
}


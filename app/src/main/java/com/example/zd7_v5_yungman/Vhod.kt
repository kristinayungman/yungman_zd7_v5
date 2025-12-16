package com.example.zd7_v5_yungman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Vhod.newInstance] factory method to
 * create an instance of this fragment.
 */
class Vhod : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vhod, container, false)

        val emailField = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val passwordField = view.findViewById<TextInputEditText>(R.id.editTextPassword)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupRole)

        buttonLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val selectedId = radioGroup.checkedRadioButtonId

            // Проверки
            if (email.isEmpty()) {
                emailField.error = "Введите email"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.error = "Некорректный email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordField.error = "Введите пароль"
                return@setOnClickListener
            }
            if (password.length < 6) {
                passwordField.error = "Пароль должен содержать не менее 6 символов"
                return@setOnClickListener
            }
            if (selectedId == -1) {
                Toast.makeText(context, "Выберите роль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRole = when (view.findViewById<RadioButton>(selectedId).id) {
                R.id.radioPassenger -> "passenger"
                R.id.radioDriver -> "driver"
                R.id.radioDispatcher -> "dispatcher"
                else -> null
            }

            // Переход на нужный фрагмент
            val nextFragment: Fragment = when (selectedRole) {
                "passenger" -> People.newInstance()
                "driver" -> VoditelFragment()
                "dispatcher" -> Dispetcher()
                else -> return@setOnClickListener
            }

            (activity as FragmentActivity?)?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, nextFragment)
                ?.addToBackStack(null) // чтобы можно было вернуться кнопкой "Назад"
                ?.commit()
        }

        return view
    }



    class VoditelFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = inflater.inflate(R.layout.fragment_voditel, container, false)
    }


}
package com.example.parkyapplication.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parkyapplication.R
import com.example.parkyapplication.databinding.ActivityLoginScreenBinding
import com.example.parkyapplication.utils.LocaleHelper
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import android.widget.Toast


class LoginScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    private var almaraiRegular: Typeface? = null
    private var almaraiBold: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()

        // 🟢 بعدين binding
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🟢 SharedPreferences
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        // 🟢 Load fonts
        almaraiRegular = ResourcesCompat.getFont(this, R.font.almarair)
        almaraiBold = ResourcesCompat.getFont(this, R.font.almaraib)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadSavedLanguage()
        onClickListener()
        setupLanguageButton()
    }

    private fun onClickListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.textInputLayoutEmail.editText?.text.toString().trim()
            val password = binding.textInputLayoutPassword.editText?.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please enter email and password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login is done ✅", Toast.LENGTH_SHORT).show()
                    } else {
                        val error = task.exception?.localizedMessage ?: "Login failed"
                        showErrorMessage(error)
                    }
                }
        }

    }

    private fun setupLanguageButton() {
        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showLanguageDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_language_selection, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val btnEnglish = dialogView.findViewById<Button>(R.id.btnEnglish)
        val btnArabic = dialogView.findViewById<Button>(R.id.btnArabic)

        btnEnglish.setOnClickListener {
            changeLanguage("en")
            alertDialog.dismiss()
        }

        btnArabic.setOnClickListener {
            changeLanguage("ar")
            alertDialog.dismiss()
        }
    }

    private fun changeLanguage(languageCode: String) {
        LocaleHelper.saveLanguage(this, languageCode)
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun loadSavedLanguage() {
        val savedLanguage = LocaleHelper.getSavedLanguage(this)
        updateLanguageButtonText(savedLanguage)

        if (savedLanguage == "ar") {
            applyArabicFont()
        }
    }

    private fun applyArabicFont() {
        val regular = almaraiRegular ?: return
        val bold = almaraiBold ?: return

        binding.tvLoginTitle.typeface = bold
        binding.tvLoginSubtitle.typeface = regular
        binding.tvForgotPassword.typeface = regular
        binding.btnLogin.typeface = bold
        binding.btnLanguage.typeface = regular
        binding.tvAlreadyHaveAccount.typeface = regular
        binding.tvSignUp.typeface = bold

        binding.btnLogin.textSize = 14f
        binding.tvAlreadyHaveAccount.textSize = 14f
        binding.tvSignUp.textSize = 14f
        binding.tvLoginSubtitle.textSize = 16f
    }

    private fun applyFontToAllViews(view: View, typeface: Typeface) {
        when (view) {
            is TextView -> view.typeface = typeface
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    applyFontToAllViews(view.getChildAt(i), typeface)
                }
            }
        }
    }

    private fun updateLanguageButtonText(languageCode: String) {
        binding.btnLanguage.text = when (languageCode) {
            "ar" -> "العربية"
            "en" -> "English"
            else -> "English"
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun getCurrentLanguage(): String {
        return resources.configuration.locales[0].language
    }

    private fun showErrorMessage(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showSuccessMessage(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.success))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun attachBaseContext(newBase: Context?) {
        val updatedContext = newBase?.let {
            LocaleHelper.setLocale(it, LocaleHelper.getSavedLanguage(it))
        }
        super.attachBaseContext(updatedContext ?: newBase!!)
    }
}

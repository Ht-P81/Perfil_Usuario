package com.example.perfil_usuario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setings)

        //Flecha de retroceso, si es diferente de null
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.settings_title)
        }

        supportFragmentManager.beginTransaction().
        replace(R.id.container_main, SettingsFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
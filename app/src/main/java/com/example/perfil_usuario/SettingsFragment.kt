package com.example.perfil_usuario

import android.os.Bundle
import androidx.core.content.edit
import androidx.preference.*

//Hereda y sobreescribe metodos, a PreferenceFragmentCOmpat se le añade ()
/*Adicionalmente tenemos que cambiar aquí la herencia porque requerimos también tu constructor vacío.
Este fragmento básicamente funciona como una actividad en donde se le va a vincular una vista, pero
esto únicamente puede pertenecer a otra actividad.
Es decir, no puede existir por sí solo, sino que tiene que alojarse en algunas de las actividades
que tengamos, en este caso Settings Activity.*/
class SettingsFragment : PreferenceFragmentCompat() {

    //Sobreescribimos algunos métodos
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //Configurando la aplicación settings
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val deleteUserDataPreference = findPreference<Preference>(getString(R.string.preferences_dey_delete_data))
        deleteUserDataPreference?.setOnPreferenceClickListener {
            val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
            sharedPreference.edit {
                // Datos que vamos a guardar en el sharedPreferences
                putString(getString(R.string.key_foto), null)
                putString(getString(R.string.key_nombre), null)
                putString(getString(R.string.key_email), null)
                putString(getString(R.string.key_website), null)
                putString(getString(R.string.key_telefono), null)
                putString(getString(R.string.key_twitter), null)
                putString(getString(R.string.key_latitud), null)
                putString(getString(R.string.key_longitud), null)
                //Guardamos los cambios con el metodo apply()
                apply()
            }
            true
        }

        val switchPreferenceCompat = findPreference<SwitchPreferenceCompat>(getString(R.string.preferences_key_enable_clicks))
        val listPreference = findPreference<ListPreference>(getString(R.string.preferences_key_ui_img_size))

        val restoreAllPreference = findPreference<Preference>(getString(R.string.preferences_dey_restore_data))
        restoreAllPreference?.setOnPreferenceClickListener {
            val preferenciascompartidas = PreferenceManager.getDefaultSharedPreferences(context)
            preferenciascompartidas.edit().clear().apply()
            //Reseteamos si pulsamos el botón de switchPreferenceCompat
            switchPreferenceCompat?.isChecked = true
            listPreference?.value = getString(R.string.preferences_key_img_size_large)
            true
        }

        val deleteconfiguration = findPreference<Preference>(getString(R.string.preferences_key_delete_configuration))
        deleteconfiguration?.setOnPreferenceClickListener {
            switchPreferenceCompat?.isChecked = false
            listPreference?.value = getString(R.string.preferences_key_img_size_large)
            true
        }

    }
}
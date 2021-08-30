package com.example.perfil_usuario

import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.updateLayoutParams
import androidx.preference.PreferenceManager
import com.example.perfil_usuario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Declaración de variable global
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imgUri: Uri
    private var latitud:Double = 0.0
    private var longitud:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Declarando esto tenemos acceso a las vistas de forma binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializamos la variable sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //Función que actualiza la interfaz del usuario
        //updateUI()
        //Invocamos al metodo que obtiene los datos guardados del sharedPreferences saveData()
        getUserData()

        //Metodo con las funciones de los diferentes textView de nuestra app
        setupIntents()

    } //Cierre del onCreate

    override fun onResume() {
        super.onResume()
        refreshSettingsPreferences()
    }

    private fun refreshSettingsPreferences() {
        val marcado = sharedPreferences.getBoolean(getString(R.string.preferences_key_enable_clicks), true)
        with(binding){
            tvNombre.isEnabled = marcado
            tvEmail.isEnabled = marcado
            tvWebSite.isEnabled = marcado
            tvTelefono.isEnabled = marcado
            tvTwitter.isEnabled = marcado
            tvUbicacion.isEnabled = marcado
            tvSettings.isEnabled = marcado
        }

        val imgSize = sharedPreferences.getString(getString(R.string.preferences_key_ui_img_size), "")
        val sizeValue = when (imgSize){
            getString(R.string.preferences_key_img_size_small) ->{
                resources.getDimensionPixelSize(R.dimen.pequeño_imagen_perfil)
            }
            getString(R.string.preferences_key_img_size_medium) ->{
                resources.getDimensionPixelSize(R.dimen.medio_imagen_perfil)
            }
            else ->{
                resources.getDimensionPixelSize(R.dimen.largo_imagen_perfil)
            }
        }
        binding.imagenPerfil.updateLayoutParams {
            width = sizeValue
            height = sizeValue
        }

        getUserData()
    }

    //Este metodo crea una busqueda web a partir de un campo TextView
    private fun setupIntents() {
        binding.tvNombre.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, binding.tvNombre.text)
            }
            launchIntent(intent)
        }

        binding.tvTwitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, binding.tvTwitter.text)
            }
            launchIntent(intent)
        }

        binding.tvEmail.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                    // con la action_sendto puede aplicarse para multiples sentidos le decimos
                    // que es para envíar un mail
                data = Uri.parse("mailto:")
                //Configuramos los argumentos que vamos a enviar
                // Primero configuramos los destinatarios
                putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.tvEmail.text.toString()))
                putExtra(Intent.EXTRA_SUBJECT, "Email de la aplicación de perfil")
                putExtra(Intent.EXTRA_TEXT, "Hola, email envíado desde la app")
            }
            launchIntent(intent)
        }

        //Hemos puesto en activity_main.xml a true que es lo mismo que hacer este onclick android:linksClickable="true"
        /*    binding.tvWebSite.setOnClickListener {
            val pagina = Uri.parse(binding.tvWebSite.text.toString())
            val intent = Intent(Intent.ACTION_VIEW, pagina)
            launchIntent(intent)
        }*/

        binding.tvTelefono.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL).apply {
                val phone = (it as TextView).text
                // Importantísimo uriString debe llamarse tel:, si pone otra cosa da fallo
                data = Uri.parse("tel: $phone")
            }
            launchIntent(intent)

        }

        binding.tvUbicacion.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=$latitud, $longitud (Plazuela 81)")
                `package` = "com.google.android.apps.maps"
            }
            launchIntent(intent)
        }

        binding.tvSettings.setOnClickListener {
            // nos abre las opciones de configuración de la ubicación en el dispositivo
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            launchIntent(intent)
        }

    } //llave de cierre de setupIntents()

    // metodo que pregunta el intent se lanze si
    private fun launchIntent(intent: Intent){
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }else{
            Toast.makeText(this, getString(R.string.profile_error_no_resolve), Toast.LENGTH_SHORT).show()
        }

    }

    // metodo getUserData, extraemos los valores almacenados en el sharedPreferences, metodo saveUserData()
    private fun getUserData(){
        imgUri = Uri.parse(sharedPreferences.getString(getString(R.string.key_foto), ""))
        val name = sharedPreferences.getString(getString(R.string.key_nombre), null)
        val email = sharedPreferences.getString(getString(R.string.key_email), null)
        val website = sharedPreferences.getString(getString(R.string.key_website), null)
        val telefono = sharedPreferences.getString(getString(R.string.key_telefono), null)
        val twitter = sharedPreferences.getString(getString(R.string.key_twitter), null)
        latitud = sharedPreferences.getString(getString(R.string.key_latitud), "0.0")!!.toDouble()
        longitud = sharedPreferences.getString(getString(R.string.key_longitud), "0.0")!!.toDouble()
        // Una vez obtenidos los datos guardados desde saveUserData, actualizamos los datos con UpdateUI
        updateUI(name,email,website,telefono,twitter)
    }

    //Creamos la función que actualiza la interfaz de usuario
    private fun updateUI(name: String?, email: String?, website: String?, telefono: String?,
                         twitter: String?) {
        with(binding){
            //Con el operador Elvis ?: le decimos que si está vacío ponga estos valores
            imagenPerfil.setImageURI(imgUri)
            tvNombre.text = name ?: "Hierutime"
            tvEmail.text = email ?: "hierutime@plazuela81.com"
            tvWebSite.text = website ?: "www.plazuela81.com"
            tvTelefono.text = telefono ?: "0034 660 55 23 43"
            tvTwitter.text = twitter ?: "https://twitter.com/Hierutime"
            //Ubicación GPS de la plazuela
            latitud = 36.6780
            longitud = -6.1310
        }

    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Evento que genera el pulsar el menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //
        when(item.itemId){
            //El primer caso agregado de opciones que va verse
            R.id.action_edit ->{
                //Creamos una variable intent, con los que mandar los datos desde la main al edit_ativity
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra(getString(R.string.key_foto), imgUri.toString())
                intent.putExtra(getString(R.string.key_nombre), binding.tvNombre.text)
                intent.putExtra(getString(R.string.key_email), binding.tvEmail.text.toString())
                intent.putExtra(getString(R.string.key_website), binding.tvWebSite.text.toString())
                intent.putExtra(getString(R.string.key_telefono), binding.tvTelefono.text)
                intent.putExtra(getString(R.string.key_twitter), binding.tvTwitter.text)
                intent.putExtra(getString(R.string.key_latitud), latitud)
                intent.putExtra(getString(R.string.key_longitud), longitud)
                //Pasamos datos
                //Lanza que desde la activity main vaya a EditActivity
                //startActivity(intent)
                startActivityForResult(intent, RC_EDIT) // <-- Lanzamiento y espera de respuesta
            }
            // El segundo caso que va a suceder en el menu
            R.id.action_settings ->{
                val intent = Intent(this,SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //Recibe los datos editados desde editActivity, aquí podemos empezar a guardar los datos (sharedPreferences)
    // antes de actualizar la interfaz de usuario, ya están guardados ahora solo tenemos que recuperarlos
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == RC_EDIT){
                imgUri = Uri.parse(data?.getStringExtra(getString(R.string.key_foto)))
                val name = data?.getStringExtra(getString(R.string.key_nombre))
                val email = data?.getStringExtra(getString(R.string.key_email))
                val website = data?.getStringExtra(getString(R.string.key_website))
                val telefono = data?.getStringExtra(getString(R.string.key_telefono))
                val twitter = data?.getStringExtra(getString(R.string.key_twitter))
                latitud = data?.getDoubleExtra(getString(R.string.key_latitud), 0.0) ?: 0.0
                longitud = data?.getDoubleExtra(getString(R.string.key_longitud), 0.0) ?:0.0

                //Refrescar la interfaz de usuario
                //updateUI(name!!, email!!, website!!, telefono!!, twitter!!)

                // Guardamos datos de usuario
                saveUserData(name, email, website, telefono,twitter)

            }
        }
    }

    // Metodo que guarda los datos de usuario
    private fun saveUserData(name: String?, email: String?, website: String?, telefono: String?, twitter: String?) {
        sharedPreferences.edit {
            // Datos que vamos a guardar en el sharedPreferences
            putString(getString(R.string.key_foto), imgUri.toString())
            putString(getString(R.string.key_nombre), name)
            putString(getString(R.string.key_email), email)
            putString(getString(R.string.key_website), website)
            putString(getString(R.string.key_telefono), telefono)
            putString(getString(R.string.key_twitter), twitter)
            putString(getString(R.string.key_latitud), latitud.toString())
            putString(getString(R.string.key_longitud), longitud.toString())
            //Guardamos los cambios con el metodo apply()
            apply()
        }
        // Una vez almacenados los datos pasamos a actualizarlos
        updateUI(name, email, website, telefono, twitter)
    }

    companion object{
        private const val RC_EDIT = 81
    }

}
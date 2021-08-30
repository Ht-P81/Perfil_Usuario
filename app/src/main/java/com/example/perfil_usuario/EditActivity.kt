package com.example.perfil_usuario

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.perfil_usuario.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private var imgUri: Uri? = null
    private lateinit var binding:ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Flecha de retroceso en la parte superior
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Para saber si esta en horizontal o en vertical
        val context = applicationContext
        var orientation: String
        if(resources.getBoolean(R.bool.isLand)){
            orientation = "Está vertical"
            Toast.makeText(context, orientation, Toast.LENGTH_SHORT).show()
        }else{
            orientation = "Está horizontal"
            Toast.makeText(context, orientation, Toast.LENGTH_SHORT).show()
        }


        with(binding){
            intent.extras?.let{
                imgUri = Uri.parse(it.getString(getString(R.string.key_foto)))
                actualizar_imagen()
                etNombre.setText(it.getString(getString(R.string.key_nombre)))
                etMail.setText(it.getString(getString(R.string.key_email)))
                etWebside.setText(it.getString(getString(R.string.key_website)))
                etTelefono.setText(it.getString(getString(R.string.key_telefono)))
                etTwitter.setText(it.getString(getString(R.string.key_twitter)))
                etLatitud.setText(it.getDouble(getString(R.string.key_latitud)).toString())
                etLongitud.setText(it.getDouble(getString(R.string.key_longitud)).toString())
            }

            etNombre.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText nombre, el cursor se ponga al final
                if(isFocused){ etNombre.text?.let{ etNombre.setSelection(it.length) } }
            }

            etMail.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText webside, el cursor se ponga al final
                if(isFocused){ etMail.text?.let{ etMail.setSelection(it.length) } }
            }

            etWebside.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText webside, el cursor se ponga al final
                if(isFocused){ etWebside.text?.let{ etWebside.setSelection(it.length) } }
            }

            etTelefono.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText webside, el cursor se ponga al final
                if(isFocused){ etTelefono.text?.let{ etTelefono.setSelection(it.length) } }
            }

            etTwitter.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText webside, el cursor se ponga al final
                if(isFocused){ etTwitter.text?.let{ etTwitter.setSelection(it.length) } }
            }

            etLatitud.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText webside, el cursor se ponga al final
                if(isFocused){ etLatitud.text?.let{ etLatitud.setSelection(it.length) } }
            }

            etLongitud.setOnFocusChangeListener { view, isFocused ->
                //Aqui le decimos que, si está en el editText webside, el cursor se ponga al final
                if(isFocused){ etLongitud.text?.let{ etLongitud.setSelection(it.length) } }
            }

            // on click cuando le damos sobre la imagen hacemos que abra el documento
            btnSelectPhoto.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    //Añadimos categoría
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/jpeg" //tipo de imagen de foto
                }
                startActivityForResult(intent, RC_GALLERY)
            }
        }


        //Estamos validando constantemente .extras? para que no esté vacío se puede optimizar con
        //la función de alcance let
        // código antiguo antes del let
        /*binding.etNombre.setText(intent.extras?.getString(getString(R.string.key_nombre)))
        binding.etMail.setText(intent.extras?.getString(getString(R.string.key_email)))
        binding.etWebside.setText(intent.extras?.getString(getString(R.string.key_website)))
        binding.etFacebook.setText(intent.extras?.getString(getString(R.string.key_facebook)))
        binding.etTwitter.setText(intent.extras?.getString(getString(R.string.key_twitter)))
        binding.etLatitud.setText(intent.extras?.getDouble(getString(R.string.key_latitud)).toString())
        binding.etLongitud.setText(intent.extras?.getDouble(getString(R.string.key_longitud)).toString())*/

    } // llave de cierre del onCreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //esto es lo mismo que el if, pero optimizado
        when(item.itemId){
            android.R.id.home -> onBackPressed()
            R.id.action_save -> enviarDatos()
        }

       /* if(item.itemId == R.id.action_save){
            //finish()
            enviarDatos()
            //Lanza que desde la activity main vaya a EditActivity
            //startActivity(Intent(this, MainActivity::class.java) )
        } else if (item.itemId == android.R.id.home){
        //Con el metodo finish si funcionaría pero el onBackPressed es mas específico
        //finish()
            onBackPressed()
        }
        */

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if(resources.getBoolean(R.bool.isLand)){
            Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == RC_GALLERY){
                imgUri = data?.data


                imgUri?.let{
                    //Estos permisos son para que al cambiar de foto mantenga la seleccionada
                    val contentResolver = applicationContext.contentResolver
                    val takeFlags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(it, takeFlags)

                    actualizar_imagen()
                }
            }
        }
    }

    //Metodo
    private fun actualizar_imagen(){
        binding.imagenPerfil.setImageURI(imgUri)
    }
    //Metodo con el que enviar datos
    private fun enviarDatos(){
        //Enviar los datos nuevos rellenos por el usuario
        val intent = Intent()

        // Video 171 mejores prácticas Scope
        with(binding){
            intent.apply {
                putExtra(getString(R.string.key_foto), imgUri.toString())
                putExtra(getString(R.string.key_nombre), etNombre.text.toString())
                putExtra(getString(R.string.key_email), etMail.text.toString())
                putExtra(getString(R.string.key_website), etWebside.text.toString())
                putExtra(getString(R.string.key_telefono), etTelefono.text.toString())
                putExtra(getString(R.string.key_twitter), etTwitter.text.toString())
                putExtra(getString(R.string.key_latitud), etLatitud.text.toString().toDouble())
                putExtra(getString(R.string.key_longitud), etLongitud.text.toString().toDouble())

            }
        }

        /*intent.putExtra(getString(R.string.key_nombre), binding.etNombre.text.toString())
        intent.putExtra(getString(R.string.key_email), binding.etMail.text.toString())
        intent.putExtra(getString(R.string.key_website), binding.etWebside.text.toString())
        intent.putExtra(getString(R.string.key_facebook), binding.etFacebook.text.toString())
        intent.putExtra(getString(R.string.key_twitter), binding.etTwitter.text.toString())
        intent.putExtra(getString(R.string.key_latitud), binding.etLatitud.text.toString().toDouble())
        intent.putExtra(getString(R.string.key_longitud), binding.etLongitud.text.toString().toDouble())*/

        setResult(RESULT_OK, intent)
        finish()
    }

    companion object{
        private const val RC_GALLERY = 22
    }
}

/*//Esto se me autocreó pq dabe error ActivityEditBinding...
class ActivityEditBinding {

}*/

package mx.edu.ittepic.ladm_u1_practica3

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var vector = arrayOf(0,0,0,0,0,0,0,0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        asignar.setOnClickListener {
            asignarValores()
        }
        mostrar.setOnClickListener {
            mostrarValores()
        }

        guardarSD.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 0
                ) //0 nO VOY A SOLICITAR EL PERMISO DE SE PUDO O NO
            } else {
                mensaje("PERMISOS YA OTORGADOS")
                guardarArchivoSD()
            }
        }

        leerSD.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 0
                ) //0 nO VOY A SOLICITAR EL PERMISO DE SE PUDO O NO
            } else {
                mensaje("PERMISOS YA OTORGADOS")
                leerArchivoSD()
            }
            var vectorAux = resultado.text.toString()
            var variableAux = vectorAux.split(",")
            (0..9).forEach {
                vector[it] = variableAux[it].toInt()

            }
        }
    }

    fun asignarValores(){
        if (editValor.text.isEmpty() || editPosicion.text.isEmpty()){
            mensaje("ERROR, TIENE EL CAMPO VACIO, VERIFIQUE")
            return
        }
            var dato = editValor.text.toString().toInt()

        vector[editPosicion.text.toString().toInt()] = dato
        limpiarCampos()
        mensaje("SE INSERTARON DATOS")
    }
    fun mostrarValores(){

        var total = vector.size-1
        var datos = ""

        (0..9).forEach {
            datos += vector[it].toString()+","
        }
        resultado.setText(datos.substring(0..datos.length-1))

    }
    fun guardarArchivoSD(){
        if (editText3.text.isEmpty()){
            mensaje("ERROR, TIENES ALGUNO DE LOS CAMPOS VACIOS, VERIFICA")
            return
        }
            if (noSD()){
                mensaje("NO HAY MEMORIA EXTERNA")
                return
            }
            try {

                var rutaSD = Environment.getExternalStorageDirectory()
                var  datosAchivo = File(rutaSD.absolutePath,"archivos.txt")

                var flujoSalida=OutputStreamWriter(FileOutputStream(datosAchivo))
                var data=editText3.text.toString()+"&"+
                        editText4.text.toString()+"&"+
                        resultado.text.toString()

                flujoSalida.write(data)
                flujoSalida.flush()//Subirlo
                flujoSalida.close()

                mensaje("Exito! El archivo se guardo correctamente")
                ponerTexto("", "", "")
                limpiarCamposD()

                var vectorAux = vector
            }catch (error:IOException){
                mensaje(error.message.toString())
            }
    }

    fun leerArchivoSD(){
            if (noSD()) {
                mensaje("NO HAY MEMORIA EXTERNA")
                return
            }
            try {
                var rutaSD = Environment.getExternalStorageDirectory()
                var datosArchivo = File(rutaSD.absolutePath, "archivos.txt")

                var flujoEntrada = BufferedReader(
                    InputStreamReader(FileInputStream(datosArchivo))
                )
                var data = flujoEntrada.readLine() //Contenido del archivo
                var vector = data.split("&")

                ponerTexto(vector[0], vector[1], vector[2])
                flujoEntrada.close()

            } catch (error: IOException) {
                mensaje(error.message.toString())
            }
    }

    fun noSD() : Boolean{
        var  estado = Environment.getExternalStorageState()
        if(estado!=Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    fun mensaje(m:String){
        AlertDialog.Builder(this)
        .setTitle("ATENCIÓN")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }

    fun ponerTexto(t1:String, t2:String, t3:String){
        editText3.setText(t1)
        editText4.setText(t2)
        resultado.setText(t3)
    }

    fun limpiarCampos(){
        editValor.setText("")
        editPosicion.setText("")
    }
    fun limpiarCamposD(){
        resultado.setText("")
    }

}

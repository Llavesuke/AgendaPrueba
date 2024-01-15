
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

/**
 * Clase que representa un contacto con nombre, apellido, email y lista de teléfonos.
 *
 * @property nombre El nombre del contacto.
 * @property apellido El apellido del contacto.
 * @property email El email del contacto.
 * @property telefonos La lista de teléfonos del contacto.
 */
class Contacto(
    private var nombre: String,
    private var apellido: String,
    private var email: String,
    private var telefonos: MutableList<String>
) {

    /**
     * Obtiene el nombre del contacto.
     *
     * @return El nombre del contacto.
     */
    fun getNombre(): String {
        return nombre
    }

    /**
     * Obtiene el apellido del contacto.
     *
     * @return El apellido del contacto.
     */
    fun getApellido(): String {
        return apellido
    }

    /**
     * Obtiene el email del contacto.
     *
     * @return El email del contacto.
     */
    fun getEmail(): String {
        return email
    }

    /**
     * Obtiene la lista de teléfonos del contacto.
     *
     * @return La lista de teléfonos del contacto.
     */
    fun getTelefonos(): MutableList<String> {
        return telefonos
    }

    /**
     * Cambia el nombre del contacto.
     *
     * @param newName El nuevo nombre del contacto.
     */
    fun changeName(newName: String) {
        nombre = newName
    }

    /**
     * Cambia el apellido del contacto.
     *
     * @param newApellido El nuevo apellido del contacto.
     */
    fun changeApellido(newApellido: String) {
        apellido = newApellido
    }

    /**
     * Cambia el email del contacto.
     *
     * @param newEmail El nuevo email del contacto.
     */
    fun changeEmail(newEmail: String) {
        email = newEmail
    }

    /**
     * Cambia la lista de teléfonos del contacto.
     *
     * @param newNumeros La nueva lista de teléfonos del contacto.
     */
    fun changeTelefonos(newNumeros: MutableList<String>) {
        telefonos = newNumeros
    }

    /**
     * Representación en cadena del contacto con formato específico.
     *
     * (override sustituye el metodo toString por lo que yo he escrito, a la hora de hacer un print, se utiliza el metodo toString,
     * asi que cada vez que use un print, se imprimira con este formato)
     *
     * @return La representación en cadena del contacto.
     */
    override fun toString(): String {
        return "\nNombre: $nombre $apellido ($email)\nTeléfonos: ${
            telefonos
                .map { telefono ->
                    when {
                        telefono.length == 9 -> telefono
                        telefono.length == 12 -> "${telefono.substring(0, 3)}-${telefono.substring(3, 12)}"
                        else -> ""
                    }
                }
                .filter { it.isNotEmpty() }
                .joinToString(" / ")
        }"
    }
}

/**
 * Clase que se encarga de cargar contactos desde un archivo CSV.
 */
class Reader {

    /**
     * Carga los contactos desde un archivo CSV y los devuelve como una lista mutable.
     *
     * @param path La ruta del archivo CSV.
     * @return Una lista mutable de objetos Contacto.
     */
    fun cargarContactos(path: String): MutableList<Contacto> {
        val contacts: MutableList<Contacto> = mutableListOf()
        csvReader().open(path) {
            readAllAsSequence().forEach { row ->
                for (element in row) {
                    val persona = element.split(";")
                    val contacto = Contacto(persona[0], persona[1], persona[2], persona.subList(3, persona.size).toMutableList())
                    contacts.add(contacto)
                }
            }
        }
        return contacts
    }
}

/**
 * Clase que representa una agenda de contactos.
 *
 * Esta clase permite gestionar una lista de contactos, incluyendo operaciones como
 * agregar, modificar, eliminar, buscar y mostrar contactos.
 *
 * @property contactos Lista mutable de [Contacto] que contiene los contactos en la agenda.
 * @property Input Objeto de la clase [IO] utilizado para la entrada de datos.
 */
class Agenda {
    private var contactos: MutableList<Contacto> = mutableListOf()
    private val Input = IO()

    /**
     * Agrega un nuevo contacto a la agenda.
     */
    fun agregarContacto() {
        var finalVerify = false
        do {
            val nombreRaw = Input.enterText("Ingrese el nombre del contacto: ")
            val nombre = nombreRaw!![0].uppercase() + nombreRaw.substring(1)
            val apellidoRaw = Input.enterText("Ingrese el apellido del contacto: ")
            val apellido = apellidoRaw!![0].uppercase() + apellidoRaw.substring(1)
            val email = Input.enterText("Ingrese el email del contacto: ")
            if (contactos.any { it.getEmail() == email}) {
                println("Ya existe el email")
            }else {

                if (nombre == ""|| apellido == "" || (email == "" || "@" !in email!! )) {
                    println("Nombre, apellido y email son obligatorios. Por favor, inténtelo de nuevo.")
                } else {
                    print("Ingrese los teléfonos del contacto (separados por comas): ")
                    val telefonosInput = readLine()?.trim() ?: ""
                    val telefonos = telefonosInput.split(",").map { it.replace(" ","") }.toMutableList()

                    if (telefonos.all { it.length == 9 || (it.length == 12 && it.startsWith("+34")) }) {
                        val nuevoContacto = Contacto(nombre!!, apellido!!, email!!, telefonos)
                        contactos.add(nuevoContacto)

                        println("Contacto agregado correctamente: $nuevoContacto")

                        print("¿Desea agregar otro contacto? (Sí/No): ")
                        val respuesta = readLine()?.trim()?.lowercase()
                        if (respuesta == "no") finalVerify = true
                    } else println("Numeros introducidos no validos")
                }
            }
        } while (!finalVerify)
    }

    /**
     * Permite modificar los campos de un contacto existente en la agenda.
     */
    fun modificarContactos() {
        var changeEffected = false
        if (contactos.isEmpty()) {
            println("No hay contactos para modificar.")
            return
        }
        do {
            contactos.forEachIndexed { index, contacto ->
                println("${index + 1} -> ${contacto.getNombre()} ${contacto.getApellido()}")
            }
            val indexChoosed = Input.enterDigit("Introduzca el numero del usuario a modificar: ") - 1
            if (indexChoosed in 0..contactos.size - 1) {
                val contactoToModify = contactos[indexChoosed]

                println("Campo a modificar ->")
                println("1. Nombre")
                println("2. Apellido")
                println("3. Email")
                println("4. Telefonos")

                val choosedField = Input.enterDigit("Introduzca el numero del campo a modificar -> ") - 1
                if (choosedField in 0..3) {
                    when (choosedField) {
                        0 -> {
                            val newName = Input.enterText("Nuevo nombre del contacto -> ")
                            contactoToModify.changeName(newName!!)
                            changeEffected = true
                        }
                        1 -> {
                            val newApellido = Input.enterText("Nuevo apellido del contacto -> ")
                            contactoToModify.changeApellido(newApellido!!)
                            changeEffected = true
                        }
                        2 -> {
                            val newEmail = Input.enterText("Nuevo email del contacto -> ")
                            var correctEmail = false
                            do {
                                if ("@" in newEmail!! && "." in newEmail) {
                                    correctEmail = true
                                    contactoToModify.changeEmail(newEmail)
                                    changeEffected = true
                                } else println("Email invalido")
                            } while (!correctEmail)
                        }
                        3 -> do {
                            val newTelefonos =
                                Input.enterText("Introduzca los nuevos telefonos separados por comas -> ").toString()
                                    .trim().split(",").map { it.replace(" ", "") }.toMutableList()
                            var phonesChecked = false

                            if (newTelefonos.all {
                                    it.length == 9 || (it.length == 12 && it.substring(
                                        0,
                                        3
                                    ) == "+34")
                                }) {
                                contactoToModify.changeTelefonos(newTelefonos)
                                changeEffected = true
                                phonesChecked = true
                            } else {
                                println("Formato inválido. Por favor, inténtelo de nuevo.")
                            }
                        } while (!phonesChecked)
                    }
                } else println("Campo elegido invalido")
            }
        } while (!changeEffected)
    }

    /**
     * Agrega una lista de contactos a la agenda.
     *
     * @param contactos Lista de [Contacto] a agregar a la agenda.
     */
    fun agregarContactos(contactos: MutableList<Contacto>) {
        this.contactos.addAll(contactos)
    }

    /**
     * Elimina un contacto de la agenda según su dirección de correo electrónico.
     */
    fun eliminarContacto() {
        val emailToEliminate = Input.enterText("Introduzca el email del contacto a eliminar: ")
        if (contactos.removeIf {it.getEmail() == emailToEliminate}) {
            println("CONTACTO REMOVIDO CORRECTAMENTE")
        } else println("No hay ningun usuario con ese email")
    }

    /**
     * Busca contactos en la agenda según un criterio de búsqueda y un valor asociado.
     */
    fun buscarContactosPorCriterio() {
        println("Seleccione el criterio de búsqueda:")
        println("1. Nombre")
        println("2. Apellido")
        println("3. Email")
        println("4. Teléfono")

        val criterioSeleccionado = Input.enterDigit("Introduzca el número del criterio de búsqueda -> ")

        val valorABuscar = when (criterioSeleccionado) {
            1 -> Input.enterText("Introduzca el nombre a buscar: ")
            2 -> Input.enterText("Introduzca el apellido a buscar: ")
            3 -> Input.enterText("Introduzca el email a buscar: ")
            4 -> Input.enterText("Introduzca el teléfono a buscar: ")
            else -> {
                println("Criterio no válido.")
                return
            }
        }

        val contactosEncontrados = when (criterioSeleccionado) {
            1 -> contactos.filter { it.getNombre().contains(valorABuscar!!, ignoreCase = true) }
            2 -> contactos.filter { it.getApellido().contains(valorABuscar!!, ignoreCase = true) }
            3 -> contactos.filter { it.getEmail().contains(valorABuscar!!, ignoreCase = true) }
            4 -> contactos.filter { it.getTelefonos().any { telefono -> telefono.contains(valorABuscar!!) } }
            else -> emptyList()
        }


        if (contactosEncontrados.isNotEmpty()) {
            println("Contactos encontrados:")
            contactosEncontrados.forEach { println(it) }
        } else {
            println("No se encontraron contactos con el criterio y valor proporcionados.")
        }
    }

    /**
     * Vacia la lista de contactos en la agenda.
     */
    fun vaciarContactos(){
        contactos = mutableListOf()
    }

    /**
     * Muestra la lista de contactos ordenados por nombre.
     */
    fun mostrarContactos() {
        val listaNombres: List<String> = contactos.map { it.getNombre() }.sorted()

        for (nombre in listaNombres) {
            val contacto = contactos.find { it.getNombre() == nombre }
            if (contacto != null) {
                println(contacto)
            }
        }
    }
}

/**
 * El menu que nos permitira ejecutar diversas acciones sobre nuestra agenda
 *
 *
 * @property agenda La instancia de la agenda que estamos utilizando
 */
class Menu(private val agenda: Agenda) {
    /**
     * Muestra las opciones disponibles en el menu
     */
    fun mostrarMenu() {
        println("MENU OPCIONES AGENDA KOTLIN")
        println("----------------------------")
        println("1. Nuevo contacto")
        println("2. Modificar contacto")
        println("3. Eliminar contacto")
        println("4. Vaciar agenda")
        println("5. Cargar agenda inicial")
        println("6. Mostrar la agenda por criterio")
        println("7. Mostrar la agenda completa")
        println("8. Salir")
        println("----------------------------")
    }

    /**
     *
     * Para seleccionar las opciones disponibles en el menu y llamar a los correspondientes metodos
     *
     * @param menuAnswer La opcion escogida del menu
     */
    fun seleccionMenu(menuAnswer: Int) {
        when (menuAnswer) {
            1 -> agenda.agregarContacto()
            2 -> agenda.modificarContactos()
            3 -> agenda.eliminarContacto()
            4 -> agenda.vaciarContactos()
            5 -> {
                agenda.vaciarContactos()
                val filePath = "src/main/kotlin/kotlin-csv/contactos.csv"
                val reader = Reader()
                agenda.agregarContactos(reader.cargarContactos(filePath))
            }
            6 -> {
                val contactosEncontrados = agenda.buscarContactosPorCriterio()
            }
            7 -> agenda.mostrarContactos()
            8 -> println("Saliendo...")
            else -> println("Opción no válida")
        }

    }
}

/**
 * Clase que maneja la entrada y salida de inputs del usuario
 *
 */
class IO {
    /**
     * Solicita al usuario que ingrese un texto y devuelve el texto ingresado.
     *
     * @param showedText Texto que se muestra al usuario como indicación para la entrada.
     * @return El texto ingresado por el usuario (puede ser nulo si no se ingresa nada).
     */
    fun enterText(showedText: String): String? {
        print(showedText)
        return readLine()?.trim()
    }

    /**
     * Solicita al usuario que ingrese un número entero y devuelve el número ingresado.
     *
     * @param showedText Texto que se muestra al usuario como indicación para la entrada.
     * @return El número entero ingresado por el usuario.
     */
    fun enterDigit(showedText: String): Int {
        var correctConversion = false
        do {
            try {
                print(showedText)
                val answer = readLine()?.trim()
                val result = answer?.toIntOrNull()

                if (result != null) {
                    correctConversion = true
                    return result
                } else {
                    throw NumberFormatException("Entrada no válida")
                }
            } catch (e: NumberFormatException) {
                println(e.message)
            }
        } while (!correctConversion)
        return 0
    }
}

fun main() {
    val filePath = "src/main/kotlin/kotlin-csv/contactos.csv"
    val reader = Reader() //Instaciamos los objetos
    val agenda = Agenda()
    val menu = Menu(agenda)
    val inputOutput = IO()
    agenda.agregarContactos(reader.cargarContactos(filePath)) //Carga inicial de los contactos

    do {
        menu.mostrarMenu()
        val eleccionMenu = inputOutput.enterDigit(">> Seleccione una opción: ")
        menu.seleccionMenu(eleccionMenu)
    } while (eleccionMenu!= 8)
}
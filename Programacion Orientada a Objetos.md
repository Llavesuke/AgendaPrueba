# PROGRAMACION ORIENTADA A OBJETOS
---


Los <mark style="background: #BBFABBA6;">objetos </mark> se utilizan como metáfora para emular las entidades reales del negocio a modelar. El objetivo es aumentar la estabilidad, mantenibilidad y legebilidad del código.

En esto tenemos varias tecnicas como: 

- Herencia

- Cohesión 

- Abstracción

- Polimorfismo: Permite crear metodos que funcionan de distinta manera

- Acoplamiento: 

- Encapsulamiento: Permite restringir el acceso directo a las propiedades, exponiendo solo lo que es necesario

### Clases
---
Las clases son una plantilla para la creaccion de objetos segun un modelo


| Clase | Animal |
| ---- | ---- |
| Nombre de clase | Perro |
| <br>Atributos o propiedades | Nombre<br>Raza<br>Altura |
| <br> |  |

### Objetos
---
Son una instancia de la clase, que tienen como característica.

Identidad: Lo identifica del resto, no va a tener el mismo nombre de variable pero si va a ser identificado por el hash code

Comportamiento: Relaciona 

### Atributos vs Propiedades 
---
**Atributos** -> Necesitan unos métodos externos para acceder a sus valores de get y set value. No pueden ser directamente llamadas.


**Propiedades** -> Internamente tienen un set y un get value, se pueden acceder directamente a ellas


```kotlin
var nombre: String = ""
	get() = field //field en este caso seria ""
	set(value) {
		require(value.trim().isNotEmpty()) { "El nombre no puede estar vacio. "}
		field = value
		}

```

En este caso le estamos diciendo que es necesario que el value que se le asigna a set, es decir el valor de la variable, no este vacío, si lo esta, lanza una excepción
### Constructor secundario
---
Si tienes distintas formas de inicializar una clase, tendrás que usar un constructor secundario. Siempre expanden los parámetros del constructor secundario, el cual sera siempre obligatorio. Solo se pueden añadir más parámetros


```kotlin
class Robot(val nombre: String) {

	var posX: Int = 0
	var posY: Int = 0

	constructor(nombre:String, posX:Int): this(nombre){
		this.posX = posX
		this.posY = 0
	}

	constructor(posX:Int, posY: Int): this("DAW1B") {
		this.posX = posX
		this.posY = posY
	}
}
```


### Enum class
---
Sirve para aumentar su legibilidad
```kotlin
enum class direccion{  
    PositiveY, NegativeY, PositiveX,NegativeX  
}

```


```kotlin
enum class TipoImc(val imcMin: Double,val imcMax: Double, val desc: String) {
    INSUFICIENTE(0.0, 18.4, "Peso insuficiente"),
    SALUDABLE(18.5, 24.9, "Peso saludable"),
    SOBREPESO(25.0, 29.9,"Sobrepeso"),
    OBESIDAD(30.0, 100.0, "Obesidad");
```

Para llamar los datos de cada uno, sería INSUFICIENTE.desc
### Get y Set
---

```kotlin
var imc: String = 0.0
	get() = obtenerImc() //el valor se actualiza cada vez que se llame la variable,se                                                                      rehace el calculo
	private set (value) {
	field = value
	}

private fun obtenerImc(): String{
	 "%.2f".format(this.peso / (this.altura * this.altura))
}

fun main(){
print(persona1.imc)
}
```


### Companion object
---
Se utiliza para propiedades de la clase que son propias de la clase, osea sin instanciarlo, no a los objetos.
Se puede utilizar para utilidades/herramientas, como para pedir una entrada.



```kotlin
companion object {

	const val ALTURA_MEDIA = 1.75
	const val PESO_MEDIA = 62.5

}

```


### Equals
---
El método Equals, es el que utiliza  toda comparación, se puede hacer, por supuesto un override.


```kotlin
override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Persona) return false

        if (this.nombre != other.nombre) return false
        if (this.altura != other.altura) return false
        if (this.peso != other.peso) return false

        return true
    }

```

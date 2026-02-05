EligeTuCesta: Procesamiento de datos y generación de XML
¿De qué trata este proyecto?
Este fue uno de mis primeros proyectos serios en Acceso a Datos. La idea era crear una herramienta que pudiera leer información de empleados desde un archivo de texto plano y convertirla en informes estructurados en XML. Lo diseñé pensando en un caso real de gestión de subvenciones por edad.

¿Cómo funciona?
El programa sigue un flujo lógico para asegurar que los datos no se pierdan ni se corrompan:

Lectura y limpieza: El sistema lee un .txt donde los datos vienen separados por almohadillas (#). Me aseguré de incluir validaciones para que, si una línea tiene un dato mal escrito o un formato de sueldo extraño, el programa no se rompa y te avise de qué registro ha fallado.

Fichero intermedio (.dat): Antes de generar el informe final, paso los datos a un archivo binario. Esto me permitió practicar la serialización de objetos en Java, lo que hace que el manejo de la información sea mucho más rápido y seguro que leer el texto una y otra vez.

Generación de informes con DOM: Al final, el programa clasifica a los empleados. Si son menores de 25 o mayores de 55, los mete en archivos XML distintos. En este paso también calculo automáticamente el sueldo anual con las 14 pagas.

Lo que aprendí haciendo esto
Control de errores: Aprendí a usar bloques try-catch para que el programa sea robusto y no se cierre si el usuario se equivoca al poner una ruta o si el archivo está vacío.

Manejo de ficheros: Reforcé mucho el uso de las librerías java.io y la API DOM para crear estructuras XML desde cero.

Transformación de datos: Tuve que lidiar con detalles técnicos como cambiar comas por puntos en los decimales para que Java pudiera hacer los cálculos matemáticos correctamente.

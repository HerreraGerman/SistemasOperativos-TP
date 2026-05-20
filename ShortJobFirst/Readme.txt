Consigna:
    Simular procesos con tiempos aleatorios que se registren en una tabla.
    Luego realizar un sistema que tomando en cuenta la tabla realizada ejecute en concordancia usando SJF (Shortest Job First).
    Algoritmo debe tener varias ejecuciones y registrar metricas correspondientes.

Funcionamiento:
    El programa simula procesos, se les asigna un nombre "Proceso + Numero Correspondiente" con un tiempo aleatorio, se almacen los datos crudos y se aplica Shortest Job First para ordenamiento.
    Resultado se renderiza en dos tablas, una sin ordenamiento y otra con ordenamiento, dicho resultado se almacena en un archivo txt.

Componentes (Estructura de Archivos):
    ProcessWithRandomTime.java
            Interfaz simple que define:
                getProcessTime()
                setProcessTime(int)
                getProcessName()
                setProcessName(String)
                generateRandomProcessTime(int min, int max)
    ProcessSimulation.java
        Representa un proceso individual.
        Atributos:
            processName
            arrivalTime
            processTime (burst time)
            startTime
            finishTime
            waitingTime
            turnaroundTime
            responseTime
        Implementa ProcessWithRandomTime para generar un tiempo de ejecución aleatorio.
    ProcessResultTable.java
        Genera líneas de texto formateadas para imprimir tablas.
        Produce:
            tabla de procesos sin programar
            tabla de la programación final con métricas (Start, Finish, Wait, Turnaround, Response)
        También imprime esas tablas en consola.
    Simulation.java
        Genera procesos aleatorios con tiempos entre DEFAULT_MIN_TIME y DEFAULT_MAX_TIME.
        Asigna tiempos de llegada aleatorios entre 0 y DEFAULT_MAX_ARRIVAL.
        Programa los procesos usando la función scheduleProcesses(...).
        Calcula métricas y crea un reporte final.
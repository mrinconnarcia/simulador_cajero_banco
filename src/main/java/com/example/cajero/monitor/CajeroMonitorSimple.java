package com.example.cajero.monitor;

import com.example.cajero.models.Persona;

import java.util.LinkedList;
import java.util.Queue;

public class CajeroMonitorSimple implements com.example.cajero.monitor.CajeroMonitor {
    private boolean cajeroDisponible = true;
    private Queue<Persona> filaEspera = new LinkedList<>();  //para el comportamiento  FIFO

    public synchronized void usarCajero(Persona persona) throws InterruptedException {
        if (!cajeroDisponible) {
            filaEspera.add(persona);
            while (filaEspera.peek() != persona) {
                wait();
            }
        }

        cajeroDisponible = false;
        System.out.println(persona.getNombre() + " edad: " + persona.getEdad() + " está usando el cajero.");
        Thread.sleep(2000); // Simulación de uso del cajero

        cajeroDisponible = true;
        filaEspera.poll();
        if (!filaEspera.isEmpty()) {
            notifyAll();
        }
    }
}

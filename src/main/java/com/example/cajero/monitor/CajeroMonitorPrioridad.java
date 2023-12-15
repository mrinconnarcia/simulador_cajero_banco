package com.example.cajero.monitor;

import com.example.cajero.models.Persona;

import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;


public class CajeroMonitorPrioridad implements com.example.cajero.monitor.CajeroMonitor {
    private final Queue<Persona> colaMayor60 = new LinkedList<>();
    private final Queue<Persona> colaGeneral = new LinkedList<>();
    private final Semaphore semaforoCajero = new Semaphore(1);

    public void usarCajero(Persona persona) throws InterruptedException {
        agregarACola(persona);

        synchronized (this) {
            while (!esSuTurno(persona)) {
                wait();
            }
        }

        semaforoCajero.acquire();
        try {
            atenderPersona(persona);
        } finally {
            semaforoCajero.release();
            synchronized (this) {
                removerDeCola(persona);
                notifyAll();
            }
        }
    }

    private synchronized void agregarACola(Persona persona) {
        if (persona.getEdad() >= 60) {
            colaMayor60.add(persona);
        } else {
            colaGeneral.add(persona);
        }
    }

    private synchronized boolean esSuTurno(Persona persona) {
        if (persona.getEdad() >= 60) {
            return colaMayor60.peek() == persona;
        }
        return colaGeneral.peek() == persona && colaMayor60.isEmpty();
    }

    private synchronized void removerDeCola(Persona persona) {
        if (persona.getEdad() >= 60) {
            colaMayor60.remove(persona);
        } else {
            colaGeneral.remove(persona);
        }
    }

    private void atenderPersona(Persona persona) throws InterruptedException {
        System.out.println(persona.getNombre() + " con edad: " + persona.getEdad() + " está usando el cajero.");
        Thread.sleep(2000L); // Simulando el uso del cajero
    }
}

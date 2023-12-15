
package com.example.cajero.threads;

import com.example.cajero.models.Persona;
import com.example.cajero.monitor.CajeroMonitor;

public class CajeroThread extends Thread {
    private final CajeroMonitor cajeroMonitor;
    private final Persona persona;

    public CajeroThread(CajeroMonitor cajeroMonitor, Persona persona) {
        this.cajeroMonitor = cajeroMonitor;
        this.persona = persona;
    }

    @Override
    public void run() {
        try {
            cajeroMonitor.usarCajero(persona);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

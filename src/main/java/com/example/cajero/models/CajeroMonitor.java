package com.example.cajero.monitor;

import com.example.cajero.models.Persona;

public interface CajeroMonitor {
    void usarCajero(Persona persona) throws InterruptedException;
}


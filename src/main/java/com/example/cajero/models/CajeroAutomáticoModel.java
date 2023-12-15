package com.example.cajero.models;

import com.example.cajero.monitor.CajeroMonitorPrioridad;
import com.example.cajero.monitor.CajeroMonitorSimple;

public class CajeroAutomáticoModel {
    private CajeroMonitorSimple cajeroSimple = new CajeroMonitorSimple();
    private CajeroMonitorPrioridad cajeroPrioridad = new CajeroMonitorPrioridad();

    public void usarCajeroSimple(Persona persona) throws InterruptedException {
        cajeroSimple.usarCajero(persona);
    }

    public void usarCajeroPrioridad(Persona persona) throws InterruptedException {
        cajeroPrioridad.usarCajero(persona);
    }
}

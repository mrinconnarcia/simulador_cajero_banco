package com.example.cajero.views;

import com.example.cajero.monitor.CajeroMonitor;
import com.example.cajero.monitor.CajeroMonitorPrioridad;
import com.example.cajero.monitor.CajeroMonitorSimple;
import com.example.cajero.models.Persona;
import com.example.cajero.threads.CajeroThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CajeroAutomáticoController {
    @FXML
    private Pane personasPane;

    private final List<Persona> personas = new ArrayList<>();
    private final CajeroMonitorSimple cajeroSimple = new CajeroMonitorSimple();
    private final CajeroMonitorPrioridad cajeroPrioridad = new CajeroMonitorPrioridad();

    private volatile boolean simulacionActiva = false;

    private static final double POSICION_COLA_X = 50;
    private static final double POSICION_COLA_Y = 250;
    private static final double POSICION_CAJERO_X = 200;
    private static final double POSICION_CAJERO_Y = 150;

    private void iniciarCajeros() {
        iniciarCajeroConTipo(cajeroSimple);
        iniciarCajeroConTipo(cajeroPrioridad);
    }

    private void iniciarCajeroConTipo(CajeroMonitor cajeroMonitor) {
        for (Persona persona : personas) {
            CajeroThread cajeroThread = new CajeroThread(cajeroMonitor, persona);
            cajeroThread.start();
        }
    }



    public void initialize() {
        for (int i = 0; i < 20; ++i) {
            Persona persona = new Persona("Persona" + (i + 1), new Random().nextInt(80) + 18);
            personas.add(persona);
            visualizarPersonaEnCola(persona, i);
        }
//        accederCajero();
    }

    private void visualizarPersonaEnCola(Persona persona, int indice) {
        Circle colaPersonaCircle = new Circle(12);
        colaPersonaCircle.setFill(persona.getEdad() >= 60 ? Color.GREEN : Color.ORANGERED);
        colaPersonaCircle.setCenterX(POSICION_COLA_X);
        colaPersonaCircle.setCenterY(POSICION_COLA_Y + (indice * 24));
        colaPersonaCircle.setId(persona.getNombre());

        Text edadTexto = new Text(String.valueOf(persona.getEdad()));
        edadTexto.setFont(new Font(12));
        edadTexto.setX(POSICION_COLA_X - 6);
        edadTexto.setY(POSICION_COLA_Y + (indice * 24) + 4);

        Platform.runLater(() -> {
            personasPane.getChildren().add(colaPersonaCircle);
            personasPane.getChildren().add(edadTexto);
        });
    }


//    private void accederCajero() {
//        personas.forEach(persona -> new Thread(() -> {     // Thread es Hebra
//            try {
//                cajeroPrioridad.usarCajero(persona);
//                Platform.runLater(() -> moverPersonaHaciaCajero(persona));
//                Thread.sleep(2000); // tiempo que la persona pasa en el cajero
//                Platform.runLater(() -> removerPersonaDeCajero(persona));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start());
//    }

    private void accederCajero(CajeroMonitorPrioridad cajero) {
        personas.forEach(persona -> new Thread(() -> {
            try {
                cajero.usarCajero(persona);
                moverYRemoverPersona(persona);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start());
    }

    private void accederCajero(CajeroMonitorSimple cajero) {
        personas.forEach(persona -> new Thread(() -> {
            try {
                cajero.usarCajero(persona);
                moverYRemoverPersona(persona);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start());
    }

    private void moverYRemoverPersona(Persona persona) throws InterruptedException {
        Platform.runLater(() -> moverPersonaHaciaCajero(persona));
        Thread.sleep(2000); // tiempo que la persona pasa en el cajero
        Platform.runLater(() -> removerPersonaDeCajero(persona));
    }

    private void moverPersonaHaciaCajero(Persona persona) {
        Circle personaCircle = (Circle) personasPane.lookup("#" + persona.getNombre());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), personaCircle);
        transition.setToX(POSICION_CAJERO_X - personaCircle.getCenterX());
        transition.setToY(POSICION_CAJERO_Y - personaCircle.getCenterY());
        transition.play();
    }

    private void removerPersonaDeCajero(Persona persona) {
        Circle personaCircle = (Circle) personasPane.lookup("#" + persona.getNombre());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), personaCircle);
        transition.setToX(500);
        transition.setToY(0);
        transition.setOnFinished(event -> personasPane.getChildren().remove(personaCircle));
        transition.play();
    }

    @FXML
    private void onUsarCajeroSimple() {
        accederCajero(cajeroSimple);
    }

    @FXML
    private void onUsarCajeroPrioridad() {
        accederCajero(cajeroPrioridad);
    }
}


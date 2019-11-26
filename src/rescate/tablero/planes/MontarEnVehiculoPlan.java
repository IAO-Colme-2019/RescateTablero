package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class MontarEnVehiculoPlan extends Plan {

    @Override
    public void body() {

        System.out.println("tablero recibe peticion subir a vehiculo...");
        IMessageEvent request = (IMessageEvent) getInitialEvent();
        //TODO Obtener Jugador del mensaje.
        Jugador jugador = (Jugador) request.getParameter("emisor").getValue();
        Boolean camionOAmbulancia = (Boolean) request.getParameter("camionOAmbulancia").getValue();

        //Get Ambulancia
        Ambulancia ambulancia = (Ambulancia) getBeliefbase().getBelief("ambulancia").getFact();
        //Get Camion Bomberos
        CamionBomberos camionBomberos = (CamionBomberos) getBeliefbase().getBelief("camionBomberos").getFact();

        getBeliefbase().getBeliefSet("ambulancia").removeFact(ambulancia);
        getBeliefbase().getBeliefSet("camionBomberos").removeFact(camionBomberos);

        List<Jugador> jugadoresEnAmbulancia = ambulancia.getJugadoresEnVehiculo();
        List<Jugador> jugadoresEnCamion = camionBomberos.getJugadoresEnVehiculo();

        int[] posicionAmbulancia = ambulancia.getPosicion();
        int[] posicionCamionBomberos = camionBomberos.getPosicion();
        int[] posicionJugador = jugador.getPosicion();

        //Si camionOAmbulancia ==1 peticion para ir a ambulancia, si es 0 es para ir a camion.
        if (camionOAmbulancia) {
            jugadoresEnAmbulancia.add(jugador);
            posicionJugador = posicionAmbulancia;
            jugador.setPosicion(posicionJugador);

        } else {
            jugadoresEnCamion.add(jugador);
            posicionJugador = posicionCamionBomberos;
            jugador.setPosicion(posicionJugador);
        }

        ambulancia.setJugadoresEnVehiculo(jugadoresEnAmbulancia);
        camionBomberos.setJugadoresEnVehiculo(jugadoresEnCamion);


        getBeliefbase().getBeliefSet("ambulancia").addFact(ambulancia);
        getBeliefbase().getBeliefSet("camionBomberos").addFact(camionBomberos);
        //TODO Añadir a la base de creencias al jugador.


    }


}

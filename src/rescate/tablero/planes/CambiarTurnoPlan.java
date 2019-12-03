package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class CambiarTurnoPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de cambiar turno");
    
    
    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    
    // Turno
    int turno = (int) getBeliefbase().getBelief("turno").getFact();
    
    // Se encuentra en la lista de jugadores del tablero el jugador al que le toca jugar en el siguiente turno
    int indiceProximoJugador = turno % t.getJugadores().size();
    Jugador jugador = t.getJugadores().get(indiceProximoJugador);

    //Se añaden los PA genericos y se restauran los de clase
    if (jugador.getRol() == Jugador.Rol.ESPUMA_IGNIFUGA) {
      jugador.setPuntosAccion(jugador.getPuntosAccion() + 3);
      jugador.setPuntosAccionExtincion(3);
    }
    else if (jugador.getRol() == Jugador.Rol.GENERALISTA) {
      jugador.setPuntosAccion(jugador.getPuntosAccion() + 5);
    }
    else{
      jugador.setPuntosAccion(jugador.getPuntosAccion() + 4);
      if (jugador.getRol() == Jugador.Rol.JEFE) {
        jugador.setPuntosAccionMando(2);
      }
      else if (jugador.getRol() == Jugador.Rol.RESCATES) {
        jugador.setPuntosAccionMovimiento(3);
      }
    }

    getBeliefbase().getBelief("siguienteTurno").setFact(false);

    // Con esto evitamos que se pueda quedar a true porque haya más de 3 PDIs y así no puede poner PDIs a mitad de turno
    getBeliefbase().getBelief("finTurno").setFact(false);


    System.out.println("[INFO] Turno cambiado, ahora juega el jugador con id " + indiceProximoJugador);

    // Se informa también al jugador al que le toca ahora jugar
    IMessageEvent respuesta = createMessageEvent("Inform_Turno_Asignado");
    TurnoAsignado predicado = new TurnoAsignado();
    predicado.setHabitacion(t.getHabitacion(jugador.getHabitacion()));
    respuesta.setContent(predicado);
    respuesta.getParameterSet(SFipa.RECEIVERS).addValue(jugador.getIdAgente());
    sendMessage(respuesta);
  }
  
}

package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class FinTurnoPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de finalizar un turno");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Turno
    int turno = (int) getBeliefbase().getBelief("turno").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    CambiarTurno accion = (CambiarTurno) peticion.getContent();
    
    // Se comprueba que sea el turno del jugador
    if (t.getIndiceJugador(idJugador) != (turno % t.getJugadores().size())) {
      System.out.println("[RECHAZADO] No es el turno del jugador con id " + idJugador);
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Refuse_Cambiar_Turno");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
      return;
    }
    
    System.out.println("[INFO] Siguiente turno");
    
    // Se informa al jugador que el turno se ha cambiado correctamente
    IMessageEvent respuesta = createMessageEvent("Inform_Turno_Cambiado");
    respuesta.setContent(new TurnoCambiado());
    respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
    sendMessage(respuesta);

    // Se suma el turno y se pone el belief
    turno++;
    getBeliefbase().getBelief("turno").setFact(turno);
    getBeliefbase().getBelief("propagarFuego").setFact(true);
  }
  
}

package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.ontologia.acciones.UnirsePartida;
import rescate.ontologia.conceptos.Jugador;
import rescate.ontologia.conceptos.Tablero;
import rescate.ontologia.predicados.JugadorUnido;

class UnirJugadorPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de unirse a la partida");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    UnirsePartida accion = (UnirsePartida) peticion.getContent();

    // Se inicializa la lista de jugadores en caso de que sea el primer jugador que se une
    if (t.getJugadores() == null) {
      t.setJugadores(new ArrayList<Jugador>());
    }
    // Si el jugador ya está unido
    else if (t.getJugador(idJugador) != null) {
      System.out.println("[RECHAZADO] El jugador con id " + idJugador + " ya está unido a la partida");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Refuse_Unirse_Partida");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
      return;
    }

    System.out.println("[INFO] Se une a la partida el jugador con id " + idJugador);

    // Se añade el jugador al tablero
    Jugador jugador = new Jugador();
    jugador.setIdAgente(idJugador);
    t.getJugadores().add(jugador); 

    // Se actualiza en la base de creencias el hecho tablero
    getBeliefbase().getBelief("tablero").setFact(t);
    getBeliefbase().getBelief("numJugadores").setFact(((int) getBeliefbase().getBelief("numJugadores").getFact())+1);

    // Se informa de que el jugador se ha unido
    IMessageEvent respuesta = createMessageEvent("Inform_Jugador_Unido");
    respuesta.setContent(new JugadorUnido());
    respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
    sendMessage(respuesta);

  }

}

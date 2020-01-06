package rescate.tablero.planes;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class AcabarPartidaPlan extends Plan {

	@Override
	public void body() {

    if((boolean) getBeliefbase().getBelief("jugando").getFact()){
      System.out.println("[PLAN] El tablero acaba la partida");
  
      // Tablero y jugadores
      Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
      ArrayList<Jugador> jugadores = t.getJugadores();
  
      // Beliefs y resultado
      int salvados = (int) getBeliefbase().getBelief("salvados").getFact();
  
      // Se informa a todos los jugadores de que la partida ha acabado y del resultado
      IMessageEvent respuesta = createMessageEvent("Inform_Partida_Acabada");
      ResultadoPartida predicado = new ResultadoPartida();
      predicado.setResultado((salvados > 6) ? 0 : 1);
      respuesta.setContent(predicado);
  
      // Como destinatarios, todos los jugadores
      for (Jugador j: jugadores) {
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(j.getIdAgente());
      }
  
      // Se envia la respuesta
      sendMessage(respuesta);
      // Se deja de jugar
      getBeliefbase().getBelief("jugando").setFact(false);
    }
  }

}
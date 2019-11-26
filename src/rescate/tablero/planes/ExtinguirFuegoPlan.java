package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class ExtinguirFuegoPlan extends Plan {

  @Override
  public void body() {
    FuegoExtinguidoPredicado fa = new FuegoExtinguidoPredicado();
    System.out.println("tablero recibe peticion de extinguir fuego...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		AgentIdentifier jugadorId = (AgentIdentifier) request.getParameter("emisor").getValue();
    Casilla casillaSolicitada = (Casilla) request.getParameter("casilla").getValue();
    Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    Jugador jugador = new Jugador();
    for(int i = 0; i<tablero.getJugadores().size(); i++){
      if(tablero.getJugadores().get(i).getIdAgente().equals(jugadorId)){
        jugador = tablero.getJugadores().get(i);
      }
    }
		boolean ok = false;

    if (casillaSolicitada.tieneFuego() == 2 && (jugador.getPuntosAccion() >= 2 || jugador.getPuntosAccionExtincion() >= 2)){ //tiene fuego y suficientes PA
      ok=true;
    }

    IMessageEvent msg = createMessageEvent("Agree_Extinguir_Fuego");

		if (!ok) {
			msg = createMessageEvent("Refuse_Extinguir_Fuego");
      
      if(casillaSolicitada.tieneFuego() != 2){
        System.out.println("tablero deniega peticion de extinguir fuego: la casilla no tiene fuego");
      }
      if(jugador.getPuntosAccion() < 2){
        System.out.println("tablero deniega peticion de apagar fuego: el jugador no tiene suficientes PA");
      }
		} else {

      //Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
      Casilla[][] mapa = tablero.getMapa();

      for(int i=0; i< mapa.length; i++){
        for(int j=0; j< mapa[0].length; j++){
          if(mapa[i][j].equals(casillaSolicitada)){
            mapa[i][j].setTieneFuego(0);
          }
        }
      }

      tablero.setMapa(mapa);

      getBeliefbase().getBelief("tablero").setFact(tablero);
      System.out.println("tablero informa que el fuego ha sido extinguido");

    }
    msg.setContent(fa);
    msg.getParameterSet(SFipa.RECEIVERS).addValue(jugadorId);
    sendMessage(msg);

  }

}

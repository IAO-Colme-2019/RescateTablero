package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class ExtinguirHumoPlan extends Plan {

  @Override
  public void body() {

    HumoExtinguidoPredicado fa = new HumoExtinguidoPredicado();
    System.out.println("tablero recibe peticion de extinguir humo...");
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

    if (casillaSolicitada.tieneFuego() == 1 && (jugador.getPuntosAccion() >= 1 || jugador.getPuntosAccionExtincion() >= 1)){ //tiene fuego y suficientes PA
      ok=true;
    }

    IMessageEvent msg = createMessageEvent("Agree_Extinguir_Humo");

		if (!ok) {
			msg = createMessageEvent("Refuse_Extinguir_Humo");
      
      if(casillaSolicitada.tieneFuego() != 2){
        System.out.println("tablero deniega peticion de extinguir humo: la casilla no tiene humo");
      }
      if(jugador.getPuntosAccion() < 1){
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
      System.out.println("tablero informa que el humo ha sido extinguido");

    }
    msg.setContent(fa);
    msg.getParameterSet(SFipa.RECEIVERS).addValue(jugadorId);
    sendMessage(msg);

  }

}

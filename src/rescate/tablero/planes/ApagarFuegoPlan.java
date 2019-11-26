package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class ApagarFuegoPlan extends Plan {

  @Override
  public void body() {
    FuegoApagadoPredicado fa = new FuegoApagadoPredicado();
    System.out.println("tablero recibe peticion de apagar fuego...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		AgentIdentifier jugadorId = (AgentIdentifier) request.getParameter("sender").getValue();
    Casilla casillaSolicitada = (Casilla) request.getParameter("casilla").getValue();
    Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    Jugador jugador = new Jugador();
    for(int i = 0; i<tablero.getJugadores().size(); i++){
      if(tablero.getJugadores().get(i).getIdAgente().equals(jugadorId)){
        jugador = tablero.getJugadores().get(i);
      }
    }

    boolean ok = false;

    if (casillaSolicitada.tieneFuego() == 2 && (jugador.getPuntosAccion() >= 1 || jugador.getPuntosAccionExtincion() >= 1)){ //tiene fuego y suficientes PA
      ok=true;
    }

    IMessageEvent msg = createMessageEvent("Agree_Apagar_Fuego");

		if (!ok) {
      msg = createMessageEvent("Refuse_Apagar_Fuego");
      if(casillaSolicitada.tieneFuego() != 2){
        System.out.println("tablero deniega peticion de apagar fuego: la casilla no tiene fuego");
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
            mapa[i][j].setTieneFuego(1);
          }
        }
      }

      tablero.setMapa(mapa);

      getBeliefbase().getBelief("tablero").setFact(tablero);
      System.out.println("tablero informa que el fuego ha sido apagado (se transforma en humo)");

    }
    msg.setContent(fa);
    msg.getParameterSet(SFipa.RECEIVERS).addValue(jugadorId);
    sendMessage(msg);
  }

}

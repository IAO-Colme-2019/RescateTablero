package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class EliminarMateriaPeligrosaPlan extends Plan {

  @Override
  public void body() {
	    System.out.println("tablero recibe peticion de eliminar matera peligrosa...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		AgentIdentifier jugadorId = (AgentIdentifier) request.getParameter("emisor").getValue();
		Casilla casillaSolicitada = (Casilla) request.getParameter("casilla").getValue();
		MateriaPeligrosaEliminadaPredicado mpep = new MateriaPeligrosaEliminadaPredicado();

		boolean ok = false;
		Jugador jugador = new Jugador();
		Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();

		for (int i = 0; i < tablero.getJugadores().size(); i++) {
			if (tablero.getJugadores().get(i).getIdAgente().equals(jugadorId)) {
				jugador = tablero.getJugadores().get(i);
			}
		}
		if (casillaSolicitada.tieneMateriaPeligrosa() == true && (jugador.getPuntosAccion() >= 2 && jugador.getRol() == Jugador.Rol.MATERIAS_PELIGROSAS)) { 																									
			ok = true;
		}



	    IMessageEvent msg = createMessageEvent("Agree_Eliminar_Materia");
		if (!ok) {
			msg = createMessageEvent("Refuse_Eliminar_Materia");
			System.out.println("tablero deniega peticion de eliminar materia peligrosa: la casilla no tiene materia peligrosa");
		} else {

	    Casilla[][] mapa = tablero.getMapa();

	    for(int i=0; i< mapa.length; i++){
	       for(int j=0; j< mapa[0].length; j++){
	         if(mapa[i][j].equals(casillaSolicitada)){
	           mapa[i][j].setTieneMateriaPeligrosa(false);
	         }
	       }
	     }

	     tablero.setMapa(mapa);

	     getBeliefbase().getBelief("tablero").setFact(tablero);
	     System.out.println("tablero informa que la materia peligrosa ha sido eliminada");

	    }
	    msg.setContent(mpep);
	    msg.getParameterSet(SFipa.RECEIVERS).addValue(jugadorId);
	    sendMessage(msg);
  }

}
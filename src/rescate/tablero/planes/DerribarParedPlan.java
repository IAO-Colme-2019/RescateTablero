package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class DerribarParedPlan extends Plan {

  @Override
  public void body() {
	  	System.out.println("tablero recibe peticion de derribar pared...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		MuroDerribadoPredicado md = new MuroDerribadoPredicado();

		AgentIdentifier jugadorId = (AgentIdentifier) request.getParameter("emisor").getValue();
		Casilla paredSolicitada = (Casilla) request.getParameter("casilla").getValue();
		boolean derribar = false;
		boolean derribar_empezar = false;

		Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
		Jugador jugador = new Jugador();
		int posicionConex = 0;
		for (int i = 0; i < tablero.getJugadores().size(); i++) {
			if (tablero.getJugadores().get(i).getIdAgente().equals(jugadorId)) {
				jugador = tablero.getJugadores().get(i);
			}
		}
		

		if (jugador.getPuntosAccion() >= 2	|| (jugador.getRol()==Jugador.Rol.RESCATES && jugador.getPuntosAccion() >= 1)) { 																								
				
			for (int i = 0; i < 4 && derribar != true || derribar_empezar != true; i++) {
				if (paredSolicitada.getConexiones()[i] == Casilla.Conexion.PARED_SEMIRROTA) {
					derribar = true;
					posicionConex = i;
				}else if(paredSolicitada.getConexiones()[i] == Casilla.Conexion.PARED) {
					derribar_empezar = true;
					posicionConex = i;
				}
			}

		}
			IMessageEvent msg = createMessageEvent("Agree_Derribar_Muro");
		if (!derribar && !derribar_empezar) {
				msg = createMessageEvent("Refuse_Derribar_Muro");
				System.out.println("tablero deniega peticion de derribar pared: no hay paredes");
		} else if (derribar){
				 msg = createMessageEvent("agreed Derribar");
				Casilla.Conexion[] conexiones = paredSolicitada.getConexiones();
				conexiones[posicionConex] = Casilla.Conexion.PARED_ROTA;
				paredSolicitada.setConexiones(conexiones);
				Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
				getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
				mapa[paredSolicitada.getPosicion()[1]][paredSolicitada.getPosicion()[0]] = paredSolicitada;
				getBeliefbase().getBeliefSet("casillas").addFact(mapa);
				System.out.println("tablero informa que la pared ha sido derribada correctamente");

		} else if (derribar_empezar){

				
				Casilla.Conexion[] conexiones = paredSolicitada.getConexiones();
				conexiones[posicionConex] = Casilla.Conexion.PARED_SEMIRROTA;
				paredSolicitada.setConexiones(conexiones);
				Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
				getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
				mapa[paredSolicitada.getPosicion()[1]][paredSolicitada.getPosicion()[0]] = paredSolicitada;
				getBeliefbase().getBeliefSet("casillas").addFact(mapa);
				System.out.println("tablero informa que la pared ha sido semi-derribada");
				
		}

		msg.setContent(md);
		msg.getParameterSet(SFipa.RECEIVERS).addValue(jugadorId);
		sendMessage(msg);

		
  }

}

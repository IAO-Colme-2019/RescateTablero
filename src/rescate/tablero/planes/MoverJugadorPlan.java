package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.conceptos.Casilla.Conexion;
import rescate.ontologia.predicados.*;

class MoverJugadorPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de mover a un jugador");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    Desplazar accion = (Desplazar) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Casilla de destino
    Casilla destino = null;
    // Obstaculo entre origen y destino
    boolean obstaculo = false;

    switch(accion.getDireccion()) {
      case ARRIBA:
        obstaculo = hayObstaculo(c.getConexiones()[0]);
        destino = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
        break;
      case DERECHA:
        obstaculo = hayObstaculo(c.getConexiones()[1]);
        destino = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
        break;
      case ABAJO:
        obstaculo = hayObstaculo(c.getConexiones()[2]);
        destino = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
        break;
      case IZQUIERDA:
        obstaculo = hayObstaculo(c.getConexiones()[3]);
        destino = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
        break;
    }

    if (obstaculo) {
      System.out.println("[FALLO] Desde la casilla del jugador con id " + idJugador + " en dirección " + accion.getDireccion() + " hay un obstáculo.");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Desplazar");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    else {
      if (jugador.getPuntosAccion() + jugador.getPuntosAccionMovimiento() >= puntosAccionNecesarios(destino, jugador)) {
        jugador.setPosicion(destino.getPosicion());
        if(destino.getPuntoInteres()==Casilla.PuntoInteres.VICTIMA_OCULTO){
          destino.setPuntoInteres(Casilla.PuntoInteres.VICTIMA);
        }else if (destino.getPuntoInteres()==Casilla.PuntoInteres.FALSA_ALARMA_OCULTO){
          destino.setPuntoInteres(Casilla.PuntoInteres.FALSA_ALARMA);
        }
        getBeliefbase().getBelief("tablero").setFact(t);

        System.out.println("[INFO] El jugador con id " + idJugador + " se ha desplazado a la casilla" + destino.getPosicion()[0] + " , "+ destino.getPosicion()[1]);
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Inform_Desplazar");
        respuesta.setContent(new Desplazado());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene PA suficientes para desplazarse");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Desplazar");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      
    }

  }

  public boolean hayObstaculo(Casilla.Conexion con) {
    return con == Casilla.Conexion.PUERTA_CERRADA || con == Casilla.Conexion.PARED || con == Casilla.Conexion.PARED_SEMIRROTA;
  }

  public int puntosAccionNecesarios(Casilla destino, Jugador j) {

    return 1;
  }

}

package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class IdentificarPuntoDeInteresPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de identificar PDI");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    IdentificarPuntoInteres accion = (IdentificarPuntoInteres) peticion.getContent();

    // Hechos
    int PDITablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
    int PDIVictima = (int) getBeliefbase().getBelief("PDIVictima").getFact();
    int PDIFalsaAlarma = (int) getBeliefbase().getBelief("PDIFalsaAlarma").getFact();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Si no es un PDI oculto
    if (accion.getCasilla().getPuntoInteres() != Casilla.PuntoInteres.OCULTO) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no está en una casilla con PDI oculto");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Identificar_PDI");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    // Si el jugador no tiene rol EXP en IMG
    else if (jugador.getRol() != Jugador.Rol.EXPERTO_EN_IMAGENES){
      System.out.println("[FALLO] El jugador con id " + idJugador + " no tiene rol EXPERTO EN IMAGENES");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Identificar_PDI");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    // Si todas las condiciones se cumplen, se realiza la accion
    else {
      // Suficientes PA
      if (jugador.getPuntosAccion() > 0) {
        System.out.println("[INFO] Se identifica el PDI de la casilla[" + accion.getCasilla().getPosicion()[0] + ", " + accion.getCasilla().getPosicion()[1] + "]");
        // Si no queda de un tipo, se coloca del otro...
        if (PDIVictima == 0) {
          accion.getCasilla().setPuntoInteres(Casilla.PuntoInteres.NADA);
          PDIFalsaAlarma--;
          PDITablero--;
        } else if (PDIFalsaAlarma == 0) {
          accion.getCasilla().setPuntoInteres(Casilla.PuntoInteres.VICTIMA);
          PDIVictima--;
        }
        // Si quedan de los dos tipos, de manera aleatoria...
        else if (Math.random() < 0.5) {
          accion.getCasilla().setPuntoInteres(Casilla.PuntoInteres.NADA);
          PDIFalsaAlarma--;
          PDITablero--;
        } else {
          accion.getCasilla().setPuntoInteres(Casilla.PuntoInteres.VICTIMA);
          PDIVictima--;
        }
        // Se actualiza la casilla
        t.setCasilla(accion.getCasilla().getPosicion()[0], accion.getCasilla().getPosicion()[1], accion.getCasilla());
        // Se actualiza el consumo de PA
        jugador.setPuntosAccion(jugador.getPuntosAccion() - 1);
        // Se actualizan los hechos
        getBeliefbase().getBelief("PDITablero").setFact(PDITablero);
        getBeliefbase().getBelief("PDIVictima").setFact(PDIVictima);
        getBeliefbase().getBelief("PDIFalsaAlarma").setFact(PDIFalsaAlarma);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_PDI_Identificado");
        respuesta.setContent(new PuntoInteresIdentificado());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // No suficientes PA
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Identificar_PDI");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }

  }

}

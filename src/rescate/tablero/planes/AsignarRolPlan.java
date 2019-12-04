package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.RolElegido;
import rescate.ontologia.predicados.RolesDisponibles;

public class AsignarRolPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de elección de rol");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    ElegirRol accion = (ElegirRol) peticion.getContent();

    // Se identifican los roles disponibles y...
    ArrayList<Integer> roles = new ArrayList<Integer>();
    Collections.addAll(roles, 0, 1, 2, 3, 4, 5, 6, 7, 8);
    roles.remove(0);
    // ...se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = null;
    for (int i = 0; i < t.getJugadores().size(); i++) {
      roles.remove(t.getJugadores().get(i).getRol());
      if (t.getJugadores().get(i).getIdAgente() == idJugador) {
        jugador = t.getJugadores().get(i);
      }
    }

    // PA suficientes (no necesario si no tiene ningun rol previamente)
    if (jugador.getRol() == 0 || (jugador.getRol() != 0 && jugador.getPuntosAccion() > 1)) {
      // Si dentro de los roles disponibles está el que quiere el jugador...
      if (roles.contains(accion.getRol())) {
        System.out.println("[INFO] El jugador con id " + idJugador + " cambia al rol " + accion.getRol());
        // Se reducen los PA si es necesario
        if (jugador.getRol() != 0) {
          jugador.setPuntosAccion(jugador.getPuntosAccion() - 2);
        }
        // Se actualiza el rol del jugador
        jugador.setRol(accion.getRol());
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se confirma al jugador la asignación del rol elegido
        IMessageEvent respuesta = createMessageEvent("Inform_Rol_Elegido");
        respuesta.setContent(new RolElegido());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // Si no...
      else {
        System.out.println("[RECHAZADO] El rol " + accion.getRol() + " solicitado por el jugador con id " + idJugador + " no está disponible");
        // Se contesta al jugador rechazando la elección de rol y con la lista de roles disponibles
        IMessageEvent respuesta = createMessageEvent("Refuse_Rol_Elegido");
        RolesDisponibles predicado = new RolesDisponibles();
        predicado.setRolesDisponibles(roles);
        respuesta.setContent(predicado);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
    // No PA suficientes
    else {
      System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene PA suficientes");
      // Se contesta al jugador rechazando la elección de rol y con la lista de roles disponibles
      IMessageEvent respuesta = createMessageEvent("Refuse_Rol_Elegido");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }

  }

}

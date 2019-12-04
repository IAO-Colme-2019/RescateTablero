package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class UsarCannonAguaPlan extends Plan {


  private Tablero t;

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de usar el cañón de agua");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    t = (Tablero) getBeliefbase().getBelief("t").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    UsarCannonAgua accion = (UsarCannonAgua) peticion.getContent();

    // Se encuentra en la lista de jugadores del t el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Si el jugador no esta subido al camion
    if(!jugador.subidoCamion()){
      System.out.println("[FALLO] El jugador " + idJugador + " no esta en el camion de bomberos");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Usar_Cannon_Agua");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);

    }
    // Si el jugador esta subido al camion
    else {
      // Suficientes PA
      if (jugador.getPuntosAccion() > ((jugador.getRol() == 8) ? 2 : 4)) {
        // Se consume el PA
        jugador.setPuntosAccion(jugador.getPuntosAccion() - ((jugador.getRol() == 8) ? 2 : 4));
        // Se tiran los dados
        int [] posicion = tirarDados(); 
        // Preguntamos al conductor de ambualncia si quiere volver a tirar los dados 
        if(jugador.getRol() == 8) {
          // Pregunta
          IMessageEvent pregunta = createMessageEvent("Request_Aceptar_Tirada");
          pregunta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          AceptarTirada aceptar = new AceptarTirada(posicion);
          pregunta.setContent(aceptar);
          // Respuesta
          IMessageEvent respuesta = sendMessageAndWait(pregunta, 10000);
          aceptar = (AceptarTirada) respuesta.getContent();
          System.out.println("[RESPUESTA] La tirada ha sido " +  ((aceptar.tiradaAceptada()) ? "aceptada" : "rechazada") + " por el jugador con id " + idJugador);
          // Si la respuesta es negativa se vuelven a tirar los dados 
          if (!aceptar.tiradaAceptada()){
            posicion = tirarDados();
          }
        }
        System.out.println("[INFO] Se apunta el camión de bomberos a la casilla[" + posicion[0] + ", " + posicion[1] + "]");
        // Se apagan las adyacentes
        apagarAdyacentes(posicion);
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Inform_Cannon_De_Agua_Usado");
        respuesta.setContent(new CannonDeAguaUsado());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // No suficientes PA
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para usar cañon de agua");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Usar_Cannon_Agua");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
  }

  public int[] tirarDados(){
    int[] posicion = new int[2];
    // Arriba-Derecha
    if (t.getMapa()[0][7].esCamionBomberos()) {
      posicion[0] = (int)(Math.random() * 4 + 5); // entre 5 y 8
      posicion[1] = (int)(Math.random() * 3 + 1); // entre 1 y 3
    }
    // Abajo-Derecha
    else if (t.getMapa()[5][9].esCamionBomberos()) {
      posicion[0] = (int)(Math.random() * 4 + 5); // entre 5 y 8
      posicion[1] = (int)(Math.random() * 3 + 4); // entre 4 y 6
    }
    // Arriba-Izquierda
    else if (t.getMapa()[1][0].esCamionBomberos()) {
      posicion[0] = (int)(Math.random() * 4 + 1); // entre 1 y 4
      posicion[1] = (int)(Math.random() * 3 + 1); // entre 1 y 3
    }
    // Abajo-Izquierda
    else {
      posicion[0] = (int)(Math.random() * 4 + 1); // entre 1 y 4
      posicion[1] = (int)(Math.random() * 3 + 4); // entre 4 y 6
    }
    return posicion;
  }

  public void apagarAdyacentes(int[] posicion) {
    // Casilla y conexiones
    Casilla c =  t.getMapa()[posicion[1]][posicion[0]];
    int[] conexiones =  c.getConexiones();;
    // Se apaga la casilla
    c.setTieneFuego(0);

    // Arriba
    if(conexiones[0] == 0 || conexiones[0] == 1 || conexiones[0] == 5) {
      t.getMapa()[posicion[1] - 1][posicion[0]].setTieneFuego(0);
    }
    // Derecha
    if(conexiones[1] == 0 || conexiones[1] == 1 || conexiones[1] == 5) {
      t.getMapa()[posicion[1]][posicion[0] + 1].setTieneFuego(0);
    }
    // Abajo
    if(conexiones[2] == 0 || conexiones[2] == 1 || conexiones[2] == 5) {
      t.getMapa()[posicion[1] + 1][posicion[0]].setTieneFuego(0);
    }
    
    // Izquierda
    if(conexiones[3] == 0 || conexiones[3] == 1 || conexiones[3] == 5) {
      t.getMapa()[posicion[1]][posicion[0] - 1].setTieneFuego(0);
    }
  }

}

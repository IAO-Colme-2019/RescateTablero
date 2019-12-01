package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.conceptos.Casilla.Conexion;
import rescate.ontologia.predicados.*;

class UsarCannonDeAguaPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de usar el cañón de agua");
    CannonUsado cu = new CannonUsado();
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = tablero.getJugador(idJugador);

    // Casilla en la que está el jugador

    boolean usado = false;

    IMessageEvent msg = createMessageEvent("Inform_Usar_Canon");

    if(jugador.subidoCamion() && (jugador.getPuntosAccion() > 4 || (jugador.getRol() == Jugador.Rol.CONDUCTOR && jugador.getPuntosAccion() > 2))){
      
      int posicionCanonX;
      int posicionCanonY;

      Casilla[][] mapa = tablero.getMapa();

      if (jugador.getPosicion()[1] == 0) {
        //Cuadrante de arriba-derecha
        posicionCanonX = (int)(Math.random() * 4 + 5);// entre 5 y 8
        posicionCanonY = (int)(Math.random() * 3 + 1);// entre 1 y 3
        
        Casilla[][] nuevoMapa = apagarAdyacentes(mapa, posicionCanonX, posicionCanonY);

        tablero.setMapa(nuevoMapa);
      }else if(jugador.getPosicion()[1] == mapa.length-1){
        //Cuadrante de abajo-izquierda
        posicionCanonX = (int)(Math.random() * 4 + 1);// entre 1 y 4
        posicionCanonY = (int)(Math.random() * 3 + 4);// entre 4 y 6
        
        Casilla[][] nuevoMapa = apagarAdyacentes(mapa, posicionCanonX, posicionCanonY);

        tablero.setMapa(nuevoMapa);
      }else if(jugador.getPosicion()[0] == 0){
        //Cuadrante de arriba-izquierda
        posicionCanonX = (int)(Math.random() * 4 + 1);// entre 1 y 4
        posicionCanonY = (int)(Math.random() * 3 + 1);// entre 1 y 3
        
        Casilla[][] nuevoMapa = apagarAdyacentes(mapa, posicionCanonX, posicionCanonY);

        tablero.setMapa(nuevoMapa);
      }else if(jugador.getPosicion()[0] == mapa[0].length-1){
        //Cuadrante de abajo-derecha
        posicionCanonX = (int)(Math.random() * 4 + 5);// entre 5 y 8
        posicionCanonY = (int)(Math.random() * 3 + 4);// entre 4 y 6
        
        Casilla[][] nuevoMapa = apagarAdyacentes(mapa, posicionCanonX, posicionCanonY);

        tablero.setMapa(nuevoMapa);
      }else{
        //debe se imposible
      }

      usado = true;
    }

    

		if (!usado) {
      msg = createMessageEvent("Refuse_Usar_Canon");
      if(!jugador.subidoCamion()){
        System.out.println("[RECHAZADO] tablero deniega peticion de usar cañon: el jugador no está subido al camión");
      }
      if(!(jugador.getPuntosAccion() > 4 || (jugador.getRol() == Jugador.Rol.CONDUCTOR && jugador.getPuntosAccion() > 2))){
        System.out.println("[RECHAZADO] tablero deniega peticion de usar cañon: el jugador no tiene suficientes PA");
      }
    }else{
      getBeliefbase().getBelief("tablero").setFact(tablero);
      System.out.println("[INFO] tablero informa que se ha usado el cañon de agua");
    }
    
    msg.setContent(cu);
    msg.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
    sendMessage(msg);

  }


  public Casilla[][] apagarAdyacentes(Casilla[][] mapa, int posX, int posY){

    Casilla casilla = mapa[posY][posX];
    Conexion[] conexiones =  casilla.getConexiones();

    casilla.setTieneFuego(Casilla.Fuego.NADA);

    //Casilla adyacente arriba
    if(conexiones[0] == Casilla.Conexion.NADA || conexiones[0] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[0] == Casilla.Conexion.PARED_ROTA){
      mapa[posY-1][posX].setTieneFuego(Casilla.Fuego.NADA);
    }
    //Casilla adyacente abajo
    if(conexiones[2] == Casilla.Conexion.NADA || conexiones[2] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[2] == Casilla.Conexion.PARED_ROTA){
      mapa[posY+1][posX].setTieneFuego(Casilla.Fuego.NADA);
    }
    //Casilla adyacente derecha
    if(conexiones[1] == Casilla.Conexion.NADA || conexiones[1] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[1] == Casilla.Conexion.PARED_ROTA){
      mapa[posY][posX+1].setTieneFuego(Casilla.Fuego.NADA);
    }
    //Casilla adyacente izquierda
    if(conexiones[3] == Casilla.Conexion.NADA || conexiones[3] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[3] == Casilla.Conexion.PARED_ROTA){
      mapa[posY][posX-1].setTieneFuego(Casilla.Fuego.NADA);
    }

    return mapa;
  }

}
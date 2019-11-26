package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.SFipa;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.HabitacionActualizada;

public class ColocarPuntosInteresPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero trata de colocar un PDI");

    // PDI tipo victima sin colocar
    int PDIVictima = (int) getBeliefbase().getBelief("PDIVictima").getFact();
    // PDI tipo falsa alarma sin colocar
    int PDIFalsaAlarma = (int) getBeliefbase().getBelief("PDIFalsaAlarma").getFact();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    // Mapa
    Casilla[][] mapa = t.getMapa();
    // Lista de jugadores
    ArrayList<Jugador> jugadores = t.getJugadores();

    // Si no quedan fichas por colocar
    if (PDIVictima + PDIFalsaAlarma == 0) {
      System.out.println("[ERROR] No quedan PDIs por colocar");
      return;
    }

    // Posiciones aleatorias para el nuevo PDI en el tablero
    int X = (int) (Math.random() * 8 + 1);
    int Y = (int) (Math.random() * 6 + 1);
    // Casilla en la posicion X e Y
    Casilla c = mapa[Y][X];

    // Mientras el número de PDI en el tablero sea menor a 3
    while (true) {

      // Hay bombero
      boolean hayBombero = false;
      for (Jugador j : jugadores) {
        if (j.getPosicion()[0] == Y && j.getPosicion()[1] == X) {
          hayBombero = true;
          break;
        }
      }

      // Se puede colocar...
      if (c.getPuntoInteres() == Casilla.PuntoInteres.NADA && c.tieneFuego() != Casilla.Fuego.FUEGO && !hayBombero) {

        // Si no queda de un tipo, se coloca del otro...
        if (PDIVictima == 0) {
          colocarFalsaAlarma(c, t);
          PDIFalsaAlarma--;
          break;
        } else if (PDIFalsaAlarma == 0) {
          colocarVictima(c, t);
          PDIVictima--;
          break;
        }

        // Si quedan de los dos tipos, de manera aleatoria...
        if (Math.random() < 0.5) {
          colocarFalsaAlarma(c, t);
          PDIFalsaAlarma--;
          break;
        } else {
          colocarVictima(c, t);
          PDIVictima--;
          break;
        }

      }

      // No se puede colocar, se siguen las flechas para encontrar una nueva posible casilla
      else {
        // La nueva casilla es la indica por la flecha de la casilla actual
        switch (c.getFlecha()) {
          case ARRIBA:
            Y++;
            break;
          case ARRIBA_DERECHA:
            X++;
            Y++;
            break;
          case DERECHA:
            X++;
            break;
          case ABAJO_DERECHA:
            X++;
            Y--;
            break;
          case ABAJO:
            Y--;
            break;
          case ABAJO_IZQUIERDA:
            X--;
            Y--;
            break;
          case IZQUIERDA:
            X--;
            break;
          case ARRIBA_IZQUIERDA:
            X--;
            Y++;
            break;
          case NADA:
            break;
        }
        // Se actualiza la casilla
        c = mapa[Y][X];
      }

    }

    // Una vez colocado el nuevo PDI, se actualizan las creencias
		getBeliefbase().getBelief("PDIVictima").setFact(PDIVictima);
		getBeliefbase().getBelief("PDIFalsaAlarma").setFact(PDIFalsaAlarma);
    getBeliefbase().getBelief("tablero").setFact(t);

    // Casillas en la habitación
    ArrayList<Casilla> habitacion = t.getHabitacion(c.getHabitacion());
    // Se oculta el PDI de aquellos PDI sin revelar aun
    for (Casilla c_: habitacion) {
      if (c_.puntoInteresOculto()) {
        c_.setPuntoInteres(Casilla.PuntoInteres.OCULTO);
      }
    }
    
    // Se informa a los jugadores en la habitación del PDI nuevo
    for (Jugador j: jugadores) {
      if (j.getHabitacion() == c.getHabitacion()) {
        System.out.println("[ACTUALIZACION] Se informa a " + j.getIdAgente() + " del nuevo PDI");
        // Se informa al jugador
        IMessageEvent mensaje = createMessageEvent("InformHabitacionActualizada");
        HabitacionActualizada habitacionPredicado = new HabitacionActualizada();
        habitacionPredicado.setCasillas(habitacion);
        mensaje.getParameterSet(SFipa.RECEIVERS).addValue(j.getIdAgente());
        mensaje.setContent(habitacionPredicado);
        sendMessage(mensaje);
      }
    }

  }

  public void colocarVictima(Casilla c, Tablero t) {
    System.out.println("[INFO] Se ha colocado un PDI de víctima");
    c.setPuntoInteres(Casilla.PuntoInteres.VICTIMA);
    c.setPuntoInteresOculto(true);
    t.setCasilla(c.getPosicion()[0], c.getPosicion()[1], c);
  }

  public void colocarFalsaAlarma(Casilla c, Tablero t) {
    System.out.println("[INFO] Se ha colocado un PDI de falsa alarma");
    c.setPuntoInteres(Casilla.PuntoInteres.FALSA_ALARMA);
    c.setPuntoInteresOculto(true);
    t.setCasilla(c.getPosicion()[0], c.getPosicion()[1], c);
  }

}
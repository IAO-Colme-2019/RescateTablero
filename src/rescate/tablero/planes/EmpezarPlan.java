package rescate.tablero.planes;


import jadex.adapter.fipa.SFipa;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;
import rescate.ontologia.conceptos.Tablero;
import rescate.ontologia.predicados.TurnoAsignado;

public class EmpezarPlan extends Plan {

  // Casillas con sus atributos de nuestro t.getMapa()
  // {con1, con2, con3, con4, flecha, apCamion, apAmbul, hab}
  public int[][][] modelo = new int[][][] {
      { { 0, 0, 0, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 1, 0 }, { 0, 0, 1, 0, 8, 0, 1, 0 },{ 0, 0, 3, 0, 8, 1, 0, 0 }, { 0, 0, 3, 0, 8, 1, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0 } },
      { { 0, 3, 0, 0, 8, 1, 0, 0 }, { 3, 0, 0, 3, 3, 0, 0, 1 }, { 3, 0, 0, 0, 4, 0, 0, 1 }, { 3, 2, 0, 0, 4, 0, 0, 1 }, { 3, 0, 0, 2, 4, 0, 0, 2 }, { 3, 3, 0, 0, 4, 0, 0, 2 }, { 1, 0, 0, 3, 4, 0, 0, 3 },{ 3, 0, 0, 0, 4, 0, 0, 3 }, { 3, 3, 0, 0, 5, 0, 0, 3 }, { 0, 0, 0, 3, 8, 0, 0, 0 } },
      { { 0, 3, 0, 0, 8, 1, 0, 0 }, { 0, 0, 0, 3, 2, 0, 0, 1 }, { 0, 0, 0, 0, 3, 0, 0, 1 }, { 0, 3, 3, 0, 6, 0, 0, 1 },{ 0, 0, 3, 3, 4, 0, 0, 2 }, { 0, 2, 3, 0, 4, 0, 0, 2 }, { 0, 0, 3, 2, 2, 0, 0, 3 },{ 0, 0, 3, 0, 5, 0, 0, 3 }, { 0, 3, 2, 0, 6, 0, 0, 3 }, { 0, 0, 0, 3, 8, 0, 0, 0 } },
      { { 0, 1, 0, 0, 8, 0, 1, 0 }, { 0, 0, 0, 1, 2, 0, 0, 1 }, { 0, 2, 0, 0, 0, 0, 0, 1 }, { 3, 0, 0, 2, 4, 0, 0, 4 },{ 3, 0, 0, 0, 6, 0, 0, 4 }, { 3, 0, 0, 0, 6, 0, 0, 4 }, { 3, 3, 0, 0, 6, 0, 0, 4 },{ 3, 0, 0, 3, 0, 0, 0, 5 }, { 2, 3, 0, 0, 6, 0, 0, 5 }, { 0, 0, 0, 3, 8, 0, 1, 0 } },
      { { 0, 3, 0, 0, 8, 0, 1, 0 }, { 0, 0, 3, 3, 2, 0, 0, 1 }, { 0, 3, 3, 0, 4, 0, 0, 1 }, { 0, 0, 3, 3, 2, 0, 0, 4 },{ 0, 0, 2, 0, 2, 0, 0, 4 }, { 0, 0, 3, 0, 2, 0, 0, 4 }, { 0, 2, 3, 0, 0, 0, 0, 4 },{ 0, 0, 3, 2, 4, 0, 0, 5 }, { 0, 1, 3, 0, 6, 0, 0, 5 }, { 0, 0, 0, 1, 8, 0, 1, 0 } },
      { { 0, 3, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 3, 2, 0, 0, 6 }, { 3, 0, 0, 0, 1, 0, 0, 6 }, { 3, 0, 0, 0, 6, 0, 0, 6 },{ 2, 0, 0, 0, 0, 0, 0, 6 }, { 3, 3, 0, 0, 0, 0, 0, 6 }, { 3, 0, 0, 3, 2, 0, 0, 7 },{ 3, 3, 0, 0, 7, 0, 0, 7 }, { 3, 3, 0, 3, 6, 0, 0, 8 }, { 0, 0, 0, 3, 8, 1, 0, 0 } },
      { { 0, 3, 0, 0, 8, 0, 0, 0 }, { 0, 0, 3, 3, 1, 0, 0, 6 }, { 0, 0, 3, 0, 0, 0, 0, 6 }, { 0, 0, 1, 0, 0, 0, 0, 6 },{ 0, 0, 3, 0, 0, 0, 0, 6 }, { 0, 2, 3, 0, 0, 0, 0, 6 }, { 0, 0, 3, 2, 0, 0, 0, 7 },{ 0, 2, 3, 0, 0, 0, 0, 7 }, { 0, 3, 3, 2, 7, 0, 0, 8 }, { 0, 0, 0, 3, 8, 1, 0, 0 } },
      { { 0, 0, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 0, 8, 1, 0, 0 }, { 3, 0, 0, 0, 8, 1, 0, 0 }, { 1, 0, 0, 0, 8, 0, 1, 0 },{ 3, 0, 0, 0, 8, 0, 1, 0 }, { 3, 0, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 0, 8, 0, 0, 0 },{ 3, 0, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 0, 8, 0, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0 } }, 
    };

  private Tablero t;

  public void body() {

    System.out.println("[PLAN] Se inicializa el tablero");

    // Tablero
    t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    t.setMapa(new Casilla[modelo.length][modelo[0].length]);

    // Inicializacion de los atributos de cada casilla siguendo el modelo que hemos
    // definido
    for (int i = 0; i < modelo.length; i++) {
      for (int j = 0; j < modelo[i].length; j++) {
        t.getMapa()[i][j] = new Casilla();
        t.getMapa()[i][j].setPosicion(new int[] { j, i });
        Casilla.Conexion[] c = Casilla.Conexion.values();
        t.getMapa()[i][j].setConexiones(new Casilla.Conexion[] { c[modelo[i][j][0]], c[modelo[i][j][1]], c[modelo[i][j][2]], c[modelo[i][j][3]] });
        t.getMapa()[i][j].setFlecha(Casilla.Direccion.values()[modelo[i][j][4]]);
        t.getMapa()[i][j].setEsAparcamientoCamion(modelo[i][j][5] == 1);
        t.getMapa()[i][j].setEsAparcamientoAmbulancia(modelo[i][j][6] == 1);
        t.getMapa()[i][j].setHabitacion(modelo[i][j][7]);
        t.getMapa()[i][j].setTieneFuego(Casilla.Fuego.NADA);
        t.getMapa()[i][j].setPuntoInteres(Casilla.PuntoInteres.NADA);
      }
    }

    // 3 fuegos con foco de calor y una explosion en cada uno
    for (int i = 0; i < 3; i++) {
      // Posiciones aleatorias para el fuego y el foco de calor
      int X = (int) (Math.random() * 8 + 1);
      int Y = (int) (Math.random() * 6 + 1);
      // Casilla en la posicion X e Y
      Casilla casilla = t.getMapa()[Y][X];
      if (casilla.tieneFuego() == Casilla.Fuego.NADA) {
        t.getMapa()[Y][X].setTieneFuego(Casilla.Fuego.FUEGO);
        t.getMapa()[Y][X].setTieneFocoCalor(true);
        explosion(X, Y);
      } else {
        i--;
      }
    }

    // Colocamos 3 PDI en posiciones random
    ColocarPDI();
    ColocarPDI();
    ColocarPDI();

    // Colocamos 4 materias peligrosas
    for (int i = 0; i < 4; i++) {
      // Posiciones aleatorias para la materia peligrosa
      int X = (int) (Math.random() * 8 + 1);
      int Y = (int) (Math.random() * 6 + 1);
      // Casilla en la posicion X e Y
      Casilla casilla = t.getMapa()[Y][X];
      if (casilla.tieneFuego() == Casilla.Fuego.NADA)
        t.getMapa()[Y][X].setTieneMateriaPeligrosa(true);
      else
        i--;
    }

    // Colocamos el camion y la ambulancia en uno de los 4 aparcamientos de manera
    // aleatoria
    switch ((int) (Math.random() * 4)) {
      case 0:
        t.getMapa()[0][7].setCamionBomberos(true);
        t.getMapa()[0][8].setCamionBomberos(true);
        break;
      case 1:
        t.getMapa()[5][9].setCamionBomberos(true);
        t.getMapa()[6][9].setCamionBomberos(true);
        break;
      case 2:
        t.getMapa()[7][1].setCamionBomberos(true);
        t.getMapa()[7][2].setCamionBomberos(true);
        break;
      case 3:
        t.getMapa()[1][0].setCamionBomberos(true);
        t.getMapa()[2][0].setCamionBomberos(true);
        break;
      default:
        break;
    }
    switch ((int) (Math.random() * 4)) {
      case 0:
        t.getMapa()[0][5].setAmbulancia(true);
        t.getMapa()[0][6].setAmbulancia(true);
        break;
      case 1:
        t.getMapa()[3][9].setAmbulancia(true);
        t.getMapa()[4][9].setAmbulancia(true);
        break;
      case 2:
        t.getMapa()[7][3].setAmbulancia(true);
        t.getMapa()[7][4].setAmbulancia(true);
        break;
      case 3:
        t.getMapa()[3][0].setAmbulancia(true);
        t.getMapa()[4][0].setAmbulancia(true);
        break;
      default:
        break;
    }

    // Posicion inicial de jugadores
    for (int i = 0; i < t.getJugadores().size(); i++) {
      switch ((int) (Math.random() * 4)) {
        case 0:
          t.getJugadores().get(i).setPosicion(new int[] { 6, 0 });
          break;
        case 1:
          t.getJugadores().get(i).setPosicion(new int[] { 9, 4 });
          break;
        case 2:
          t.getJugadores().get(i).setPosicion(new int[] { 3, 7 });
          break;
        case 3:
          t.getJugadores().get(i).setPosicion(new int[] { 0, 3 });
          break;
        default:
          break;
      }
      t.getJugadores().get(i).setHabitacion(0);
      t.getJugadores().get(i).setLlevandoVictima(Jugador.LlevandoVictima.NO);
      t.getJugadores().get(i).setRol(Jugador.Rol.NINGUNO);
    }

    // Actualizamos el belief del tablero
    getBeliefbase().getBelief("tablero").setFact(t);
    getBeliefbase().getBelief("empezar").setFact(false);    
    getBeliefbase().getBelief("PDITablero").setFact(3);


    // Se informa también al jugador al que le toca ahora jugar
    IMessageEvent respuesta = createMessageEvent("Inform_Turno_Asignado");
    TurnoAsignado predicado = new TurnoAsignado();
    predicado.setHabitacion(t.getHabitacion(0));
    respuesta.setContent(predicado);
    respuesta.getParameterSet(SFipa.RECEIVERS).addValue(t.getJugadores().get(0).getIdAgente());
    sendMessage(respuesta);
  }

  // Explosion arriba, derecha, abajo e izquierda dada una casilla[X, Y]
  private void explosion(int X, int Y) {
    explosion(X, Y, 0);
    explosion(X, Y, 1);
    explosion(X, Y, 2);
    explosion(X, Y, 3);
  }

  // Explosion en una direccion desde una casilla[X, Y]
  private void explosion(int X, int Y, int direccion) {

    // Posicion nueva casilla
    int X_ = X;
    int Y_ = Y;
    // Direccion opuesta para dañar conexion
    int direccion_ = 0;

    // Dependiendo de la direccion
    switch (direccion) {
      case 0:
        Y_ = Y - 1;
        direccion_ = 2;
        break;
      case 1:
        X_ = X + 1;
        direccion_ = 3;
        break;
      case 2:
        Y_ = Y + 1;
        direccion_ = 0;
        break;
      case 3:
        X_ = X - 1;
        direccion_ = 1;
        break;
    }

    // Si la casilla esta dentro de los limites
    if (Y > -1 && X > -1 && Y < t.getMapa().length && X < t.getMapa()[0].length) {
      // Si hay obstaculo, se daña y se para
      if (obstaculo(X, Y, direccion)) {
        // Si es una pared
        if (t.getMapa()[Y][X].getConexiones()[direccion] != Casilla.Conexion.PUERTA_CERRADA) {
          // Se reduce en uno los cubos de daño
          getBeliefbase().getBelief("cubosDanno").setFact((int) getBeliefbase().getBelief("cubosDanno").getFact() - 1);
        }
        t.getMapa()[Y][X].dannarConexion(direccion);
        t.getMapa()[Y_][X_].dannarConexion(direccion_);
        return;
      }
      // Si hay una puerta abierta, se daña y se continua
      if (t.getMapa()[Y][X].getConexiones()[direccion] == Casilla.Conexion.PUERTA_ABIERTA) {
        t.getMapa()[Y][X].dannarConexion(direccion);
        t.getMapa()[Y_][X_].dannarConexion(direccion_);
      }
      // Si la nueva casilla esta dentro de los limites
      if (Y_ > -1 && X_ > -1 && Y_ < t.getMapa().length && X_ < t.getMapa()[0].length) {
        // Si no hay fuego, se cambia a fuego y se para
        if (t.getMapa()[Y_][X_].tieneFuego() != Casilla.Fuego.FUEGO) {
          t.getMapa()[Y_][X_].setTieneFuego(Casilla.Fuego.FUEGO);
          return;
        }
        // Si hay fuego, se realiza una nueva explosion en la misma direccion
        explosion(X_, Y_, direccion);
      }
    }
  }

  // Devuelve si hay un obstaculo (pared sin romper o puerta cerrada) en la
  // direccion indicada de una casilla
  private boolean obstaculo(int X, int Y, int direccion) {
    return t.getMapa()[Y][X].getConexiones()[direccion] == Casilla.Conexion.PUERTA_CERRADA
        || t.getMapa()[Y][X].getConexiones()[direccion] == Casilla.Conexion.PARED
        || t.getMapa()[Y][X].getConexiones()[direccion] == Casilla.Conexion.PARED_SEMIRROTA;
  }

  private void ColocarPDI() {

    // Posiciones aleatorias para el nuevo PDI en el tablero
    int X = (int) (Math.random() * 8 + 1);
    int Y = (int) (Math.random() * 6 + 1);
    // Casilla en la posicion X e Y
    Casilla c = t.getMapa()[Y][X];

    // Se evitan posibles bucles infinitos (por las flechas)
    int maxIntentos = 11;
    int intentos = 0;

    // Hasta que se coloque el PDI
    while (true) {

      // Nuevo intento
      intentos++;

      // Si se ha entrado en un bucle infinito
      if (intentos > maxIntentos) {
        for (int i = 1; i < t.getMapa().length - 1; i++) {
          for (int j = 1; j < t.getMapa()[i].length - 1; j++) {
            // Se encuentra la primera casilla en la que sea viable poner el PDI
            Casilla c_ = t.getMapa()[i][j];
            if (c_.getPuntoInteres() == Casilla.PuntoInteres.NADA && c_.tieneFuego() != Casilla.Fuego.FUEGO) {
              // Se coloca el PDI (oculto y cuando se descubra se decidirá si es falsa alarma
              // o víctima)
              System.out.println("[INFO] Se ha colocado un PDI en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
              c.setPuntoInteres(Casilla.PuntoInteres.OCULTO);
              return;
            }
          }
        }
      }

      // Se puede colocar...
      if (c.getPuntoInteres() == Casilla.PuntoInteres.NADA && c.tieneFuego() != Casilla.Fuego.FUEGO) {

        // Se coloca el PDI (oculto y cuando se descubra se decidirá si es falsa alarma
        // o víctima)
        System.out.println("[INFO] Se ha colocado un PDI en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        c.setPuntoInteres(Casilla.PuntoInteres.OCULTO);
        break;

      }

      // No se puede colocar, se siguen las flechas para encontrar una nueva posible
      // casilla
      else {
        // La nueva casilla es la indica por la flecha de la casilla actual
        switch (c.getFlecha()) {
          case ARRIBA:
            Y--;
            break;
          case ARRIBA_DERECHA:
            X++;
            Y--;
            break;
          case DERECHA:
            X++;
            break;
          case ABAJO_DERECHA:
            X++;
            Y++;
            break;
          case ABAJO:
            Y++;
            break;
          case ABAJO_IZQUIERDA:
            X--;
            Y++;
            break;
          case IZQUIERDA:
            X--;
            break;
          case ARRIBA_IZQUIERDA:
            X--;
            Y--;
            break;
          case NADA:
            break;
        }
        // Se actualiza la casilla
        c = t.getMapa()[Y][X];
      }
    }
  }

}

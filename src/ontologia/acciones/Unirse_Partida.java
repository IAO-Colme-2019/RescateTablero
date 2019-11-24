package ontologia.acciones;

import ontologia.conceptos.*;

public class Unirse_Partida extends Accion {

        private Jugador jugador;

        public Unirse_Partida(){;}

        public Jugador getJugador(){
            return jugador;
        }

        public void setJugador(Jugador jugador){
            this.jugador = jugador;
        }

}

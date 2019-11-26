package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;

public class UnirJugadorPlan extends Plan {

  public void body(){
        IMessageEvent request = (IMessageEvent)getInitialEvent();

        System.out.println("Mensaje de request recibido");

        Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
        UnirsePartidaAccion rj = (UnirsePartidaAccion) request.getContent();
        Jugador player = new Jugador();
        player.setIdAgente((AgentIdentifier) request.getParameter("sender").getValue());
        System.out.println("Nuevo Jugador creado: "+player);
        
        /*Unirse_Partida rj3 = new Unirse_Partida();
		    rj3.setJugador(player);
        sendMessage(request.createReply("Agree_unirse_a_partida", rj3));*/

        /*for(int i=0; i< tablero.getJugadores().size();i++){

          if(tablero.getJugadores().get(i).getIdAgente().equals(player.getIdAgente())){            
            
            System.out.println(" Error al unir jugador a la partida, el jugador ya se encuentra en ella. ");
            return;
          }

        }
        tablero.getJugadores().add(player);*/

      // Se coge el numero de jugadores registrado y se aumenta en 1 unidad /

		  System.out.println("Jugador unido a la partida.");
    }

}


 

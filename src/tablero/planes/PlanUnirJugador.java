package tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import ontologia.acciones.*;
import ontologia.conceptos.*;

public class PlanUnirJugador extends Plan{
    public void body(){
        IMessageEvent request = (IMessageEvent)getInitialEvent();

        System.out.println("Mensaje de request recibido");

        Unirse_Partida rj = (Unirse_Partida) request.getContent();
        Jugador player = new Jugador();
        player.setIdAgente((AgentIdentifier) request.getParameter("sender").getValue());
        System.out.println("Nuevo Jugador creado: "+player);
        
        /*Unirse_Partida rj3 = new Unirse_Partida();
		rj3.setJugador(player);
		sendMessage(request.createReply("Agree_unirse_a_partida", rj3));*/

        /* Se coge el numero de jugadores registrado y se aumenta en 1 unidad */
		System.out.println("Jugador unido a la partida.");
    }
}
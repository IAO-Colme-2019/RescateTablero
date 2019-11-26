package rescate.tablero.planes;

import java.util.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.JugadorUnidoPredicado;

public class UnirJugadorPlan extends Plan {

  public void body(){
        IMessageEvent request = (IMessageEvent)getInitialEvent();
        JugadorUnidoPredicado jugadorUnido = new JugadorUnidoPredicado();
        System.out.println("Mensaje de request recibido");

        Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
        UnirsePartidaAccion rj = (UnirsePartidaAccion) request.getContent();
        boolean existe = false; 
        //player.setIdAgente((AgentIdentifier) request.getParameter("sender").getValue());
        
        AgentIdentifier jugadorId = (AgentIdentifier) request.getParameter("sender").getValue();
        
        if(tablero.getJugadores() != null){

          for (int i = 0; i < tablero.getJugadores().size(); i++) {
            if (tablero.getJugadores().get(i).getIdAgente().equals(jugadorId)) {
             existe = true;
            }
         }

        }else{
          tablero.setJugadores(new ArrayList<Jugador>());
        }
        
        
        IMessageEvent msg = createMessageEvent("Agree_Unirse_a_la_partida");
        if(existe){
            msg.setContent("Refuse_Unirse_a_la_partida");
            System.out.println(" Error al unir jugador a la partida, el jugador ya se encuentra en ella. ");

        }else{

            Jugador jugador = new Jugador();

            jugador.setIdAgente(jugadorId);
            tablero.getJugadores().add(jugador); 
            
            getBeliefbase().getBelief("tablero").setFact(tablero);

            System.out.println("Tablero informa que el jugador se ha unido a la partida.");
                    
        }

        msg.setContent(jugadorUnido);
        msg.getParameterSet(SFipa.RECEIVERS).addValue(jugadorId);
        sendMessage(msg);		 
    }


}


 

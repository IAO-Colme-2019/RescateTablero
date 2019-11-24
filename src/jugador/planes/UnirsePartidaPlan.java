package jugador.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import ontologia.acciones.*;
import ontologia.conceptos.*;

public class UnirsePartidaPlan extends Plan {

	public void body() {
        AgentIdentifier agentId = new AgentIdentifier();
        Jugador jugador = new Jugador();

        jugador.setIdAgente(agentId);

        ServiceDescription sd = new ServiceDescription();
        sd.setName("tablero");
        sd.setType("agente");

        AgentDescription dfaDesc = new AgentDescription();
        dfaDesc.addService(sd);

        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(-1);

        IGoal ft = createGoal("df_search");
        ft.getParameter("description").setValue(dfaDesc);
        ft.getParameter("constraints").setValue(sc);
        dispatchSubgoalAndWait(ft);
        System.out.println("Jugador busca tablero...");

        AgentDescription[] result = (AgentDescription[])ft.getParameterSet("result").getValues();

        if(result.length > 0){
            System.out.println("Jugador pide unirse a la partida");
            AgentIdentifier tablero = result[0].getName();
            //Solicitud unirse
            IMessageEvent msgsend = createMessageEvent("RequestUnirsePartidaMsg");
            Unirse_Partida accion = new Unirse_Partida();
            accion.setJugador(jugador);
            msgsend.setContent(accion);
            msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            sendMessage(msgsend);
            System.out.println("Jugador envia mensaje");
        }else{
            System.out.println("Tablero no encontrado");
        }
	}

}

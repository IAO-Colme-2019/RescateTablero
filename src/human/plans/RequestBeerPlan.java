package human.plans;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;
import jadex.runtime.IBelief;
import ontologia.acciones.BeberCerveza;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.adapter.fipa.AgentDescription;
import jadex.adapter.fipa.SearchConstraints;
import jadex.adapter.fipa.ServiceDescription;
import java.util.Random;


public class RequestBeerPlan extends Plan
{
	public void body()
	{
		System.out.println ("humano necesita cerveza...");
		// ya hemos acabado la cerveza que estabamos bebiendo
		// buscamos en el DF al agente robot
		ServiceDescription sd = new ServiceDescription();
		sd.setType("beer");
		sd.setName("robot");
		AgentDescription dfadesc = new AgentDescription();
		dfadesc.addService(sd);
		SearchConstraints	sc	= new SearchConstraints();
		sc.setMaxResults(-1);
		IGoal ft = createGoal("df_search");
		ft.getParameter("description").setValue(dfadesc);
		ft.getParameter("constraints").setValue(sc);
		dispatchSubgoalAndWait(ft);
		System.out.println ("humano busca robot...");
		AgentDescription[] result	= (AgentDescription[])ft.getParameterSet("result").getValues();
		if (result.length>0)
		{
			System.out.println ("humano pide cerveza...");
			AgentIdentifier robot = result[new Random().nextInt(result.length)].getName();			
			// le pedimos nueva cerveza
			IMessageEvent msgsend = createMessageEvent("RequestBeberCervezaMsg");
			BeberCerveza accion= new BeberCerveza();
			msgsend.setContent(accion);
			msgsend.getParameterSet(SFipa.RECEIVERS).addValue(robot);
			sendMessage(msgsend); 
		}
		else
			System.out.println("robot no encontrado");

		}
}

package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.GUI.GUI;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class IniciarGUIPlan extends Plan {

  @Override
  public void body(){
    System.out.println("panasdf");
    GUI GUI = (GUI) getBeliefbase().getBelief("gui").getFact();
    GUI.launch();
  }
}
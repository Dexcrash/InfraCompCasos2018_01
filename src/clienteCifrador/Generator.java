package clienteCifrador;

import core.LoadGenerator;
import core.Task;

public class Generator {
	
	private LoadGenerator generator;
	 
  public Generator(int pnTrans, int pmS)
   {
     Task work = createTask();
     int numberOfTasks = pnTrans;
    int gapBetweenTasks = pmS;
     this.generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work, gapBetweenTasks);
    this.generator.generate();
 }
  private Task createTask()
  {
    return new Cliente();
   }
 
}

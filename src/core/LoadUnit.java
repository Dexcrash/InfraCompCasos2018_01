package core;

   
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoadUnit
/*    */   implements Runnable
/*    */ {
/*    */   private Task command;
/*    */   private int id;
/*    */   private long extraTimeGap;
/*    */   private boolean sync;
/*    */   
/*    */   public LoadUnit(Task commandP, int idP, long extraTimeGapP, boolean syncP)
/*    */   {
/* 41 */     this.command = commandP;
/* 42 */     this.id = idP;
/* 43 */     this.extraTimeGap = extraTimeGapP;
/* 44 */     this.sync = syncP;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 50 */     if (this.sync)
/*    */     {
/* 52 */       waitUntil();
/*    */     }
/* 54 */     this.command.execute();
/* 55 */     System.out.println("[LoadUnit " + this.id + "] [Executed at: " + new Date(System.currentTimeMillis()) + "]");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void waitUntil()
/*    */   {
/* 63 */     long born = System.currentTimeMillis();
/* 64 */     long waitMl = born + LoadGenerator.SYNC_GAP + this.extraTimeGap;
/*    */     
/* 66 */     Date wait = new Date(waitMl);
/*    */     
/* 68 */     System.out.println("[LoadUnit" + this.id + "] [Waiting Until Sync: " + wait.toString() + "**]");
/*    */     
/* 70 */     boolean isTheTime = false;
/* 71 */     while (!isTheTime)
/*    */     {
/* 73 */       if (new Date(System.currentTimeMillis()).toString().equals(wait.toString()))
/*    */       {
/* 75 */         isTheTime = true;
/*    */       }
/*    */     }
/*    */   }
/*    */ }



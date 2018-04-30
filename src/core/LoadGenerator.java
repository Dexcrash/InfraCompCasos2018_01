/*     */ package core;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadGenerator
/*     */ {
/*  25 */   public static int SYNC_GAP = 5000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int executorsNumber;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ExecutorService executors;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int loadUnits;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Task unit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long timeGap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoadGenerator(String nameP, int executorsNumberP, int loadUnitsP, Task unitP, long timeGapP)
/*     */   {
/*  67 */     this.name = nameP;
/*  68 */     this.executorsNumber = executorsNumberP;
/*  69 */     this.executors = Executors.newFixedThreadPool(executorsNumberP);
/*  70 */     this.loadUnits = loadUnitsP;
/*  71 */     this.unit = unitP;
/*  72 */     this.timeGap = timeGapP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoadGenerator(String nameP, int loadUnitsP, Task unitP, long timeGapP)
/*     */   {
/*  86 */     this.name = nameP;
/*  87 */     this.executorsNumber = loadUnitsP;
/*  88 */     this.executors = Executors.newFixedThreadPool(this.executorsNumber);
/*  89 */     this.loadUnits = loadUnitsP;
/*  90 */     this.unit = unitP;
/*  91 */     this.timeGap = timeGapP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generate()
/*     */   {
/*  99 */     for (int i = 0; i < this.loadUnits; i++)
/*     */     {
/* 101 */       boolean sync = false;
/* 102 */       if (this.timeGap == 0L)
/*     */       {
/* 104 */         sync = true;
/*     */       }
/* 106 */       LoadUnit unidad = new LoadUnit(this.unit, i, this.timeGap * i, sync);
/* 107 */       this.executors.execute(unidad);
/*     */       
/*     */       try
/*     */       {
/* 111 */         Thread.sleep(this.timeGap);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 115 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getSYNC_GAP()
/*     */   {
/* 126 */     return SYNC_GAP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setSYNC_GAP(int SYNC_GAP_P)
/*     */   {
/* 135 */     SYNC_GAP = SYNC_GAP_P;
/*     */   }
/*     */ }



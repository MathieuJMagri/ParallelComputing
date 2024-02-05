package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

//This version of DiningPhilosophers creates a deadlock situation during execution.
public class DiningPhilosophers {

  public static void main(String[] args) {

    int numberOfPhilosophers = 5;
    Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
    Chopstick[] chopsticks = new Chopstick[numberOfPhilosophers];
    
    //Create Chopsticks for the Philosophers
    for (int i = 0; i < chopsticks.length; i++) {
      chopsticks[i] = new Chopstick();
      
    }
    
    //for i < philosopher number -> label chopsticks for each philosopher and then start thread
    for (int i = 0; i < philosophers.length; i++) {
      
      
      //Uncomment below for deadlock to fix the deadlock situation
      //if(i == 0) {
      // philosophers[i] = new Philosopher(chopsticks[i%(numberOfPhilosophers)], chopsticks[(i+1)%numberOfPhilosophers], i, 0);
      //}else {
        philosophers[i] = new Philosopher(chopsticks[(i+1)%numberOfPhilosophers], chopsticks[i%(numberOfPhilosophers)],  i, 0);
      //}
      
      Thread t = new Thread(philosophers[i]);
      t.start();
      
    }

  }

  //A Philosopher can do one of three things, it can either be thinking, waiting or eating.
  public static class Philosopher implements Runnable {
    
    //The left and right chopstick for each Philosopher
    private Chopstick ChopstickLeft = new Chopstick(); 
    private Chopstick ChopstickRight = new Chopstick(); 
    
    //An id for each philosopher
    private int id;
    
    //The number of times a philosopher gets to eat
    private int numberOfMeals;
    
    public Philosopher(Chopstick ChopstickRight, Chopstick ChopstickLeft, int id, int numberOfMeals) {
      this.ChopstickLeft = ChopstickLeft;
      this.ChopstickRight = ChopstickRight;
      this.id = id;
      this.numberOfMeals = numberOfMeals;
    }


    @Override
    public void run() {
      
      for(int i = 0; i < 50; i++) {
        
        try {
          
          //Philosopher is thinking
          Thinking();
          
          //Take the lock and then pick up the chopstick
          this.ChopstickRight.rel.lock();
          PickUpRightChopstick();
          
        //Take the lock and then pick up the chopstick
          this.ChopstickLeft.rel.lock();
          PickUpLeftChopstick();
          
          //Philosopher is eating
          Eating();
          this.numberOfMeals++;
          
        
      }finally {
        
      //Unlock the lock and then drop the chopstick
        this.ChopstickLeft.rel.unlock();
        DropLeftChopstick();
        
        
      //Unlock the lock and then drop the chopstick
        this.ChopstickRight.rel.unlock();
        DropRightChopstick();
    
        
      }
        
        //The below code is useful for slowing down each threads execution if that is needed.
//        try {
//          Thread.sleep(200);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
        
        if(i == 49) {
          System.out.println("Philosopher #" + this.id + " has ate " + this.numberOfMeals + " times");
        }
        
      }
      

    }
    
    public void PickUpLeftChopstick(){
      System.out.println("Philosopher #" + this.id + " picked up his left chopstick!");
    }
    
    public void PickUpRightChopstick(){
      System.out.println("Philosopher #" + this.id + " picked up his right chopstick!");
    }
    
    public void DropLeftChopstick(){
      System.out.println("Philosopher #" + this.id + " dropped his left chopstick!");
    }
    
    public void DropRightChopstick(){
      System.out.println("Philosopher #" + this.id + " dropped his right chopstick!");
    }
    
    public void Eating(){
      System.out.println("Philosopher #" + this.id + " is eating!");
    }
    
    public void Thinking(){
      System.out.println("Philosopher #" + this.id + " is thinking!");
    }



  }
  
  public static class Chopstick {
    
    //By putting false, the lock will be awarded arbitrarily, this is to create a starvation effect which will be corrected.
    ReentrantLock rel = new ReentrantLock(false);
    
    public Chopstick() {

    }



  }
  
  

}

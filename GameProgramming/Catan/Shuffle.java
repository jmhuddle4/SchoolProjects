/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.Random;

/**
 *
 * @author Max
 */
public class Shuffle {
    
    public int[] ar;
    
    public Shuffle(int[] ar) {
        this.ar = ar;
    }
    
    // Implementing Fisherâ€“Yates shuffle
    public void shuffleArray(int[] ar)
      {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
          int index = rnd.nextInt(i + 1);
          // Simple swap
          int a = ar[index];
          ar[index] = ar[i];
          ar[i] = a;
        }
      }
}

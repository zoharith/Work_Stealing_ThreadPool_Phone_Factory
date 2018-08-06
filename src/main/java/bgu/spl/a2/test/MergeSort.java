/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] arrToMerge;
    public MergeSort(int[] array) {
        this.arrToMerge = array;
    }

    
    @Override
    protected void start() {
    	
        if(arrToMerge.length>1){
        int [] Larr=Arrays.copyOfRange(this.arrToMerge, 0, this.arrToMerge.length/2);
        int [] Rarr=Arrays.copyOfRange(this.arrToMerge, this.arrToMerge.length/2, this.arrToMerge.length);
        MergeSort left=new MergeSort(Larr);
        MergeSort right=new MergeSort(Rarr);
        LinkedList<MergeSort> tasksOfMergeSort=new LinkedList<MergeSort>();
        tasksOfMergeSort.add(left);
        tasksOfMergeSort.add(right);
        this.spawn(left);
        this.spawn(right);
        this.whenResolved(tasksOfMergeSort, ()->{
            int[] resultOfTheLeftArr=tasksOfMergeSort.get(0).getResult().get();        
            int[] resultOfTheRightArr=tasksOfMergeSort.get(1).getResult().get();  
            this.complete(merge(resultOfTheLeftArr,resultOfTheRightArr));
        });
        }
        else
            this.complete(arrToMerge);
       
    }
    public static void main(String[] args) throws InterruptedException {
  
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int n = 10; //you may check on different number of elements if you like
        int[] array = new Random().ints(n).toArray();
        MergeSort task = new MergeSort(array);
        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
           System.out.println(Arrays.toString(task.getResult().get()));
           
            l.countDown();
        });
        l.await();
        pool.shutdown();
    

}
    /** @merge the known merge sort method   **/
    public int[] merge(int [] left, int [] right){
        int[] result=new int[left.length+right.length];
        int firstIndex=0;
        int socondIndex=0;
        int tmpIndex=0;
        while(tmpIndex<result.length){  
            if((firstIndex<left.length)&& (socondIndex<right.length) && (left[firstIndex]<right[socondIndex])){
                result[tmpIndex]=left[firstIndex];
                tmpIndex++;
                firstIndex++;
               
            }
            else if((firstIndex<left.length) && (socondIndex<right.length) && (left[firstIndex]>=right[socondIndex])){
                result[tmpIndex]=right[socondIndex];
                tmpIndex++;              
                socondIndex++;
            }
            else if(socondIndex>=right.length){
               
                result[tmpIndex]=left[firstIndex];
                tmpIndex++;
                firstIndex++;
               
            }
            else if(firstIndex>=left.length){
         	   result[tmpIndex]=right[socondIndex];
         	   tmpIndex++;
         	   socondIndex++;
            }
        }
        return result;
 }
 


}
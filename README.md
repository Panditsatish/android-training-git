# android-training-git
This is training part about git command 
this is test repo for git command
package com.example.helloworld;
public class Overloading {
    public void Overloading(int a , int b) {
        System.out.println("sum is = " + (a+b));
    }
   void  Overloading(int a , int b,int c) {
        System.out.println("sum is = " + (a+b+c));
    }
    void Overloading(float a , float b) {
        System.out.println("sum is = " + (a+b));
    }
    public static void main(String [] args){
        Overloading sc= new Overloading();
        sc.Overloading(12,23);
        sc.Overloading(12,34,23);
        sc.Overloading(12f,23f);



    }
}

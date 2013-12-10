/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author Uday
 */
public class Kaggle {
static HashMap<Integer, Double> avgT = new HashMap<Integer, Double>();
static HashMap<Integer, Double> avgx = new HashMap<Integer, Double>();
static HashMap<Integer, Double> avgy = new HashMap<Integer, Double>();
static HashMap<Integer, Double> avgz = new HashMap<Integer, Double>();

static HashMap<Integer, Double> varT = new HashMap<Integer, Double>();
static HashMap<Integer, Double> varx = new HashMap<Integer, Double>();
static HashMap<Integer, Double> vary = new HashMap<Integer, Double>();
static HashMap<Integer, Double> varz = new HashMap<Integer, Double>();

static HashMap<Integer, Integer> cnt = new HashMap<Integer, Integer>();

static HashMap<Integer, Double> avgOT = new HashMap<Integer, Double>();
static HashMap<Integer, Double> avgOx = new HashMap<Integer, Double>();
static HashMap<Integer, Double> avgOy = new HashMap<Integer, Double>();
static HashMap<Integer, Double> avgOz = new HashMap<Integer, Double>();
static HashMap<Integer, Integer> Ocnt = new HashMap<Integer, Integer>();
static Double T0=86400000.0;
static long totalcnt;
    /**
     * @param args the command line arguments
     */
    public static void trainData(){
        
        FileInputStream ft = null;
        try {
            File file = new File("train.csv");
            ft = new FileInputStream(file);
            DataInputStream in = new DataInputStream(ft);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strline;
            String[] parts;
            Double T;
            long counter=0;
            double x,y,z;
            int dID;
            
            strline = br.readLine();
            while((strline = br.readLine()) != null){
                parts = strline.split(",");
                T=(Double.parseDouble(parts[0]) % T0);
                x=Double.parseDouble(parts[1]);
                y=Double.parseDouble(parts[2]);
                z=Double.parseDouble(parts[3]);
                dID=Integer.parseInt(parts[4]);
                int tmp;
                double frac1;
                if(cnt.containsKey(dID)){
                    tmp=cnt.get(dID);
                    frac1=1.0/(tmp+1);
                    cnt.put(dID,tmp+1);
                    avgT.put(dID,(frac1*T)+((1-frac1)*avgT.get(dID)) );
                    avgx.put(dID,(frac1*x)+((1-frac1)*avgx.get(dID)) );
                    avgy.put(dID,(frac1*y)+((1-frac1)*avgy.get(dID)) );
                    avgz.put(dID,(frac1*z)+((1-frac1)*avgz.get(dID)) );
                    varT.put(dID,(frac1*T*T)+((1-frac1)*varT.get(dID)) );
                    varx.put(dID,(frac1*x*x)+((1-frac1)*varx.get(dID)) );
                    vary.put(dID,(frac1*y*y)+((1-frac1)*vary.get(dID)) );
                    varz.put(dID,(frac1*z*z)+((1-frac1)*varz.get(dID)) );
                    
                }
                else{
                    cnt.put(dID,1);
                    avgT.put(dID, T);
                    avgx.put(dID, x);
                    avgy.put(dID, y);
                    avgz.put(dID, z);
                    varT.put(dID, T*T);
                    varx.put(dID, x*x);
                    vary.put(dID, y*y);
                    varz.put(dID, z*z);
                }
                counter++;
                if(counter%1000000==0) System.out.println("Trained "+counter);
            }       
            totalcnt=counter;
            System.out.println("Total datapoints used in training: "+counter);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ft.close();
            } catch (IOException ex) {
                Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    
    public static void testData(){
        try {
        FileInputStream ft2 = null;
            File test = new File("test.csv");
            ft2 = new FileInputStream(test);
            DataInputStream in2 = new DataInputStream(ft2);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
            int flag=-1;
            String strline;
            String[] parts;
            strline = br2.readLine();
            //Double avgOT=0.0,avgOx=0.0,avgOy=0.0,avgOz=0.0,
            Double frac1;
            //int Ocnt=0;
            int counter=0;
            while((strline = br2.readLine()) != null){
                counter++;
                if(counter%1000000==0) System.out.println(counter);
                parts = strline.split(",");
                if(Ocnt.containsKey(Integer.parseInt(parts[4]))){
                    frac1=1.0/(1+Ocnt.get(Integer.parseInt(parts[4])));
                    Ocnt.put(Integer.parseInt(parts[4]), Ocnt.get(Integer.parseInt(parts[4]))+1);
                    avgOT.put(Integer.parseInt(parts[4]),frac1*(Double.parseDouble(parts[0]) % T0)+(1-frac1)*avgOT.get(Integer.parseInt(parts[4])));
                    avgOx.put(Integer.parseInt(parts[4]),frac1*Double.parseDouble(parts[1])+(1-frac1)*avgOx.get(Integer.parseInt(parts[4])));
                    avgOy.put(Integer.parseInt(parts[4]),frac1*Double.parseDouble(parts[2])+(1-frac1)*avgOy.get(Integer.parseInt(parts[4])));
                    avgOz.put(Integer.parseInt(parts[4]),frac1*Double.parseDouble(parts[3])+(1-frac1)*avgOz.get(Integer.parseInt(parts[4])));
                    
                    //Ocnt++;
                }
                else{
                    Ocnt.put(Integer.parseInt(parts[4]),1);
                    avgOT.put(Integer.parseInt(parts[4]),(Double.parseDouble(parts[0]) % T0));
                    avgOx.put(Integer.parseInt(parts[4]),Double.parseDouble(parts[1]));
                    avgOy.put(Integer.parseInt(parts[4]),Double.parseDouble(parts[2]));
                    avgOz.put(Integer.parseInt(parts[4]),Double.parseDouble(parts[3]));
                }
            }
            
            System.out.println("Total datapoints used in testing: "+counter);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    public static double getAverages(Integer Seq,int OID){
        double uT=avgT.get(OID);
        double ux=avgx.get(OID);
        double uy=avgy.get(OID);
        double uz=avgz.get(OID);
        double vT=varT.get(OID)-(uT*uT);
        double vx=varx.get(OID)-(ux*ux);
        double vy=vary.get(OID)-(uy*uy);
        double vz=varz.get(OID)-(uz*uz);
        double myT=avgOT.get(Seq);
        double myx=avgOx.get(Seq);
        double myy=avgOy.get(Seq);
        double myz=avgOz.get(Seq);
        return (cnt.get(OID)*Math.exp(-(((myx-ux)*(myx-ux))/(2*vx))-(((myy-uy)*(myy-uy))/(2*vy))-(((myz-uz)*(myz-uz))/(2*vz))-(((myT-uT)*(myT-uT))/(2*vT))))/(Math.sqrt(vx)*Math.sqrt(vy)*Math.sqrt(vz)*Math.sqrt(vT));
        //return Math.sqrt(((avgOx.get(Seq)-avgx.get(OID))*(avgOx.get(Seq)-avgx.get(OID)))+((avgOy.get(Seq)-avgy.get(OID))*(avgOy.get(Seq)-avgy.get(OID)))+((avgOz.get(Seq)-avgz.get(OID))*(avgOz.get(Seq)-avgz.get(OID))));
    }
    public static void main(String[] args) {
    try {
        totalcnt=0;
        trainData();
        testData();


        System.out.println();
        System.out.println();
        System.out.println();

        for (Map.Entry<Integer, Double> entry : avgT.entrySet()) { 
            System.out.println(entry.getKey() + " 1:" + entry.getValue()
                + " 2:" + avgx.get(entry.getKey())
                + " 3:" + avgy.get(entry.getKey())
                + " 4:" + avgz.get(entry.getKey())); 
        }

        System.out.println();
        System.out.println();
        System.out.println();


        for (Map.Entry<Integer, Double> entry : avgOT.entrySet()) { 
            System.out.println(entry.getKey() + " 1:" + entry.getValue()
                + " 2:" + avgOx.get(entry.getKey())
                + " 3:" + avgOy.get(entry.getKey())
                + " 4:" + avgOz.get(entry.getKey())); 
        }

        // //getAverages(100006,593);
        // int seq;
        // int id;
        // File test = new File("questions.csv");
        // FileInputStream ft = null;
        // ft = new FileInputStream(test);
        // DataInputStream in = new DataInputStream(ft);
        // BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // String strline;
        // String[] parts;
        // strline = br.readLine();
        // System.out.println("QuestionId,IsTrue");
        // while((strline = br.readLine()) != null){
        //     parts = strline.split(",");
        //     seq=Integer.parseInt(parts[1]);
        //     id=Integer.parseInt(parts[2]);
        //     System.out.println(Integer.parseInt(parts[0])+","+getAverages(seq,id));
        // }
    } 
    catch(Exception e){
        
    }
    // catch (FileNotFoundException ex) {
    //     Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
    // } catch (IOException ex) {
    //     Logger.getLogger(Kaggle.class.getName()).log(Level.SEVERE, null, ex);
    // }
    }
}

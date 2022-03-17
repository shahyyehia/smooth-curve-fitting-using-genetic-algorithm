/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softcomputingassignment2;

import static java.lang.Math.pow;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Nada Hossam
 */
public class SmoothCurve {

    private int number_of_points;
    private int degree;
    private int number_of_bits;
    private int population_number, t=1, T;
    private double min = -10;
    private double max = 10;
    private ArrayList<ArrayList<Double>> generation = new ArrayList<>();
    private ArrayList<ArrayList<Double>> matPool = new ArrayList<>();
    private ArrayList<pairs<Double, Double>> pair_of_points = new ArrayList<pairs<Double, Double>>();
    private ArrayList<ArrayList<Double>> newOffSpring = new ArrayList<>();
    private Random rand = new Random();
   // Map<Integer,Double> map ;
    Map<ArrayList<Double>,Double> bestFit = new HashMap();

    public SmoothCurve() {
    }

    ///23ml l population number random
    public SmoothCurve(int number_of_points, int degree) {
        this.number_of_points = number_of_points;
        this.degree = degree;
        this.number_of_bits = degree + 1;
        this.T = this.number_of_bits * 5000;
        this.population_number = rand.nextInt(100-number_of_points)+number_of_points;
    }

    ///azbot l random function
    void initializeGeneration() {
        for (int i = 0; i < population_number; i++) {
            double r;
            ArrayList<Double> gen = new ArrayList<>();

            for (int j = 0; j < number_of_bits; j++) {
                r = (Math.random()*(max-min))+min;
                gen.add(r);
            }
            generation.add(gen);
        }
    }

    void printGeneration() {
        System.out.println(generation.toString());
    }

    void add(double x, double y) {
        pair_of_points.add(new pairs(x, y));
        /*pair_of_points.add(new pairs(1, 5));
        pair_of_points.add(new pairs(2, 8));
        pair_of_points.add(new pairs(3, 13));
        pair_of_points.add(new pairs(4, 20));*/

    }

    double calculateTotalError(ArrayList<Double> array) {
        double totalError = 0;
        for (int i = 0; i < pair_of_points.size(); i++) {
            double y = 0;
            double error = 0;
            double yActual = pair_of_points.get(i).second;
            for (int j = 0; j < number_of_bits; j++) {
                y = y + array.get(j) * pow(pair_of_points.get(i).first, j);
            }
            error = pow(y - yActual, 2);
            totalError += error;
        }
        totalError = totalError / pair_of_points.size();
        return  totalError;
    }

    Map<Integer,Double> Fitness(ArrayList<ArrayList<Double>> gen ) {
        Map<Integer,Double> map = new HashMap();
        for (int i = 0; i < gen.size(); i++) {
            map.put(i, calculateTotalError(gen.get(i)));
        }
        return map;
    }

    void printMap(Map<Integer,Double> map) {
        System.out.println(map.toString());
    }

    void Selection(Map<Integer,Double> map) {
        Boolean[] visited = new Boolean[population_number];
        Arrays.fill(visited, Boolean.FALSE);
        Random index = new Random();
        int index1, index2;
        int c=0;
        int min=0;
        while (c<population_number) {
            for(int i =0;i<population_number/4;i++)
            {
                index1 = index.nextInt(population_number-1);

                if (map.get(index1) > map.get(min)) {
                    min=min;
                } else {
                    min=index1;
                }
            }
            matPool.add(generation.get(min));
            /*for(int i=0;i<visited.length;i++)
                System.out.print(visited[i]+" ");
            System.out.println("\n");*/
            c++;
           /* if (Arrays.asList(visited).contains(false)) {
                continue;
            } else {
                break;
            }*/
            
        }

        //System.out.println("mat: "+matPool);
    }
       //fix it
    void CrossOver() {
        ArrayList<Double> first = null, second = null, part1, part2;
        double pc = .6;
        for (int i = 0; i < matPool.size() - 1; i+=2) {
            int xc1 = (rand.nextInt(number_of_bits-1) + 1);
            int xc2 = (rand.nextInt(number_of_bits-1) + 1);
            //System.out.println("xc: "+xc1+"   "+xc2);
            double rc = (Math.random()*(1-0))+0;;
            first = matPool.get(i);
            second = matPool.get(i + 1);
            if (rc <= pc) {
                //System.out.println("in");
                if(xc1>xc2)
                {
                    ///take from xc1 to end
                    part1 = subArray(xc1,first.size(), first);
                    part2 = subArray(xc1,second.size(), second);
                    first = replaceArray(xc1,first.size(), first, part2);
                    second = replaceArray(xc1, second.size(), second, part1);
                    ///take from 0 to xc2
                    part1 = subArray(0,xc2, first);
                    part2 = subArray(0,xc2, second);
                    first = replaceArray(0, xc2, first, part2);
                    second = replaceArray(0, xc2, second, part1);
                }
                else if(xc2 > xc1)
                {
                    ///take from xc2 to end
                    part1 = subArray(xc2,first.size(), first);
                    part2 = subArray(xc2,second.size(), second);
                    first = replaceArray(xc2,first.size(), first, part2);
                    second = replaceArray(xc2, second.size(), second, part1);
                    ///take from 0 to xc1
                    part1 = subArray(0,xc1, first);
                    part2 = subArray(0,xc1, second);
                    first = replaceArray(0, xc1, first, part2);
                    second = replaceArray(0, xc1, second, part1);
                }

            }
            newOffSpring.add(first);
            newOffSpring.add(second);
        }
        if(matPool.size()%2 !=0)
            newOffSpring.add(matPool.get(matPool.size()-1));
        //System.out.println(newOffSpring);
    }

     ArrayList<Double> subArray(int start, int end, ArrayList<Double> list) {
        ArrayList<Double> finalArray = new ArrayList<>();
        for (int i = start; i < end; i++) {
            finalArray.add(list.get(i));
        }

        return finalArray;
    }

     ArrayList<Double> replaceArray(int start,int end, ArrayList<Double> list, ArrayList<Double> part) {
        for (int i = start, j = 0; i < end; i++, j++) {
            list.set(i, part.get(j));
        }

        return list;
    }

    void Mutation()
    {
        double pm=.01, rm, lx, ux, r1, r, y, ty, x ;
        int b=3;
        for(int i = 0 ;i < newOffSpring.size() ; i++)
        {   
            for(int j=0;j<number_of_bits;j++)
            {
                rm=(Math.random()*(1-0))+0;
                if(rm<=pm)
                {
                    lx = newOffSpring.get(i).get(j) - min;
                    ux = max - newOffSpring.get(i).get(j);
                    r1=(Math.random()*(1-0))+0;
                    if(r1<=.5)
                    {
                        y=lx;
                        r=(Math.random()*(1-0))+0;
                        ty = y * ( 1-Math.pow(r, Math.pow( (1 - t/T), b) ) );
                        x = newOffSpring.get(i).get(j) - ty;
                    }
                    else
                    {
                        y=ux;
                        r=(Math.random()*(1-0))+0;
                        ty = y * ( 1-Math.pow(r, Math.pow( (1 - t/T), b) ) );
                        x = newOffSpring.get(i).get(j) + ty;
                    }
                    //System.out.println(x);
                    newOffSpring.get(i).set(j, x);
                    
                    
                }
            }
        }
        //generation.clear();
        //generation=newOffSpring;
        //map.clear();
    }
    void Replacement()
    {
        ArrayList<ArrayList<Double>> newGens = new ArrayList<>();
        Map<Integer,Double> FitForMatPool = new HashMap();
        Map<Integer,Double> FitForOffSpring = new HashMap() ;
        FitForMatPool = Fitness(matPool);  
        FitForOffSpring = Fitness(newOffSpring);
        for(int i=0;i<matPool.size();i++)
        {
            if(FitForMatPool.get(i)>FitForOffSpring.get(i))
            {
                newGens.add(newOffSpring.get(i));
            }
            else
            {
                newGens.add(matPool.get(i));
            }
        }
        generation=newGens;
    }
    void getBestInPop(Map<Integer,Double> map)
    {
        //System.out.println("s: "+map);
        Double min = Collections.min(map.values());
        //System.out.println("min: "+min);
        for(Map.Entry<Integer,Double> entry : map.entrySet())
        {
            if(Objects.equals(entry.getValue(), min))
            {
                bestFit.put(generation.get(entry.getKey()), min);
                break;
                     
            }
        }
    }
    String getBestInAll()
    {
        Double min = Collections.min(bestFit.values());
        String res="";
        for(Map.Entry<ArrayList<Double>,Double> entry : bestFit.entrySet())
        {
            if(Objects.equals(entry.getValue(), min))
            {
                res=entry.getKey().toString()+" Error = "+new BigDecimal(min).toPlainString();
                break;
                     
            }
        }
        return res;
    }
    void run()
    {
        Map<Integer,Double> map= new HashMap();
        initializeGeneration();
        //printGeneration();
        ArrayList<Double> test= new ArrayList();
        /*test.add(0.429163);
        test.add(1.18487);
        test.add(-0.717967);
        test.add(0.0854301);*/
        //generation.add([0.429163,1.18487,-0.717967,0.0854301]);
        //generation.add(test);
        map = Fitness(generation);
        //add(1,1);
        while(t<T)
        {     
           // printGeneration();
            
            //printMap();
            //System.out.println("af");
            Selection(map);
            //System.out.println(matPool);
            CrossOver();
            //System.out.println(newOffSpring);
            Mutation();
            //printGeneration();
            //map = Fitness();
            Replacement();
            map=Fitness(generation);
            //System.out.println("Maaaaaaap "+map.size()+" ////// "+generation.size());
            getBestInPop(map);
            newOffSpring=new ArrayList();
            matPool=new ArrayList();
            t++;
        }
        //System.out.println("done");
    }
}

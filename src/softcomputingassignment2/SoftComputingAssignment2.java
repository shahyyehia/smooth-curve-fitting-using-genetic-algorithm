/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softcomputingassignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Nada Hossam
 */
public class SoftComputingAssignment2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        ArrayList<String> result = new ArrayList();
        int m, numberOFPoints, d;
        File myObj = new File("input-2.txt");
        Scanner myReader = new Scanner(myObj);
        SmoothCurve sc;
        m = Integer.parseInt(myReader.nextLine());
        
        
        for(int i=0 ; i < m ; i++)
        {
            String [] str = myReader.nextLine().split(" ");
            numberOFPoints=Integer.parseInt(str[0]);
            d=Integer.parseInt(str[1]);
            
            sc=new SmoothCurve(numberOFPoints,d);
            for(int j = 0;j<numberOFPoints;j++)
            {
                String [] str1 = myReader.nextLine().split(" ");
                sc.add( Double.parseDouble(str1[0]), Double.parseDouble(str1[1]));
            }
            //System.out.println("run");
            sc.run();
            result.add(sc.getBestInAll());
            //System.out.println(sc.getBestInAll());
        }
        myReader.close();
        FileWriter myWriter = new FileWriter("output.txt");
     
        for(int i = 0 ;i < result.size() ; i++)
        {
             myWriter.write("Case "+(i+1)+"\n"+result.get(i)+"\n");
        }
         myWriter.close();
    }
    
}

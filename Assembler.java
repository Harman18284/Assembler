import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Assembler {

    //This method converts the decimal number into 8-bit binary..
    public static String intToBinary(int a) {
        String temp = Integer.toBinaryString(a);
        while(temp.length() !=8){
            temp = "0"+temp;
        }
        return temp;
    }

    public static void appendStrToFile(String fileName,
                                       String str)
    {
        try {

            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName, true));
            out.write(str);
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public static void passtwo(ArrayList<Symbols> Symbol_Table,ArrayList<Literals> literal_Table,HashMap<String,String> opcodes) throws IOException{

        System.out.println("Pass 2 Started..");

        BufferedReader in = null;
        try{
            in = new BufferedReader( new FileReader("input.txt"));

            String l;
            boolean data_sec=false;


            while ((l = in.readLine()) != null) {

                l=l.replaceAll("\\s+"," ").trim();
                boolean extra=false;

                boolean comment=false;

                //checking for text section and data section...
                if(l.equals("Text Section")){
                    comment=true;
                }

                if(l.equals("Data Section")){
                    data_sec=true;
                }

                //comment check before each line..
                for(int it=0;it<l.length();it++){
                    char z = l.charAt(it);

                    if(z==';'){
                        comment=true;
                    }
                }

                String[] instruction=null;

                instruction=l.split(" ");

                for(int i=0;i<instruction.length;i++){

                    if(instruction[i].equals("START")){
                        comment=true;
                    }

                    if(i==0){
                        for(int k=0;k<Symbol_Table.size();k++){
                            if(instruction[i].equals(Symbol_Table.get(k).symbol)){
                                extra=true;
                            }
                        }
                    }

                    if(!comment && !data_sec){

                        System.out.print("\n");

                        for(int st=0;st<Symbol_Table.size();st++){
                            if(Symbol_Table.get(st).symbol.equals(instruction[i])){
                                System.out.print(Symbol_Table.get(st).address + " ");

                                if(!extra){
                                    appendStrToFile("machinecode.txt",intToBinary(Symbol_Table.get(st).address));
                                }

                            }

                            if(Symbol_Table.get(st).symbol.equals(instruction[i] + ":")){
                                System.out.print(Symbol_Table.get(st).address + " ");
                                if(!extra){
                                    appendStrToFile("machinecode.txt",intToBinary(Symbol_Table.get(st).address));
                                }
                            }



                        }

                        if(opcodes.containsKey(instruction[i])){
                            String a = opcodes.get(instruction[i]);
                            System.out.print(a + " ");
                            appendStrToFile("machinecode.txt",a);
                        }


                        try{
                            int x = -2;
                            x = Integer.parseInt(instruction[i]);
                            System.out.print(x);
                            appendStrToFile("machinecode.txt",intToBinary(Integer.parseInt(instruction[i])));
                        }
                        catch(NumberFormatException e){

                        }

                    }
                }

                System.out.println(Arrays.toString(instruction));
                appendStrToFile("machinecode.txt","\n");

            }

            System.out.println("------------------------------");
            System.out.println("Pass 2 Finished..");
            System.out.println("MachineCode.txt file is generated..");

        }
        finally{
            if (in != null)
                in.close(); // IOException
        }

    }

    public static void passone(ArrayList<Symbols> Symbol_Table,ArrayList<Literals> literal_Table,HashMap<String,String> opcodes) throws IOException {


        int location_counter;
        int length=1;

        location_counter=-1;
        boolean loc_counter_flag=true;

        //File Reading..
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader( new FileReader("input.txt"));

//                out = new PrintWriter( new FileWriter("output.txt"));

            String l;

            while ((l = in.readLine()) != null){ //IOException

                l=l.replaceAll("\\s+"," ").trim();

                boolean comment=false;

                //checking for text section and data section...
                if(l.equals("Text Section") || l.equals("Data Section")){
                    comment=true;
                }

                //comment check before each line..
                for(int it=0;it<l.length();it++){
                    char z = l.charAt(it);

                    if(z==';'){
                        comment=true;
                    }

                }

                String[] instruction=null;

                instruction=l.split(" ");

                for(int i=0;i<instruction.length;i++){


                    if(!comment){

                        //storing declared variable in symbol table..(DW Statement)
                        if(instruction[i].equals("DW")){
                            Symbol_Table.add(new Symbols(instruction[i-1],location_counter));
                            appendStrToFile("Symbol_Table.txt",instruction[i-1]);
                            appendStrToFile("Symbol_Table.txt",Integer.toString(location_counter));
                            appendStrToFile("Symbol_Table.txt","\n");
                        }

                        try{
                            if(instruction[i].equals("START") && instruction[i+1]!=null && loc_counter_flag){

                                location_counter+= Integer.parseInt(instruction[i+1]);
                                loc_counter_flag=false;
                            }
                        }
                        catch(IndexOutOfBoundsException e){
                            if(loc_counter_flag){
                                location_counter=-1;
                                loc_counter_flag=false;
                            }

                        }

                        //CLA checks
//                    try{
//                        if(instruction[i].equals("CLA") && instruction[i+1]!=null && loc_counter_flag){
//
//                            location_counter= Integer.parseInt(instruction[i+1]);
//                            loc_counter_flag=false;
//                        }
//                    }
//                    catch(IndexOutOfBoundsException e){
//                        if(loc_counter_flag){
//                            location_counter=0;
//                            loc_counter_flag=false;
//                        }
//
//                    }

                        //Iteration over characters of the words..
                        for(int j=0;j<instruction[i].length();j++){

                            //Label founder..
                            char c = instruction[i].charAt(j);
                            if(c==':'){
                                Symbol_Table.add(new Symbols(instruction[i],location_counter));
                                appendStrToFile("Symbol_Table.txt",instruction[i]);
                                appendStrToFile("Symbol_Table.txt",Integer.toString(location_counter));
                                appendStrToFile("Symbol_Table.txt","\n");
                            }

                        }
                    }

                }

//                System.out.println(Arrays.toString(instruction));


                if(!comment){
                    location_counter+=length;
                }

            }
        }finally {
            if (in != null)
                in.close(); // IOException

        }

        System.out.println("Pass One Completed");
        System.out.println("----- Symbols Table -----");
        for(int r=0;r<Symbol_Table.size();r++){
            System.out.println(Symbol_Table.get(r).symbol + " " + Symbol_Table.get(r).address);
        }

    }

    public static void main(String[] args) throws IOException {

        //initialize tables;
        ArrayList<Symbols> Symbol_Table = new ArrayList<Symbols>();
        ArrayList<Literals> literal_Table = new ArrayList<Literals>();

        HashMap<String,String> opcodes = new HashMap<String,String>();
        opcodes.put("CLA","0000");
        opcodes.put("LAC","0001");
        opcodes.put("SAC","0010");
        opcodes.put("ADD","0011");
        opcodes.put("SUB","0100");
        opcodes.put("BRZ","0101");
        opcodes.put("BRN","0110");
        opcodes.put("BRP","0111");
        opcodes.put("INP","1000");
        opcodes.put("DSP","1001");
        opcodes.put("MUL","1010");
        opcodes.put("DIV","1011");
        opcodes.put("STP","1100");

        passone(Symbol_Table,literal_Table,opcodes );
        passtwo(Symbol_Table,literal_Table,opcodes );

    }

}

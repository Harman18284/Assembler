import java.io.*;

import java.util.ArrayList;

public class Assembler {

    public static void passone() throws IOException {

        boolean more_input=true;
        String line,symbol,literal,opcode;
        int location_counter,length,value,type;
        final int end_statement=-2;

        location_counter=0;

        //initialize tables;
        ArrayList<Symbols> Symbol_Table = new ArrayList<Symbols>();
        ArrayList<Literals> literal_Table = new ArrayList<Literals>();

        //File Reading..
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader( new FileReader("input.txt"));

//                out = new PrintWriter( new FileWriter("output.txt"));

            String l;
            while ((l = in.readLine()) != null){ //IOException



            }
        }finally {
            if (in != null)
                in.close(); // IOException

        }

    }

    public static void main(String[] args) {



    }

}

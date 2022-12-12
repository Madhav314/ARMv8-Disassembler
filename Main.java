import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static HashMap<Integer, String> convert = new HashMap<>();
    public static HashMap<Integer, String> cond = new HashMap<>();
    public static ArrayList<String> instructions = new ArrayList<>();
    public static int i = 1;

    public static void main(String[] args) throws FileNotFoundException {

        initiate();

        File file = new File(args[0]);

        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()){
            instructions.add(scan.nextLine());
        }

        convert();
    }

    public static void initiate(){

        convert.put(0b10001011000,"ADD");
        convert.put(0b1001000100,"ADDI");
        convert.put(0b10001010000,"AND");
        convert.put(0b1001001000,"ANDI");

        convert.put(0b000101,"B");
        convert.put(0b100101,"BL");
        convert.put(0b11010110000,"BR");
        convert.put(0b01010100,"B");

        convert.put(0b10110101,"CBNZ");
        convert.put(0b10110100,"CBZ");

        convert.put(0b11001010000,"EOR");
        convert.put(0b1101001000,"EORI");

        convert.put(0b11111000010,"LDUR");
        convert.put(0b11010011011,"LSL");
        convert.put(0b11010011010,"LSR");

        convert.put(0b10101010000,"ORR");
        convert.put(0b1011001000,"ORRI");

        convert.put(0b11111000000,"STUR");
        convert.put(0b11001011000,"SUB");
        convert.put(0b1101000100,"SUBI");
        convert.put(0b1111000100,"SUBIS");
        convert.put(0b11101011000,"SUBS");

        convert.put(0b10011011000,"MUL");

        convert.put(0b11111111101,"PRNT");
        convert.put(0b11111111100,"PRNL");

        convert.put(0b11111111110,"DUMP");

        convert.put(0b11111111111,"HALT");

        cond.put(0x0, "EQ");
        cond.put(0x1, "NE");
        cond.put(0x2, "HS");
        cond.put(0x3, "LO");
        cond.put(0x4, "MI");
        cond.put(0x5, "PL");
        cond.put(0x6, "VS");
        cond.put(0x7, "VC");
        cond.put(0x8, "HI");
        cond.put(0x9, "LS");
        cond.put(0xa, "GE");
        cond.put(0xb, "LT");
        cond.put(0xc, "GT");
        cond.put(0xd, "LE");


    }
    static int type;

    public static void convert(){
        for (int i = 0; i < instructions.size(); i++) {

            // Type R: ADD, SUB, AND, ORR, LSR, LSL, EOR, SUBS, MUL, PRNT, PRNL, HALT, DUMP
            type = Integer.parseInt(instructions.get(i).substring(0, instructions.get(i).length() - 21), 2);
            if ((type == 1112) || (type == 1624) || (type == 1104) || (type == 1360) || (type == 1690) || (type == 1691) || (type == 1616) || (type == 1880) || (type == 1240) || (type == 2045) || (type == 2044) || (type == 2047) || (type == 2046)){
                R(instructions.get(i));
                continue;
            }

            // Type D: LDUR, STUR
            if ((type == 1986) || (type == 1984)) {
                D(instructions.get(i));
                continue;
            }

            // Type I: ADDI, SUBI, EORI, ORRI, SUBIS, ANDI
            type = Integer.parseInt(instructions.get(i).substring(0, instructions.get(i).length() - 22), 2);
            if ((type == 580) || (type == 836) || (type == 840) || (type == 712) || (type == 964) || (type == 584)) {
                I(instructions.get(i));
                continue;
            }
            // Type CB: CBZ, CBNZ, B.cond
            type = Integer.parseInt(instructions.get(i).substring(0, instructions.get(i).length() - 24), 2);
            if ((type == 180) || (type == 181) || (type == 84)) {
                CB(instructions.get(i));
                continue;
            }
            // Type B: B, BL
            type = Integer.parseInt(instructions.get(i).substring(0, instructions.get(i).length() - 26), 2);
            if ((type == 5) || (type == 37)) {
                B(instructions.get(i));
            }

        }
    }

    public static void R(String inst){
        String line;

        int type = Integer.parseInt(inst.substring(inst.length() - 32, inst.length() - 21), 2);

        int one = Integer.parseInt(inst.substring(inst.length() - 5, inst.length()), 2);
        int two = Integer.parseInt(inst.substring(inst.length() - 10, inst.length() - 5), 2);
        int three = Integer.parseInt(inst.substring(inst.length() - 21, inst.length() - 16), 2);
        int shamt  = Integer.parseInt(inst.substring(inst.length() - 16, inst.length() - 10), 2);

        if(type == 1690 || type == 1691){ // LSR / LSL
            line = convert.get(type) + " X" + one + ", X" + two + ", #" + shamt;
        }
        else if(type == 2045){
            line = convert.get(type) + " X" + one;
        }
        else if(type == 2044){
            line = convert.get(type);
        }
        else if(type == 2046){
            line = convert.get(type);
        }
        else if(type == 2047){
            line = convert.get(type);
        }
        else {
            line = convert.get(type) + " X" + one + ", X" + two + ", X" + three;
        }

        System.out.println(line);
    }

    public static void D(String inst){
        String line;

        int type = Integer.parseInt(inst.substring(inst.length() - 32, inst.length() - 21), 2);

        int one = Integer.parseInt(inst.substring(inst.length() - 5, inst.length()), 2);
        int two = Integer.parseInt(inst.substring(inst.length() - 10, inst.length() - 5), 2);
        int three = Integer.parseInt(inst.substring(inst.length() - 21, inst.length() - 12), 2);

        line = convert.get(type) + " X" + one + ", [X" + two + ", #" + three + "]";

        System.out.println(line);
    }

    public static void I(String inst){
        String line;

        int type = Integer.parseInt(inst.substring(inst.length() - 32, inst.length() - 22), 2);

        int one = Integer.parseInt(inst.substring(inst.length() - 5, inst.length()), 2);
        int two = Integer.parseInt(inst.substring(inst.length() - 10, inst.length() - 5), 2);
        int three = Integer.parseInt(inst.substring(inst.length() - 22, inst.length() - 10), 2);

        line = convert.get(type) + " X" + one + ", X" + two + ", #" + three;

        System.out.println(line);
    }

    public static void CB(String inst){
        String line;

        int type = Integer.parseInt(inst.substring(inst.length() - 32, inst.length() - 24), 2);

        int one = Integer.parseInt(inst.substring(inst.length() - 24, inst.length() - 5), 2);
        int two = Integer.parseInt(inst.substring(inst.length() - 5), 2);

        if(type == 84){
            line = convert.get(type) + "." + cond.get(two) + " label" + i;
            i += 1;
        }
        else {
            line = convert.get(type) + " X" + two + ", #" + one;
        }

        System.out.println(line);

    }

    public static void B(String inst){
        String line;

        int type = Integer.parseInt(inst.substring(inst.length() - 32, inst.length() - 26), 2);

        int one = Integer.parseInt(inst.substring(inst.length() - 26), 2);

        line = convert.get(type) + " " + "label" + i;
        i += 1;

        System.out.println(line);
    }

}
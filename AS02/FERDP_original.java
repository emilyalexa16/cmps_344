/* FERDP.java, 2024-05-14
   Copyright 2024 Paul M. Jackowitz
*/
import java.util.Scanner;
import java.io.*;

public class FERDP {

   static final int TOKEN_NL=0;
// --------------------------------------------------
   static final int TOKEN_ERROR = -1;
// --------------------------------------------------
   static final int TOKEN_LP = 1;
   static final int TOKEN_RP = 2;
   static final int TOKEN_ID = 3;
   static final int TOKEN_IL = 4;
   static final int TOKEN_SL = 5;
// --------------------------------------------------
   static final int TOKEN_LIMIT = 4096;
   static int[] token = new int[TOKEN_LIMIT];
   static int n;
   static int level = 0;
   
///////////////////////////////////////////////////////////////////////////////////////////////////
// main and preliminaries
///////////////////////////////////////////////////////////////////////////////////////////////////
  
   public static void main(String[] args) throws FileNotFoundException, IOException {
      System.out.println("PMJ's FERDP 2025..27 ...");
      Scanner tokenInput;
      if(args.length > 0) {
         tokenInput = new Scanner(new File(args[0]));
         n = readInput(tokenInput);
         System.out.println(recognize());
      } 
      else {
         System.out.println("---ERROR: missing filename argument");
      }
   }
   
   public static int readInput(Scanner input) {
      int result = 0;
      int sourceLine = 0;
      while(input.hasNextInt() && (result < token.length)) {
         token[result] = input.nextInt(); 
         //if(token[result] != TOKEN_NL) {
            result = result + 1;
         //}
      }
      if(input.hasNextInt()) {
         System.out.println("---ERROR: token memory filled");
      } 
      return result;
   }
   
   public static void printIndent() {
      for(int tab=level; tab>0; tab=tab-1) {  
         System.out.print("  ");
      }
   }
   
   public static void printPre(String s, int startIndex) {
      printIndent();
      System.out.println("<<"+s+printAhead(startIndex,1));
      level = level + 1;
   }
   
   public static void printPost(String s, int startIndex, int k) {
      level = level - 1;
      printIndent();
      System.out.println(">>"+s+printAhead(startIndex,k));
   }
   
   public static String printAhead(int startIndex, int k) {
      if((startIndex+k) >= n) { k = n - startIndex; }
      String result = " "+startIndex+":[";
      for(int i=0; i<k; i++) {
         result = result + token[startIndex+i];
         if((i+1)<k) { result = result + ","; }
      }
      result = result + "]";
      return result;
   }
   
   public static void printError(String message) {
      System.out.println("===> Syntax Error: " + message);
   }
   
   public static int next(int index) {
      while((index < n) && (token[index] == TOKEN_NL)) {
         index = index + 1;
      }
      return index;
   }
   
   public static boolean recognize() {
      int recognized = Unit(0);
      return (recognized == n);      
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////
// Recursive Descent Parsing Methods
///////////////////////////////////////////////////////////////////////////////////////////////////
   
   public static int Unit(int startIndex) {
      printPre("Unit",startIndex);
      int result = 0;
      startIndex = next(startIndex);
      int nSE = SE(startIndex);
      int nSEs = 0;
      if(nSE > 0) {
         nSEs = SEs(startIndex+nSE);
      }
      result = nSE + nSEs;
      printPost("Unit",startIndex,result);
      return result;
   }
   
   public static int SEs(int startIndex) {
      int result = 0;
      startIndex = next(startIndex);
      if(startIndex < n) {
         printPre("SEs",startIndex);
         result = SE(startIndex);
         if((result > 0) && ((startIndex+result) < n)) {
            result = result + SEs(startIndex+result);
         }
         printPost("SEs",startIndex,result);
      }
      return result;
   }
   
   public static int SE(int startIndex) {
      int result = 0;
      startIndex = next(startIndex);
      if(startIndex < n) {
         printPre("SE",startIndex);
         result = SA(startIndex);
         if(result == 0) {
            result = LA(startIndex);
            if(result == 0) {
               result = L(startIndex);
            }
         }
         printPost("SE",startIndex,result);
      }
      return result;
   }
   
   public static int SA(int startIndex) {
      printPre("SA",startIndex);
      startIndex = next(startIndex);
      int result =ID(startIndex);
      printPost("SA",startIndex,result);
      return result;
   }
   
   public static int LA(int startIndex) {
      printPre("LA",startIndex);
      startIndex = next(startIndex);
      int result = IL(startIndex);
      if(result == 0) {
         result = SL(startIndex);
      }
      printPost("LA",startIndex,result);
      return result;
   }
   
   public static int L(int startIndex) {
      printPre("L",startIndex);
      startIndex = next(startIndex);
      int result = 0;
      int nL = 0;
      if(token[startIndex] == TOKEN_LP) {
         nL = 1 + SEs(startIndex+1);
         if(token[startIndex+nL] == TOKEN_RP) {
            result = nL + 1;
         } else {
            printError("matching right parenthesis missing!");
         }
      }
      printPost("L",startIndex,result);
      return result;
   }
   
   public static int ID(int startIndex) {
      printPre("ID",startIndex);
      startIndex = next(startIndex);
      int result = 0;
      if(token[startIndex] == TOKEN_ID) { result = 1; }
      printPost("ID",startIndex,result);
      return result;
   }
   
   public static int IL(int startIndex) {
      printPre("IL",startIndex);
      startIndex = next(startIndex);
      int result = 0;
      if(token[startIndex] == TOKEN_IL) { result = 1; }
      printPost("IL",startIndex,result);
      return result;
   }
   
   public static int SL(int startIndex) {
      printPre("SL",startIndex);
      startIndex = next(startIndex);
      int result = 0;
      if(token[startIndex] == TOKEN_SL) { result = 1; }
      printPost("SL",startIndex,result);
      return result;
   }
   
}
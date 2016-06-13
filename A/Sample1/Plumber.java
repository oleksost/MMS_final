/******************************************************************************************************************
* File:Plumber.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example to illstrate how to use the PlumberTemplate to create a main thread that
* instantiates and connects a set of filters. This example consists of three filters: a source, a middle filter
* that acts as a pass-through filter (it does nothing to the data), and a sink filter which illustrates all kinds
* of useful things that you can do with the input stream of data.
*
* Parameters: 		None
*
* Internal Methods:	None
*
******************************************************************************************************************/
public class Plumber
{
   public static void main( String argv[]) 
   {
	   String input_path="";
	   String output_path="";
	   if (argv.length==2){
		   input_path=argv[0];
		   output_path=argv[1];
	  
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/
		SourceFilter Filter1 = new SourceFilter(input_path);
		MiddleFilter_altitude_convertion Filter2 = new MiddleFilter_altitude_convertion();
		MiddleFilter_tempreture_convertion Filter3 = new MiddleFilter_tempreture_convertion();
		SystemA_SinkFilter Filter4 = new SystemA_SinkFilter(output_path);
		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/
		Filter4.Connect(Filter3); 
		Filter3.Connect(Filter2); 
		Filter2.Connect(Filter1);
		
		
		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/

		Filter1.start();
		Filter2.start();
		Filter3.start();
		Filter4.start();
	   }else{
		   System.out.println("Please, type in the input file path and the output file path as parameters");
		   
	   }

   } // main

} // Plumber
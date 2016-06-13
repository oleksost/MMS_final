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
	   String output_path_outlier="";
	   String output_path_result="";
	   
	   if (argv.length==2){
		   input_path=argv[0];
		   output_path_outlier=argv[1];
		   output_path_result=argv[2];
		   
		   
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/
		SourceFilter Filter_Source = new SourceFilter(input_path);
		
		MiddleFilter_altitude_convertion Filter_alt_convertion = new MiddleFilter_altitude_convertion();
		
		MiddleFilter_tempreture_convertion Filter_tempreture_conv = new MiddleFilter_tempreture_convertion();
		
		Filter_correct Filter_correct_values = new Filter_correct();
		Filter_outlier Filter_outlier_values = new Filter_outlier();
		SinkFilter_outlier Sinkfilter_Outliers = new SinkFilter_outlier(output_path_outlier);
		SinkFilter_correct Sinkfilter_correct = new SinkFilter_correct(output_path_result);
		
		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/

		Sinkfilter_correct.Connect(Filter_correct_values,0);
		Sinkfilter_Outliers.Connect(Filter_outlier_values,0);
		Filter_correct_values.Connect(Filter_tempreture_conv,1);
		Filter_outlier_values.Connect(Filter_tempreture_conv,0);
		Filter_tempreture_conv.Connect(Filter_alt_convertion,0);
		Filter_alt_convertion.Connect(Filter_Source,0);
		
		
		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/
		Filter_Source.start();
		Filter_alt_convertion.start();
		Filter_tempreture_conv.start();
		Filter_correct_values.start();
		Filter_outlier_values.start();
		Sinkfilter_Outliers.start();
		Sinkfilter_correct.start();
	   }else{
		   System.out.println("Please, type in the input file path and the output file path for outlier and the result files as parameters");
		   
	   }
		

   } // main

} // Plumber
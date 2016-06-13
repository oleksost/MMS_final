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
public class Plumber {
 public static void main(String argv[]) throws InterruptedException {
	 
	   String input_path_1="";
	   String input_path_2="";
	   String output_path_under10K="";
	   String output_path_outlier="";
	   String output_path_result="";
	   
	 if (argv.length==2){
		   input_path_1=argv[0];
		   input_path_2=argv[1];
		   output_path_under10K=argv[2];
		   output_path_outlier=argv[3];
		   output_path_result=argv[4];
   /****************************************************************************
    * Here we instantiate three filters.
    ****************************************************************************/
   //SourceFilter Source_Filter1 = new SourceFilter("/Users/d065820/Documents/HU_Belrin/MMS/MMSE-Ex-Sheet-02-sources/DataSets/SubSetA.dat");
   //SourceFilter Source_Filter2 = new SourceFilter("/Users/d065820/Documents/HU_Belrin/MMS/MMSE-Ex-Sheet-02-sources/DataSets/SubSetB.dat");
   SourceFilter Source_Filter1 = new SourceFilter(input_path_1);
   SourceFilter Source_Filter2 = new SourceFilter(input_path_2);
   Filter_merge merge_Filter = new Filter_merge();
   Filter_sort_on_date date_sorter = new Filter_sort_on_date();
   Filter10K LessThan10K_Filter = new Filter10K(output_path_under10K);
   MiddleFilter_altitude_convertion alt_conv = new MiddleFilter_altitude_convertion();
   MiddleFilter_tempreture_convertion temp_conv = new MiddleFilter_tempreture_convertion();
   Filter_correct filter_correct = new Filter_correct();
   SinkFilter_correct Sink_correct = new SinkFilter_correct(output_path_result);
   Filter_outlier filter_outlier = new Filter_outlier();
   SinkFilter_outlier Sink_outlier = new SinkFilter_outlier(output_path_outlier);

   /*
    * Connect the filter to a chain
    */
   merge_Filter.Connect(new FilterFramework[] {
    Source_Filter1,
    Source_Filter2
   }, 0);
   date_sorter.Connect(new FilterFramework[] {
    merge_Filter
   }, 0);
   LessThan10K_Filter.Connect(new FilterFramework[] {
    date_sorter
   }, 0);
   alt_conv.Connect(new FilterFramework[] {
    LessThan10K_Filter
   }, 0);
   temp_conv.Connect(new FilterFramework[] {
    alt_conv
   }, 0);
   filter_correct.Connect(new FilterFramework[] {
    temp_conv
   }, 0);
   Sink_correct.Connect(new FilterFramework[] {
    filter_correct
   }, 0);
   filter_outlier.Connect(new FilterFramework[] {
    temp_conv
   }, 1);
   Sink_outlier.Connect(new FilterFramework[] {
    filter_outlier
   }, 0);

   /****************************************************************************
    * Here we start the filters up. All-in-all,... its really kind of boring.
    ****************************************************************************/

   Source_Filter1.start();
   Source_Filter2.start();
   merge_Filter.start();
   date_sorter.start();
   LessThan10K_Filter.start();
   alt_conv.start();
   temp_conv.start();
   filter_correct.start();
   Sink_correct.start();
   filter_outlier.start();
   Sink_outlier.start();
	 }
	 else{
		   System.out.println("Please, type in the input file path and the output file path for outlier and the result files as parameters");
		   
	 }
  } // main

} // Plumber
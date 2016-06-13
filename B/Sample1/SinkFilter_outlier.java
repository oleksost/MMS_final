import java.util.*;						// This class is used to interpret time words

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

public class SinkFilter_outlier extends FilterFramework {
public String output_path;
	
	public SinkFilter_outlier(String fileName){
		output_path= fileName;
	}

	public void run() 
    {
		/************************************************************************************
		*	TimeStamp is used to compute time using java.util's Calendar class.
		* 	TimeStampFormat is used to format the time value so that it can be easily printed
		*	to the terminal.
		*************************************************************************************/

		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
		PrintWriter writer;
		try {
			//writer = new PrintWriter("/Users/d065820/Documents/HU_Belrin/MMS/MMS Abgabe/B_v2/Output/Outlier.dat", "UTF-8");
			writer = new PrintWriter(output_path, "UTF-8");
										
			
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		int IdLength = 4;				// This is the length of IDs in the byte stream

		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		double cash_Pressure=0.0;
		double erg_id2 = 0.0;
		
		
		DecimalFormat dfm = new DecimalFormat("000000.00000");
		DecimalFormat dft = new DecimalFormat("000.00000");
		
		writer.format("%1s%30s","Time:", "Pressure(psi):");
		writer.print("\n____________________________________________________________");
		writer.print("\n");
		
		
		/*************************************************************
		*	First we announce to the world that we are alive...
		**************************************************************/

			while (true)
			{
				try
				{
					/***************************************************************************
					// We know that the first data coming to this filter is going to be an ID and
					// that it is IdLength long. So we first decommutate the ID bytes.
					****************************************************************************/
					id = 0;
					byte[] id_bytes=new byte[8];
					for (i=0; i<8; i++ )
					{
						databyte = ReadFilterInputPort();	// This is where we read the byte from the stream...
						id_bytes[i]=databyte;
						bytesread++;						// Increment the byte count

					} // for
					
					id = (int) bytesToLong(id_bytes);

					/****************************************************************************
					// Here we read measurements. All measurement data is read as a stream of bytes
					// and stored as a long value. This permits us to do bitwise manipulation that
					// is neccesary to convert the byte stream into data words. Note that bitwise
					// manipulation is not permitted on any kind of floating point types in Java.
					// If the id = 0 then this is a time value and is therefore a long value - no
					// problem. However, if the id is something other than 0, then the bits in the
					// long value is really of type double and we need to convert the value using
					// Double.longBitsToDouble(long val) to do the conversion which is illustrated.
					// below.
					*****************************************************************************/

					measurement = 0;
					//long measurement_time = 0;
					
					byte[] measurement_bytes=new byte[8];
					for (i=0; i<MeasurementLength; i++ )
					{
						databyte = ReadFilterInputPort();
						measurement_bytes[i]=databyte;
						bytesread++;									// Increment the byte count

					} // if
					
					measurement= bytesToLong(measurement_bytes);

					
					/****************************************************************************
					// Here we look for an ID of 0 which indicates this is a time measurement.
					// Every frame begins with an ID of 0, followed by a time stamp which correlates
					// to the time that each proceeding measurement was recorded. Time is stored
					// in milliseconds since Epoch. This allows us to use Java's calendar class to
					// retrieve time and also use text format classes to format the output into
					// a form humans can read. So this provides great flexibility in terms of
					// dealing with time arithmetically or for string display purposes. This is
					// illustrated below.
					****************************************************************************/
	
					if (id==0){
						TimeStamp.setTimeInMillis(measurement);
						System.out.println("Time: "+ TimeStampFormat.format((TimeStamp.getTime())));
					}// if 
	
					/****************************************************************************
					// Here we pick up a measurement (ID = 3 in this case), but you can pick up
					// any measurement you want to. All measurements in the stream are
					// decommutated by this class. Note that all data measurements are double types
					// This illustrates how to convert the bits read from the stream into a double
					// type. Its pretty simple using Double.longBitsToDouble(long value). So here
					// we print the time stamp and the data associated with the ID we are interested
					// in.
					****************************************************************************/
					
					
					//System.out.println("id: "+id);
					
					if ( id == 2 )
					{
						writer.format("%1s%20s",TimeStampFormat.format(TimeStamp.getTime()), dfm.format(Double.longBitsToDouble(measurement)));
						writer.print("\n");	
					} // if

				} // try
	
				/*******************************************************************************
				*	The EndOfStreamExeception below is thrown when you reach end of the input
				*	stream (duh). At this point, the filter ports are closed and a message is
				*	written letting the user know what is going on.
				********************************************************************************/
	
				catch (EndOfStreamException e)
				{
					ClosePorts();
					//System.out.print( "\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread );
					writer.close();
					break;
	
				} // catch
	
			} // while
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		System.out.println("\n" + "System B wurde ausgeführt und ist nun fertig");
		
   } // run
}

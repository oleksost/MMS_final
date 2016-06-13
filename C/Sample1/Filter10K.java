import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/******************************************************************************************************************
* File:MiddleFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

public class Filter10K extends FilterFramework
{
    public String output_path;
	
	public Filter10K(String fileName){
		output_path= fileName;
	}
	public void run()
    {
		try {
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		long erg = 0;
		long currentTime=0;
		int byteswritten = 0;				// Number of bytes written to the stream.
		
		// Next we write a message to the terminal to let the world know we are alive...
		//PrintWriter writer_below_10000_feet = new PrintWriter("/Users/d065820/Documents/HU_Belrin/MMS/MMS Abgabe/C_v2/Output/LessThan.dat", "UTF-8");
		PrintWriter writer_below_10000_feet = new PrintWriter(output_path, "UTF-8");
		
		writer_below_10000_feet.format("%1s%30s%20s%20s","Time:", "Temperature(C):", "Altitude(m):", "Pressure(psi):");
		writer_below_10000_feet.print("\n______________________________________________________________________________________");
		
		System.out.print( "\n" + this.getName() + "::Middle Reading ");
		ArrayList<Long> datasetbelow_10000_feet= new ArrayList<Long>();
		

		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/

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
					databyte = ReadFilterInputPort(InputReadPorts.get(0),InputFilters.get(0));	// This is where we read the byte from the stream...
					id_bytes[i]=databyte;
					bytesread++;						// Increment the byte count

				} // for
				
				id = (int) bytesToLong(id_bytes);

				measurement = 0;
				//long measurement_time = 0;
				
				byte[] measurement_bytes=new byte[8];
				for (i=0; i<MeasurementLength; i++ )
				{
					databyte = ReadFilterInputPort(InputReadPorts.get(0),InputFilters.get(0));
					measurement_bytes[i]=databyte;
					bytesread++;									// Increment the byte count

				} // if
				measurement= bytesToLong(measurement_bytes);
				
				
				if (id==0){
					
					//TimeStamp.setTimeInMillis(measurement);
					//System.out.println(TimeStampFormat.format(TimeStamp.getTime()));
					currentTime=measurement;
					datasetbelow_10000_feet=new ArrayList<Long>();
				}
				
				if ( id == 2 )
				{
					if (Double.longBitsToDouble(measurement)<10000.0){
						//TODO: Write in File
						datasetbelow_10000_feet.add(currentTime);
						datasetbelow_10000_feet.add(measurement);
					}else{
						erg=measurement;
						TimeStamp.setTimeInMillis(currentTime);
						System.out.println(TimeStampFormat.format(TimeStamp.getTime()));
						WriteFilterOutputPort_Double(longToBytes(0));
						WriteFilterOutputPort_Double(longToBytes(currentTime));
						WriteFilterOutputPort_Double(longToBytes(id));
						WriteFilterOutputPort_Double(longToBytes(erg));
					}
				}
				if (id==3){
					if(datasetbelow_10000_feet.size()==0){
						erg=measurement;
						WriteFilterOutputPort_Double(longToBytes(id));
						WriteFilterOutputPort_Double(longToBytes(erg));
					}else{
						datasetbelow_10000_feet.add(measurement);
					}
				}
				if (id==4){
					if(datasetbelow_10000_feet.size()==0){
						erg=measurement;
						WriteFilterOutputPort_Double(longToBytes(id));
						WriteFilterOutputPort_Double(longToBytes(erg));
					}else{
						datasetbelow_10000_feet.add(measurement);
						//System.out.println(TimeStampFormat.format(Double.longBitsToDouble(datasetbelow_10000_feet.get(0))));
						writer_below_10000_feet.print("\n");
						writer_below_10000_feet.format("%1s%30s%20s%20s", TimeStampFormat.format(datasetbelow_10000_feet.get(0)), Double.longBitsToDouble(datasetbelow_10000_feet.get(3)) ,Double.longBitsToDouble(datasetbelow_10000_feet.get(1)), Double.longBitsToDouble(datasetbelow_10000_feet.get(2))); 

					}
				}
				
				
			} // try
			catch (EndOfStreamException e)
			{
				//ClosePorts(InputReadPorts.get(0));
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				//writer_below_10000_feet.close();
				break;

			} // catch

		} // while
		writer_below_10000_feet.close();

		} //try
        catch (FileNotFoundException | UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
   } // run

} // MiddleFilter
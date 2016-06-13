import java.text.SimpleDateFormat;
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

public class MiddleFilter_altitude_convertion extends FilterFramework
{
	public void run()
    {
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes

		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		long erg_id2 = 0;

		int byteswritten = 0;				// Number of bytes written to the stream.

		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");

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
				byte[] measurement_bytes=new byte[8];
				for (i=0; i<MeasurementLength; i++ )
				{
					databyte = ReadFilterInputPort(InputReadPorts.get(0),InputFilters.get(0));
					measurement_bytes[i]=databyte;
					bytesread++;									// Increment the byte count

				} // if
				measurement= bytesToLong(measurement_bytes);
				
				
				
				
				
				if (id==0){
					TimeStamp.setTimeInMillis(measurement);
					System.out.println(TimeStampFormat.format(TimeStamp.getTime()));
				}
				if ( id == 2 )
				{
					erg_id2=Double.doubleToRawLongBits(Double.longBitsToDouble(measurement)/3.28084); 
				}
				else{
					erg_id2 =measurement; 
				}
				
				WriteFilterOutputPort_Double(longToBytes(id));
				WriteFilterOutputPort_Double(longToBytes(erg_id2));
			} // try
			catch (EndOfStreamException e)
			{
				ClosePorts(InputReadPorts.get(0));
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch

		} // while

   } // run

} // MiddleFilter
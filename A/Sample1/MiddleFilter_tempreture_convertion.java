import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MiddleFilter_tempreture_convertion extends FilterFramework{

	public void run()
    {
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		int IdLength = 4;				// This is the length of IDs in the byte stream

		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		double cash_Pressure=0.0;
		long erg = 0;

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

				if ( id == 4 )
				{
					//System.out.println(id);
					erg = Double.doubleToRawLongBits((Double.longBitsToDouble(measurement)-32.0)/1.8); 
				}else{	
					erg = measurement; 
				}
				
				WriteFilterOutputPort_Double(longToBytes(id));
				WriteFilterOutputPort_Double(longToBytes(erg));
				
				
			} // try
			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch

		} // while

   } // run

}

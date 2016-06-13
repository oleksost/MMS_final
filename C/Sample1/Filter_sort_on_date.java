import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class Filter_sort_on_date extends FilterFramework{
 public void run(){
	    Calendar TimeStamp = Calendar.getInstance();
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		int byteswritten = 0;				// Number of bytes written to the stream.
		// Next we write a message to the terminal to let the world know we are alive...
		System.out.println("Sorter");
		System.out.print( "\n" + this.getName() + "::Middle Reading ");
		ArrayList<ArrayList<Long>> Complete=new ArrayList<ArrayList<Long>>();
		ArrayList<Long> dataset= new ArrayList<Long>();
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

				if (id ==0){
					TimeStamp.setTimeInMillis(measurement);
					dataset=new ArrayList<Long>();
					dataset.add(measurement);
					
				}else if(!(id==5)){
					dataset.add(measurement);
				} if (id ==5){
					dataset.add(measurement);
					Complete.add(dataset);
				}
				
			} // try
			catch (EndOfStreamException e)
			{
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch

		} // while
		
		//sort
		Collections.sort(Complete, new Comparator<ArrayList<Long>>() {
		    @Override
		    public int compare(ArrayList<Long> z1, ArrayList<Long> z2) {
		        if (z1.get(0) > z2.get(0))
		            return 1;
		        if (z1.get(0) < z2.get(0))
		            return -1;
		        return 0;
		    }
		});
		//System.out.println("Sorter write...." + Complete.get(2).size());  
		//write to the pipe 
		for (ArrayList<Long> data_tupel : Complete){
			int idi=0;
			for (Long a : data_tupel){
				WriteFilterOutputPort_Double(longToBytes(idi));
				WriteFilterOutputPort_Double(longToBytes(a));
				idi++;
			}
		}
		System.out.println("Sorter done");
	    ClosePorts(InputReadPorts.get(0));
   } // run

}

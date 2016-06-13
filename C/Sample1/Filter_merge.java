import java.io.PipedInputStream;

public class Filter_merge extends FilterFramework {

	public void run(){
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		int IdLength = 4;				// This is the length of IDs in the byte stream
		byte databyte = 0;				// This is the data byte read from the stream
		long measurement=0;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
	System.out.println("Merger");
	for (int aa=0; aa<InputFilters.size(); aa++){
		System.out.println("haha");
		FilterFramework Filter = InputFilters.get(aa);
		PipedInputStream InputFilter = InputReadPorts.get(aa);
	while (true)
	{
		try
		{
			/***************************************************************************
			// We know that the first data coming to this filter is going to be an ID and
			// that it is IdLength long. So we first decommutate the ID bytes.
			****************************************************************************/
			
			//databyte = ReadFilterInputPort(InputFilter,Filter);
			//bytesread++;
			

			id = 0;

			for (i=0; i<IdLength; i++ )
			{
				databyte = ReadFilterInputPort(InputFilter,Filter);	// This is where we read the byte from the stream...
				

				id = id | (databyte & 0xFF);		// We append the byte on to ID...

				if (i != IdLength-1)				// If this is not the last byte, then slide the
				{									// previously appended byte to the left by one byte
					id = id << 8;					// to make room for the next byte we append to the ID

				} // if

			} // for
			measurement = 0;

			for (i=0; i<MeasurementLength; i++ )
			{
				databyte = ReadFilterInputPort(InputFilter,Filter);
				//System.out.println(databyte);
				measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...
				//System.out.println(measurement);
				if (i != MeasurementLength-1)					// If this is not the last byte, then slide the
				{												// previously appended byte to the left by one byte
					measurement = measurement << 8;				// to make room for the next byte we append to the
																// measurement
				} // if

			} // if
			
			WriteFilterOutputPort_Double(longToBytes(id));
			WriteFilterOutputPort_Double(longToBytes(measurement));


		} // try
		catch (EndOfStreamException e)
		{
			
			break;

		} // catch

	} // while
	
	//ClosePorts(InputFilter);
	
	}//for

	}
	}

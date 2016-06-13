import java.util.*;
public class Filter_correct extends FilterFramework {

 public void run() {
   /************************************************************************************
    *	TimeStamp is used to compute time using java.util's Calendar class.
    * 	TimeStampFormat is used to format the time value so that it can be easily printed
    *	to the terminal.
    *************************************************************************************/
   Calendar TimeStamp = Calendar.getInstance();
   List < ArrayList < Long >> Correct_set = new ArrayList < ArrayList < Long >> ();
   ArrayList < Long > dataset_right = new ArrayList < Long > ();
   int MeasurementLength = 8; // This is the length of all measurements (including time) in bytes

   byte databyte = 0; // This is the data byte read from the stream
   int bytesread = 0; // This is the number of bytes read from the stream

   long measurement = 0; // This is the word used to store all measurements - conversions are illustrated.
   int id; // This is the measurement id
   int i; // This is a loop counter
   double erg = 0.0;
   long altitude_cash = 0;
   long currentTime = 0;
   long cash_Pressure = 0;

   while (true) {
    try {
     /***************************************************************************
     // We know that the first data coming to this filter is going to be an ID and
     // that it is IdLength long. So we first decommutate the ID bytes.
     ****************************************************************************/
     id = 0;
     byte[] id_bytes = new byte[8];
     for (i = 0; i < 8; i++) {
      databyte = ReadFilterInputPort(InputReadPorts.get(0), InputFilters.get(0)); // This is where we read the byte from the stream...
      id_bytes[i] = databyte;
      bytesread++; // Increment the byte count

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

     byte[] measurement_bytes = new byte[8];
     for (i = 0; i < MeasurementLength; i++) {
      databyte = ReadFilterInputPort(InputReadPorts.get(0), InputFilters.get(0));
      measurement_bytes[i] = databyte;
      bytesread++; // Increment the byte count

     } // if
     measurement = bytesToLong(measurement_bytes);
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
     if (id == 0) {
      dataset_right = new ArrayList < Long > ();
      TimeStamp.setTimeInMillis(measurement);
      currentTime = measurement;
     } // if

     /****************************************************************************
     // Here we pick up a measurement (ID = 3 in this case), but you can pick up
     // any measurement you want to. All measurements in the stream are
     // decommutated by this class. Note that all data measurements are double types
     // This illustrates how to convert the bits read from the stream into a double
     // type. Its pretty simple using Double.longBitsToDouble(long value). So here
     // we print the time stamp and the data associated with the ID we are interested
     // in.
     ****************************************************************************/
     if (id == 2) {
      altitude_cash = measurement;
     } // if
     if (id == 3) {
      erg = Double.longBitsToDouble(measurement);
      if ((erg < 0.0 || Math.abs(Double.longBitsToDouble(cash_Pressure) - erg) > 10.0) && cash_Pressure > 0.0) {

       if (cash_Pressure == 0.0) {
        cash_Pressure = measurement;
       }

       dataset_right.add(Double.doubleToRawLongBits(0));
       dataset_right.add(Double.doubleToRawLongBits(0));
       dataset_right.add(Double.doubleToRawLongBits(0));
       

      } else {
       dataset_right.add(currentTime);
       dataset_right.add(altitude_cash);
       dataset_right.add(measurement);
       cash_Pressure = measurement;
      }
     }
     if (id == 4) {
      erg = new Double(Double.longBitsToDouble(measurement));
      dataset_right.add(measurement);
      Correct_set.add(dataset_right);
     } // if


    } // try

    /*******************************************************************************
     *	The EndOfStreamExeception below is thrown when you reach end of the input
     *	stream (duh). At this point, the filter ports are closed and a message is
     *	written letting the user know what is going on.
     ********************************************************************************/
    catch (EndOfStreamException e) {
     ClosePorts(InputReadPorts.get(0));
     break;

    } // catch

   } // while
   for (ArrayList < Long > data_tupel: Correct_set) {
    int idi = 0;
    for (Long a: data_tupel) {
     WriteFilterOutputPort_Double(longToBytes(idi));
     WriteFilterOutputPort_Double(longToBytes(a));
     idi++;
    }
   }


  } // run
}
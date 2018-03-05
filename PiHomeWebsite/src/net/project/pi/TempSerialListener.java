package net.project.pi;


import com.pi4j.io.serial.*;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This example code demonstrates how to perform serial communications using the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class TempSerialListener {

	private static Log log = LogFactory.getLog(TempSerialListener.class);
	private static Serial serial;

	 /**
     * This example program supports the following optional command arguments/options:
     *   "--device (device-path)"                   [DEFAULT: /dev/ttyAMA0]
     *   "--baud (baud-rate)"                       [DEFAULT: 38400]
     *   "--data-bits (5|6|7|8)"                    [DEFAULT: 8]
     *   "--parity (none|odd|even)"                 [DEFAULT: none]
     *   "--stop-bits (1|2)"                        [DEFAULT: 1]
     *   "--flow-control (none|hardware|software)"  [DEFAULT: none]
     *
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
	public static void startTempLogger(){ 

		log.info("Starting listener");
        // Please see this blog article for instructions on how to disable
        // the OS console for this port:
        // https://www.cube-controls.com/2015/11/02/disable-serial-port-terminal-output-on-raspbian/
        // create an instance of the serial communications class
        serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener(new DataListener());
//        serial.addListener(new SerialDataEventListener() {
//            @Override
//            public void dataReceived(SerialDataEvent event) {
//
//                // NOTE! - It is extremely important to read the data received from the
//                // serial port.  If it does not get read from the receive buffer, the
//                // buffer will continue to grow and consume memory.
//
//                // print out the data received to the console
//                try {
//                	System.out.println(event.getAsciiString());
//                	log.info("Enter info here. Display string: " +  event.getAsciiString());
//                	
//                	Temperature temp = new Temperature();
//                	temp.setRecordedDate(new Date());
//                	temp.setRecorderName(TempRecName.AA.name());
//                	temp.setBatteryLevel("00");
//                	temp.setTempC(0.0f);
//                	
//                	new TempManager().addTemperature(temp);
//                	
//                	
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    log.error("Error while recieving temp signal", e);
//                }
//            }
//        });

        try {
            // create serial config object
            SerialConfig config = new SerialConfig();

            // set default serial settings (device, baud rate, flow control, etc)
            //
            // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
            // NOTE: this utility method will determine the default serial port for the
            //       detected platform and board/model.  For all Raspberry Pi models
            //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
            //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
            //       environment configuration.
            config.device("/dev/ttyAMA0")
                  .baud(Baud._9600)
                  .dataBits(DataBits._8)
                  .parity(Parity.NONE)
                  .stopBits(StopBits._1)
                  .flowControl(FlowControl.NONE);

            // open the default serial device/port with the configuration settings
            serial.open(config);         

        }
        catch(IOException ex) {
           log.error("Error in startTempLogger, Please restart." , ex);
        }
    
	}
	
	public static void stopTempLogger() throws IllegalStateException, IOException{
		if (serial != null && serial.isOpen() ){
			serial.close();
		}

	}
}

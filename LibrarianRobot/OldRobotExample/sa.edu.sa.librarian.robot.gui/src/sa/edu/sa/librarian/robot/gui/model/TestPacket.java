package sa.edu.sa.librarian.robot.gui.model;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * the Packet for reliable communication
 */
public class TestPacket {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {

		String[] x = { "data 1", "data2" }; // the parameter for a command
		Packet packet_ToSend = new Packet(11, -133, x);
		System.out.println(packet_ToSend.isDiscard);
		System.out.println(packet_ToSend.getDataFrameInString());
		// this byte[] is used to send
		byte[] data = packet_ToSend.getDataFrameInByte();
		System.out.format("Byte Array : %s\n Rcv\n", Arrays.toString(data));

		// build packet by using receive data byte[]
		Packet packet_Received = new Packet(data);
		System.out.println("rcv:" + packet_Received.isDiscard);
		// the oringal String from Data
		String s = packet_Received.getDataFrameInString();
		System.out.println(s);

		int seq = packet_Received.getSequenceNumber();
		System.out.println(seq);

		int cmd = packet_Received.getCommand();
		System.out.println(cmd);

		String[] Parameters = packet_Received.getParameter();
		System.out.println(Arrays.toString(Parameters));
	}
}

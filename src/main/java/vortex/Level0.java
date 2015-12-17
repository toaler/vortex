package vortex;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Add these integers together and send back the results to get a username and
 * password for vortex1. This information can be used to log in using SSH.
 * 
 * Note: vortex is on an 32bit x86 machine (meaning, a little endian
 * architecture)
 * 
 * @author toaler
 *
 */
public class Level0 {

	public static void main(String[] args) {
		final String hostName = "vortext.labs.overthewire.org";
		final int portNumber = 5842;

		// Open socket
		try (Socket s = new Socket(hostName, portNumber)) {

			// Read in the 4 unsigned ints that are encoded in Little Endian
			// order
			long result = 0;
			byte[] buf = new byte[4];
			for (int j = 0; j < 4; j++) {
				s.getInputStream().read(buf, 0, 4);
				long value = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();
				// long value = getLongFromLittleEndianBytes(buf);
				result += value;
			}

			// Create response that encodes the bytes in Little Endian order
			ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
			b.order(ByteOrder.LITTLE_ENDIAN);
			b.putInt((int) result);

			// Send back sum of 4 unsigned ints
			s.getOutputStream().write(b.array());

			// Read username/password combination
			buf = new byte[1024];
			int size = s.getInputStream().read(buf);
			String u = new String(buf, 0, size);
			System.out.println(u);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static long getLongFromLittleEndianBytes(byte[] buf) {
		long value = 0;
		for (int i = 3; i >= 0; i--) {
			value += ((long) buf[i] & 0xffL) << (8 * i);
		}
		return value;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] longToBytes(long l) {
		byte[] result = new byte[4];
		for (int i = 4; i >= 0; i--) {
			result[i] = (byte) (l & 0xFF);
			l >>= 8;
		}
		return result;
	}
}

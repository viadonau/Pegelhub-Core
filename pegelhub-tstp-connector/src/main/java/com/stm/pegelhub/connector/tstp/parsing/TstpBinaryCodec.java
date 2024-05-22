package com.stm.pegelhub.connector.tstp.parsing;

import java.util.List;

import com.stm.pegelhub.lib.model.Measurement;

public interface TstpBinaryCodec {

	/**
	 * Decodes binary data as specified in the tstp protocol description.
	 *
	 * @param toDecode the binary that was recieved from the tstp server
	 * @return the decoded binary as  List of Measurements
	 */
	List<Measurement> decode(byte[] toDecode);

	/**
	 * Encodes binary data as specified in the tstp protocol description.
	 *
	 * @param toEncode
	 * @return
	 */
	byte[] encode(List<Measurement> toEncode);
}

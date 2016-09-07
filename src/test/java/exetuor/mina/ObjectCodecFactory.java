package exetuor.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

public class ObjectCodecFactory implements ProtocolCodecFactory{
	private final static ProtocolDecoder DECODER = new ProtocolDecoder();
	private final static ProtocolEncoder ENCODER = new ProtocolEncoder();
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return ENCODER;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return DECODER;
	}
}
